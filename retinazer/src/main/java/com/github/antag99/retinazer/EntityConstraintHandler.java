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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.antag99.retinazer.Event.WithEntities;
import com.github.antag99.retinazer.Event.WithEntity;
import com.github.antag99.retinazer.utils.Bag;
import com.github.antag99.retinazer.utils.Mask;

final class EntityConstraintHandler extends EventConstraintHandler {
    private Map<Class<? extends Event>, EventCache> eventCaches = new HashMap<>();
    private Mask relevantReceivers = new Mask();

    private static final class GetterCache {
        Method getter;
        Bag<Mask> byFamily;

        GetterCache(Method getter) {
            this.getter = getter;
            this.byFamily = new Bag<>();
        }

        Mask forFamily(int index) {
            Mask mask = byFamily.get(index);
            if (mask == null) {
                byFamily.set(index, mask = new Mask());
            }
            return mask;
        }
    }

    private static final class EventCache {
        GetterCache[] getters;
        Map<String, GetterCache> gettersByProperty;

        EventCache(GetterCache[] getters, Map<String, GetterCache> gettersByProperty) {
            this.getters = getters;
            this.gettersByProperty = gettersByProperty;
        }
    }

    public EntityConstraintHandler(Engine engine, Iterable<EventReceiver> receivers) {
        super(engine, receivers);

        // Construct caches for each event type for fast lookup
        for (Class<? extends Event> eventType : engine.getEventTypes()) {
            // No need to cache getters for abstract classes
            if (Modifier.isAbstract(eventType.getModifiers()))
                continue;

            List<GetterCache> getterList = new ArrayList<>();
            Map<String, GetterCache> getterMap = new HashMap<>();

            // Find getters returning entities in class hierarchy
            Class<?> current = eventType;
            while (current != null) {
                for (Method method : current.getMethods()) {
                    method.setAccessible(true);

                    if (method.getReturnType() == Entity.class &&
                            method.getName().matches("get[A-Z].*")) {
                        GetterCache getterCache = new GetterCache(method);

                        // Translate to property name
                        String property = Character.toLowerCase(
                                method.getName().charAt(3))
                                + method.getName().substring(4);

                        getterList.add(getterCache);
                        getterMap.put(property, getterCache);
                    }
                }

                current = current.getSuperclass();
            }
            eventCaches.put(eventType, new EventCache(
                    getterList.toArray(new GetterCache[0]), getterMap));
        }

        // Add indices of event receivers to the caches
        for (EventReceiver receiver : receivers) {
            // WithEntities store the WithEntity annotations
            WithEntities withEntities = receiver.getMethod()
                    .getAnnotation(WithEntities.class);

            // Event handlers take the event as only parameter
            Class<? extends Event> eventType = receiver.getMethod()
                    .getParameterTypes()[0].asSubclass(Event.class);

            for (WithEntity withEntity : withEntities.value()) {
                String property = withEntity.name();
                int index = getEngine().getMatcher(Family.with(withEntity.with())
                        .exclude(withEntity.exclude())).index;
                for (Entry<Class<? extends Event>, EventCache> entry : eventCaches.entrySet()) {
                    if (eventType.isAssignableFrom(entry.getKey())) {
                        EventCache eventCache = entry.getValue();
                        GetterCache getterCache = eventCache.gettersByProperty.get(property);
                        if (getterCache == null) {
                            throw new IllegalArgumentException("No such entity: "
                                    + property + " (of class " + eventType.getName() + ")");
                        }
                        getterCache.forFamily(index).set(receiver.getIndex());
                    }
                }
            }

            // Guard against dumb users that don't specify any WithEntity
            if (withEntities.value().length > 0) {
                relevantReceivers.set(receiver.getIndex());
            }
        }
    }

    @Override
    public void filter(Event event, Mask receivers) {
        EventCache eventCache = eventCaches.get(event.getClass());
        if (eventCache == null) {
            throw new IllegalArgumentException("Event type has not been "
                    + "registered: " + event.getClass().getName());
        }
        Mask mask = getEngine().maskPool.obtain();
        try {
            for (GetterCache getterCache : eventCache.getters) {
                Entity entity = (Entity) getterCache.getter.invoke(event);
                for (int i = entity.families.nextSetBit(0); i != -1; i = entity.families.nextSetBit(i + 1)) {
                    mask.or(getterCache.forFamily(i));
                }
            }
        } catch (IllegalAccessException ex) {
            throw new AssertionError("This should not happen as all getters "
                    + "have been marked as accessible", ex);
        } catch (InvocationTargetException ex) {
            Internal.sneakyThrow(ex.getCause());
        }
        receivers.andNot(relevantReceivers);
        receivers.or(mask);
        getEngine().maskPool.free(mask);
    }
}
