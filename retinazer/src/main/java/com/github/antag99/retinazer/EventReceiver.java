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

import java.lang.reflect.Method;
import java.util.Objects;

import com.github.antag99.retinazer.utils.Experimental;

@Experimental
public final class EventReceiver {
    private final EntitySystem owner;
    private final Method method;
    private final Class<? extends Event> type;
    private final EventHandler handler;
    int index;

    EventReceiver(EntitySystem owner, Method method) {
        this.owner = Objects.requireNonNull(owner, "owner must not be null");
        this.method = Objects.requireNonNull(method, "method must not be null");
        this.type = method.getParameterTypes()[0].asSubclass(Event.class);
        this.handler = method.getAnnotation(EventHandler.class);

        if (index < 0) {
            throw new IllegalArgumentException("index must be >= 0: " + index);
        }

        if (!method.getDeclaringClass().isAssignableFrom(owner.getClass())) {
            throw new IllegalArgumentException(method.getName() + " belongs to "
                    + method.getDeclaringClass().getName() + ", not "
                    + owner.getClass().getName());
        }

        if (this.handler == null) {
            throw new IllegalArgumentException(method.getName() + " (of class "
                    + method.getDeclaringClass().getName()
                    + ") lacks the EventHandler annotation");
        }
    }

    public Class<? extends Event> getType() {
        return type;
    }

    public EntitySystem getOwner() {
        return owner;
    }

    public Method getMethod() {
        return method;
    }

    public EventHandler getHandler() {
        return handler;
    }

    public int getIndex() {
        return index;
    }
}
