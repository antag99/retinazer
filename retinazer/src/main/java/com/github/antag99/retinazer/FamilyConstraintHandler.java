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

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;

import com.github.antag99.retinazer.Event.WithFamily;
import com.github.antag99.retinazer.utils.Bag;
import com.github.antag99.retinazer.utils.Mask;

final class FamilyConstraintHandler extends EventConstraintHandler {
    private static final Object[] ARRAY = new Object[0];
    private Map<Class<? extends Event>, EventCache> eventCaches = new HashMap<>();
    private Bag<Mask> handlersForFamily = new Bag<>();
    private Mask relevantReceivers = new Mask();

    private Mask forFamily(int index) {
        Mask mask = handlersForFamily.get(index);
        if (mask == null) {
            handlersForFamily.set(index, mask = new Mask());
        }
        return mask;
    }

    private static final class EventCache {
        Method withMethod;
        Method excludeMethod;

        EventCache(Method withMethod, Method excludeMethod) {
            this.withMethod = withMethod;
            this.excludeMethod = excludeMethod;
        }
    }

    public FamilyConstraintHandler(Engine engine, Iterable<EventReceiver> receivers) {
        super(engine, receivers);

        for (Class<? extends Event> eventType : engine.getEventTypes()) {
            /*
             * Find with() and exclude() methods returning Class<? extends Component>[]
             */
            Method withMethod = null;
            Method excludeMethod = null;

            Class<?> current = eventType;
            while (current != null && (withMethod == null || excludeMethod == null)) {
                for (Method method : current.getDeclaredMethods()) {
                    method.setAccessible(true);
                    if (method.getReturnType() == Class[].class &&
                            method.getParameterTypes().length == 0 &&
                            method.getGenericReturnType() instanceof GenericArrayType &&
                            ((GenericArrayType) method.getGenericReturnType())
                                    .getGenericComponentType() instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) ((GenericArrayType) method
                                .getGenericReturnType()).getGenericComponentType();
                        if (parameterizedType.getActualTypeArguments().length == 1 &&
                                parameterizedType.getActualTypeArguments()[0] instanceof WildcardType) {
                            WildcardType wildcardType = (WildcardType) parameterizedType.getActualTypeArguments()[0];
                            if (wildcardType.getLowerBounds().length != 0)
                                continue;
                            if (wildcardType.getUpperBounds().length != 1)
                                continue;
                            if (wildcardType.getUpperBounds()[0] != Component.class)
                                continue;
                            if (withMethod == null && method.getName().equals("with"))
                                withMethod = method;
                            if (excludeMethod == null && method.getName().equals("exclude"))
                                excludeMethod = method;
                        }
                    }
                }

                current = current.getSuperclass();
            }

            if (withMethod != null && excludeMethod != null) {
                eventCaches.put(eventType, new EventCache(withMethod, excludeMethod));
            }
        }

        for (EventReceiver receiver : receivers) {
            if (!eventCaches.containsKey(receiver.getType()))
                throw new IllegalArgumentException(receiver.getType().getName()
                        + " does not define both with() and exclude() methods");
            WithFamily withFamily = receiver.getMethod().getAnnotation(WithFamily.class);
            int index = engine.getFamily(Family.with(withFamily.with())
                    .exclude(withFamily.exclude())).index;
            forFamily(index).set(receiver.getIndex());
            relevantReceivers.set(receiver.getIndex());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void filter(Event event, Mask receivers) {
        EventCache eventCache = eventCaches.get(event.getClass());
        if (eventCache == null) {
            return;
        }

        try {
            Class<? extends Component>[] with = (Class<? extends Component>[]) eventCache.withMethod.invoke(event, ARRAY);
            Class<? extends Component>[] exclude = (Class<? extends Component>[]) eventCache.excludeMethod.invoke(event, ARRAY);

            // TODO: Optimize family retrieval; avoid creating sets and stuff
            int index = getEngine().getFamily(Family.with(with).exclude(exclude)).index;
            Mask mask = getEngine().maskPool.obtain();
            mask.or(receivers);
            mask.and(relevantReceivers);
            mask.and(forFamily(index));

            receivers.andNot(relevantReceivers);
            receivers.or(mask);

            getEngine().maskPool.free(mask);
        } catch (IllegalAccessException ex) {
            throw new AssertionError("This should not happen as the ");
        } catch (InvocationTargetException ex) {
            throw Internal.sneakyThrow(ex.getCause());
        }
    }
}
