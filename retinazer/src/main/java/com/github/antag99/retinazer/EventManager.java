/*******************************************************************************
 * Copyright (C) 2015 Anton Gustafsson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.github.antag99.retinazer;

import static java.lang.String.format;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.antag99.retinazer.Event.WithEntity;
import com.github.antag99.retinazer.utils.Mask;

final class EventManager implements EntitySystem {

    /*
     * Event constraints are used to mask event handlers based on some criteria.
     *
     * TODO: Event constraints could be made public API in the future
     */
    private interface EventConstraint {

        public boolean accept(Event event);
    }

    private static final class TypeConstraint implements EventConstraint {
        private Class<? extends Event> type;

        public TypeConstraint(Class<? extends Event> type) {
            this.type = type;
        }

        @Override
        public boolean accept(Event event) {
            return type.isInstance(event);
        }

        @Override
        public int hashCode() {
            return type.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TypeConstraint))
                return false;
            return ((TypeConstraint) obj).type.equals(type);
        }
    }

    private static final class FamilyConstraint implements EventConstraint {
        private Class<?> type;
        private Method getter;
        private int family;

        public FamilyConstraint(Method getter, FamilyMatcher family) {
            this.type = getter.getDeclaringClass();
            this.getter = getter;
            this.family = family.index;
        }

        @Override
        public boolean accept(Event event) {
            try {
                return type.isInstance(event) &&
                        ((Entity) getter.invoke(event)).families.get(family);
            } catch (IllegalAccessException ex) {
                // Shouldn't happen as methods are marked as accessible
                throw new AssertionError(ex);
            } catch (InvocationTargetException ex) {
                // Avoid wrapping checked exceptions by bypassing the java compiler
                throw Internal.sneakyThrow(ex.getCause());
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof FamilyConstraint))
                return false;
            FamilyConstraint other = (FamilyConstraint) obj;
            return other.type.equals(type) &&
                    other.getter.equals(getter) &&
                    other.family == family;
        }

        @Override
        public int hashCode() {
            return (type.hashCode() * 31 + getter.hashCode()) * 31 + family;
        }
    }

    private static final class EventHandlerData {
        public final EventListener listener;
        public final int priority;
        public final Method method;

        public EventHandlerData(EventListener listener, int priority, Method method) {
            this.listener = listener;
            this.priority = priority;
            this.method = method;
        }
    }

    private Engine engine;
    private Set<Class<? extends Event>> eventTypes = new HashSet<>();
    private Map<EventConstraint, Mask> constraints = new HashMap<>();
    private EventHandlerData[] handlers = new EventHandlerData[0];

    public EventManager(Engine engine) {
        this.engine = engine;
        for (Class<? extends Event> eventType : engine.config.getEventTypes()) {
            eventTypes.add(eventType);
        }
    }

    private Mask getHandlers(EventConstraint constraint) {
        Mask handlerMask = constraints.get(constraint);
        if (handlerMask == null) {
            constraints.put(constraint, handlerMask = new Mask());
        }
        return handlerMask;
    }

    public void reset() {
        constraints.clear();
        handlers = new EventHandlerData[0];
    }

    public void dispatchEvent(Event event) {
        // Waste speed by checking for consistency
        if (!eventTypes.contains(event.getClass())) {
            throw new IllegalArgumentException("Event type "
                    + event.getClass().getName() + " has not been registered");
        }

        Mask excluded = engine.maskPool.obtain();
        Iterator<Entry<EventConstraint, Mask>> iterator = constraints.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<EventConstraint, Mask> constraint = iterator.next();
            if (!constraint.getKey().accept(event)) {
                excluded.or(constraint.getValue());
            }
        }
        EventHandlerData[] handlers = this.handlers;
        for (int i = excluded.nextClearBit(0); i != -1 && i < handlers.length; i = excluded.nextClearBit(i + 1)) {
            try {
                handlers[i].method.invoke(handlers[i].listener, event);
            } catch (IllegalAccessException ex) {
                // Shouldn't happen as methods are marked as accessible
                throw new AssertionError(ex);
            } catch (InvocationTargetException ex) {
                // Avoid wrapping checked exceptions by bypassing the java compiler
                throw Internal.sneakyThrow(ex.getCause());
            }
        }
    }

    public void addEventListener(EventListener listener) {
        if (listener == null)
            throw new NullPointerException("listener must not be null");

        for (int i = 0, n = handlers.length; i < n; i++) {
            if (handlers[i].listener == listener) {
                throw new IllegalArgumentException(
                        "The given listener has already been added");
            }
        }

        for (Method method : Internal.getAllMethods(listener.getClass())) {
            method.setAccessible(true);
            EventHandler eventHandler = method.getAnnotation(EventHandler.class);
            if (eventHandler != null) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException("Invalid @EventHandler:"
                            + " Expected event argument");
                }
                if (!Event.class.isAssignableFrom(parameterTypes[0])) {
                    throw new IllegalArgumentException("Invalid @EventHandler:"
                            + " Argument must be a subclass of Event");
                }
                Class<? extends Event> handlerType = parameterTypes[0].asSubclass(Event.class);

                if (!eventTypes.contains(handlerType)) {
                    throw new IllegalArgumentException("Event type "
                            + handlerType.getName() + " has not been registered");
                }

                int priority = eventHandler.priority();
                int insertionIndex = 0;
                while (insertionIndex < handlers.length) {
                    if (handlers[insertionIndex].priority > priority) {
                        break;
                    }
                    insertionIndex++;
                }

                for (Mask handlerMask : constraints.values()) {
                    handlerMask.push(insertionIndex);
                }

                TypeConstraint typeConstraint = new TypeConstraint(handlerType);
                getHandlers(typeConstraint).set(insertionIndex);

                // Replace defaults based on @WithEntity annotations
                for (WithEntity withEntity : eventHandler.value()) {
                    FamilyMatcher family = engine.getMatcher(Family
                            .with(withEntity.with())
                            .exclude(withEntity.exclude()));
                    String propertyName = withEntity.name();
                    String getterName = format("get%s%s",
                            Character.toUpperCase(propertyName.charAt(0)),
                            propertyName.substring(1));
                    Method getter;
                    try {
                        getter = handlerType.getMethod(getterName);
                        getter.setAccessible(true);
                    } catch (NoSuchMethodException ex) {
                        throw new IllegalArgumentException(
                                "No getter for property " + propertyName
                                        + " was found on " + handlerType.getName());
                    }
                    if (Modifier.isStatic(getter.getModifiers())) {
                        throw new IllegalArgumentException(
                                "Property " + propertyName + " cannot be static");
                    }
                    if (getter.getReturnType() != Entity.class) {
                        throw new IllegalArgumentException(
                                "Property " + propertyName + " must be an Entity");
                    }
                    FamilyConstraint constraint = new FamilyConstraint(getter, family);
                    getHandlers(constraint).set(insertionIndex);
                }

                EventHandlerData eventHandlerData = new EventHandlerData(
                        listener, priority, method);

                EventHandlerData[] newHandlers = new EventHandlerData[handlers.length + 1];
                System.arraycopy(handlers, 0, newHandlers, 0, insertionIndex);
                System.arraycopy(handlers, insertionIndex,
                        newHandlers, insertionIndex + 1,
                        handlers.length - insertionIndex);
                newHandlers[insertionIndex] = eventHandlerData;
                handlers = newHandlers;
            }
        }
    }

    public void removeEventListener(EventListener listener) {
        if (listener == null)
            throw new NullPointerException("listener must not be null");
        for (int i = 0, n = handlers.length; i < n; i++) {
            EventHandlerData eventHandler = handlers[i];
            if (eventHandler.listener == listener) {
                for (Mask handlerMask : constraints.values()) {
                    handlerMask.pop(i);
                }

                EventHandlerData[] newHandlers = new EventHandlerData[handlers.length - 1];
                System.arraycopy(handlers, 0, newHandlers, 0, i);
                System.arraycopy(handlers, i + 1, newHandlers, i, newHandlers.length - i);
                handlers = newHandlers;

                i--;
                n--;
            }
        }
    }
}
