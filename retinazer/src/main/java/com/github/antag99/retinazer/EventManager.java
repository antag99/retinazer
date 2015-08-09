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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.antag99.retinazer.Event.UseConstraintHandler;
import com.github.antag99.retinazer.utils.Mask;

final class EventManager extends EntitySystem {
    private Engine engine;
    private final Mask filledMask = new Mask();
    private final EventReceiver[] eventReceivers;
    private final EventConstraintHandler[] constraintHandlers;
    private final Pool<Object[]> arrayPool = new Pool<Object[]>() {
        @Override
        protected Object[] create() {
            return new Object[1];
        }

        @Override
        protected void destroy(Object[] object) {
            object[0] = null;
        }
    };

    public EventManager(Engine engine) {
        this.engine = engine;

        List<EventReceiver> receivers = new ArrayList<>();

        for (EntitySystem system : engine.getSystems()) {
            // Receivers registered for this system, to reduce complexity of
            // checking whether a method is overridden.
            List<EventReceiver> systemReceivers = new ArrayList<>();

            Class<?> current = system.getClass();
            while (current != null) {
                iterate: for (Method method : current.getDeclaredMethods()) {
                    method.setAccessible(true);
                    EventHandler handler = method.getAnnotation(EventHandler.class);
                    if (handler == null)
                        continue;

                    // Validate the signature of the event handler
                    if (method.getReturnType() != Void.TYPE)
                        throw new IllegalArgumentException(method.getName() + " must return void");

                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 1 || !Event.class.isAssignableFrom(parameterTypes[0]))
                        throw new IllegalArgumentException(method.getName() + " must take an Event as argument");

                    // Static methods may not be event handlers
                    if (Modifier.isStatic(method.getModifiers()))
                        throw new IllegalArgumentException(method.getName() + " must not be static methods");

                    // Default methods may not be event handlers
                    if (method.getDeclaringClass().isInterface())
                        throw new IllegalArgumentException(method.getName() + " must not be default methods");

                    // Check if the method is overridden in a subclass... this
                    // is tricky business, as overriding semantics are complex.

                    // Note that abstract methods are registered when they are
                    // encountered with an EventHandler annotation, unless the
                    // subclass also annotates the method with EventHandler.
                    for (int i = 0, n = systemReceivers.size(); i < n; i++) {
                        Method registeredMethod = systemReceivers.get(i).getMethod();

                        // For a method to be overridden, it must have the same name
                        // and an equal or assignable signature - e.g. may return
                        // subclass and accept superclass.
                        if (!method.getName().equals(registeredMethod.getName()) ||
                                !method.getParameterTypes()[0].isAssignableFrom(
                                        registeredMethod.getParameterTypes()[0])) {
                            continue;
                        }

                        // Private methods are never overridden.
                        int mod = method.getModifiers();
                        if (Modifier.isPrivate(mod)) {
                            continue;
                        }

                        // Package-private methods do not get overridden unless
                        // they are in the same package.
                        if (!Modifier.isPublic(mod)) {
                            Class<?> class0 = method.getDeclaringClass();
                            Class<?> class1 = registeredMethod.getDeclaringClass();
                            if (class0.getPackage() != class1.getPackage()) {
                                continue;
                            }
                        }

                        // Otherwise, the method has been overridden; don't
                        // register it twice.
                        continue iterate;
                    }

                    systemReceivers.add(new EventReceiver(system, method));
                }

                current = current.getSuperclass();
            }

            receivers.addAll(systemReceivers);
        }

        // Sort the receivers based on priority; lower means first
        Collections.sort(receivers, new Comparator<EventReceiver>() {
            @Override
            public int compare(EventReceiver o1, EventReceiver o2) {
                int p1 = o1.getHandler().priority();
                int p2 = o2.getHandler().priority();
                return p1 > p2 ? 1 : p2 > p1 ? -1 : 0;
            }
        });

        // Assign indices to all receivers
        for (int i = 0, n = receivers.size(); i < n; i++) {
            receivers.get(i).index = i;
            filledMask.set(i);
        }

        EventReceiver[] receieversAsArray = receivers.toArray(new EventReceiver[0]);

        Map<Class<? extends Annotation>, List<EventReceiver>> eventReceievers = new LinkedHashMap<>();
        for (EventReceiver receiver : receivers) {
            Method method = receiver.getMethod();
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation.annotationType().getAnnotation(UseConstraintHandler.class) == null)
                    continue;

                List<EventReceiver> list = eventReceievers.get(annotation.annotationType());
                if (list == null) {
                    eventReceievers.put(annotation.annotationType(), list = new ArrayList<EventReceiver>());
                }
                list.add(receiver);
            }
        }

        List<EventConstraintHandler> handlers = new ArrayList<>();

        for (Map.Entry<Class<? extends Annotation>, List<EventReceiver>> eventReceiver : eventReceievers.entrySet()) {
            Class<? extends EventConstraintHandler> handlerType = eventReceiver.getKey()
                    .getAnnotation(UseConstraintHandler.class).value();
            try {
                Constructor<? extends EventConstraintHandler> constructor =
                        handlerType.getConstructor(Engine.class, Iterable.class);
                constructor.setAccessible(true);
                handlers.add(constructor.newInstance(engine,
                        Collections.unmodifiableCollection(eventReceiver.getValue())));
            } catch (InstantiationException ex) {
                throw new IllegalArgumentException(handlerType.getName()
                        + " is not instantiable");
            } catch (IllegalAccessException ex) {
                throw new AssertionError("This should not happen as the"
                        + " constructor has been marked as accessible", ex);
            } catch (InvocationTargetException ex) {
                throw Internal.sneakyThrow(ex.getCause());
            } catch (NoSuchMethodException ex) {
                throw new IllegalArgumentException(handlerType.getName()
                        + " must have a constructor accepting Engine and EventReceiver[]");
            }
        }

        this.eventReceivers = receieversAsArray;
        this.constraintHandlers = handlers.toArray(new EventConstraintHandler[0]);
    }

    public void reset() {
    }

    public void dispatchEvent(Event event) {
        Object[] array = arrayPool.obtain();
        array[0] = event;

        Mask receivers = engine.maskPool.obtain().set(filledMask);
        for (EventConstraintHandler constraintHandler : constraintHandlers) {
            constraintHandler.filter(event, receivers);
        }

        try {
            for (int i = receivers.nextSetBit(0); i != -1; i = receivers.nextSetBit(i + 1)) {
                EventReceiver eventReceiver = eventReceivers[i];
                eventReceiver.getMethod().invoke(eventReceiver.getOwner(), array);
            }
        } catch (IllegalAccessException ex) {
            // Accuse the user of having changed the accessible flag
            throw new IllegalStateException("Handler method is not marked as accessible", ex);
        } catch (InvocationTargetException ex) {
            throw Internal.sneakyThrow(ex.getCause());
        }

        arrayPool.free(array);
    }
}
