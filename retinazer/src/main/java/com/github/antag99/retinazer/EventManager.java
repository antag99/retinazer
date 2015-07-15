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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.antag99.retinazer.utils.Inject;
import com.github.antag99.retinazer.utils.Mask;

final class EventManager extends EntitySystem {
    private @Inject Engine engine;
    private EventListenerData[] eventListeners = new EventListenerData[0];
    private Map<Class<? extends Event>, Mask> eventFilters = new HashMap<>();
    private Map<Family, Mask> familyFilters = new HashMap<>();

    public EventManager(EngineConfig config) {
        for (Class<? extends Event> eventType : config.getEventTypes()) {
            eventFilters.put(eventType, new Mask());
        }
    }

    private static class EventListenerData {
        public int priority;
        public EventListener<?> listener;
    }

    private static class EventHandlerListener implements EventListener<Event> {
        private Object owner;
        private Method method;

        public EventHandlerListener(Object owner, Method method) {
            this.owner = owner;
            this.method = method;
        }

        @Override
        public void handleEvent(Event event, Entity entity) {
            try {
                method.invoke(owner, event, entity);
            } catch (IllegalAccessException | IllegalArgumentException ex) {
                throw new AssertionError(ex);
            } catch (InvocationTargetException ex) {
                throw Internal.sneakyThrow(ex.getCause());
            }
        }
    }

    private Mask getFamilyFilter(Family family) {
        return familyFilters.computeIfAbsent(family, k -> new Mask());
    }

    public <T extends Event> void addEventListener(Class<T> eventClass, Family family, int priority,
            EventListener<? super T> listener) {
        int insertionIndex = 0;
        while (insertionIndex < eventListeners.length) {
            if (eventListeners[insertionIndex].priority <= priority) {
                insertionIndex++;
            } else {
                break;
            }
        }

        // Allocate space for the listener in the bitmasks, by shifting up.
        for (Mask familyFilter : familyFilters.values()) {
            for (int k = eventListeners.length - 1; k >= insertionIndex; --k) {
                if (familyFilter.get(k)) {
                    familyFilter.clear(k);
                    familyFilter.set(k + 1);
                }
            }
        }

        for (Mask eventFilter : eventFilters.values()) {
            for (int k = eventListeners.length - 1; k >= insertionIndex; --k) {
                if (eventFilter.get(k)) {
                    eventFilter.clear(k);
                    eventFilter.set(k + 1);
                }
            }
        }

        EventListenerData eventListenerData = new EventListenerData();
        eventListenerData.priority = priority;
        eventListenerData.listener = listener;
        EventListenerData[] newEventListeners = new EventListenerData[eventListeners.length + 1];
        System.arraycopy(eventListeners, 0, newEventListeners, 0, insertionIndex);
        System.arraycopy(eventListeners, insertionIndex,
                newEventListeners, insertionIndex + 1,
                eventListeners.length - insertionIndex);
        newEventListeners[insertionIndex] = eventListenerData;
        eventListeners = newEventListeners;

        getEventListeners(eventClass).set(insertionIndex);
        getFamilyFilter(family).set(insertionIndex);

        for (Entry<Class<? extends Event>, Mask> eventFilter : eventFilters.entrySet()) {
            if (eventClass.isAssignableFrom(eventFilter.getKey())) {
                eventFilter.getValue().set(insertionIndex);
            }
        }
    }

    public void removeEventListener(EventListener<?> listener) {
        for (int i = 0, n = eventListeners.length; i < n; ++i) {
            if (eventListeners[i] == listener) {
                // Remove the listener from bitmasks, by shifting down.
                for (Mask familyFilter : familyFilters.values()) {
                    for (int k = i; k < eventListeners.length; --k) {
                        if (familyFilter.get(k + 1)) {
                            familyFilter.clear(k + 1);
                            familyFilter.set(k);
                        } else {
                            familyFilter.clear(k);
                        }
                    }
                }

                for (Mask eventFilter : eventFilters.values()) {
                    eventFilter.clear(i);
                    for (int k = i; k < eventListeners.length; --k) {
                        if (eventFilter.get(k + 1)) {
                            eventFilter.clear(k + 1);
                            eventFilter.set(k);
                        } else {
                            eventFilter.clear(k);
                        }
                    }
                }

                EventListenerData[] newEventListeners = new EventListenerData[eventListeners.length - 1];
                System.arraycopy(eventListeners, 0, newEventListeners, 0, i);
                System.arraycopy(eventListeners, i + 1, newEventListeners, i, eventListeners.length - i - 1);
                eventListeners = newEventListeners;
            }
        }
    }

    private Mask getEventListeners(Class<? extends Event> eventClass) {
        Mask eventListeners = eventFilters.get(eventClass);
        if (eventListeners == null) {
            throw new IllegalArgumentException(
                    "Event type " + eventClass.getName() + " has not been registered");
        }
        return eventListeners;
    }

    @SuppressWarnings("unchecked")
    public void dispatchEvent(Event event, Entity entity) {
        Mask bits = new Mask();
        for (Entry<Family, Mask> familyFilter : familyFilters.entrySet()) {
            if (familyFilter.getKey().matches(entity)) {
                bits.or(familyFilter.getValue());
            }
        }
        bits.and(getEventListeners(event.getClass()));

        EventListenerData[] items = this.eventListeners;
        for (int i = bits.nextSetBit(0); i != -1; i = bits.nextSetBit(i + 1)) {
            ((EventListener<Event>) items[i].listener).handleEvent(event, entity);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void registerEventHandlers() {
        for (EntitySystem system : engine.getSystems()) {
            Class<? extends EntitySystem> systemClass = system.getClass();
            for (Method method : Internal.getAllMethods(systemClass)) {
                EventHandler handler = method.getAnnotation(EventHandler.class);
                if (handler != null) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 2)
                        throw new IllegalArgumentException("Invalid signature for @EventHandler " + method.getName());
                    if (!Event.class.isAssignableFrom(parameterTypes[0]))
                        throw new IllegalArgumentException("Invalid signature for @EventHandler " + method.getName());
                    if (parameterTypes[1] != Entity.class)
                        throw new IllegalArgumentException("Invalid signature for @EventHandler " + method.getName());
                    Class eventClass = parameterTypes[0];
                    FamilyConfig familyConfig = Family.with(handler.value()).exclude(handler.exclude());
                    addEventListener(eventClass, engine.getFamily(familyConfig), handler.priority(),
                            new EventHandlerListener(system, method));
                }
            }
        }
    }

    public void reset() {
        eventListeners = new EventListenerData[0];
        familyFilters.clear();
        eventFilters.values().forEach(Mask::clear);
    }
}
