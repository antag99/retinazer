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

import com.github.antag99.retinazer.util.Mask;

public final class Family {
    final Engine engine;
    final int[] components;
    final int[] excludedComponents;
    final int index;
    final EntitySet entities = new EntitySet();
    EntityListener[] listeners = new EntityListener[0];

    Mask removeEntities = new Mask();
    Mask insertEntities = new Mask();

    Family(Engine engine,
            int[] components,
            int[] excludedComponents,
            int index) {
        this.engine = engine;
        this.components = components;
        this.excludedComponents = excludedComponents;
        this.index = index;
    }

    /**
     * Adds a listener to this entity set.
     *
     * @param listener The listener to add.
     */
    public void addListener(EntityListener listener) {
        int n = listeners.length;
        for (int i = 0; i < n; i++) {
            if (listeners[i] == listener) {
                EntityListener[] newListeners = new EntityListener[n];
                System.arraycopy(listeners, 0, newListeners, 1, i);
                System.arraycopy(listeners, i + 1, newListeners, i, n - i - 1);
                newListeners[0] = listener;
                this.listeners = newListeners;
                return;
            }
        }
        EntityListener[] newListeners = new EntityListener[n + 1];
        System.arraycopy(listeners, 0, newListeners, 1, n);
        newListeners[0] = listener;
        this.listeners = newListeners;
    }

    /**
     * Removes a listener from this entity set.
     *
     * @param listener The listener to remove.
     */
    public void removeListener(EntityListener listener) {
        for (int i = 0, n = listeners.length; i < n; i++) {
            if (listeners[i] == listener) {
                EntityListener[] newListeners = new EntityListener[listeners.length - 1];
                System.arraycopy(listeners, 0, newListeners, 0, i);
                System.arraycopy(listeners, i + 1, newListeners, i, listeners.length - i - 1);
                this.listeners = newListeners;
                return;
            }
        }
    }

    public EntitySet getEntities() {
        return entities.view();
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    public static final FamilyConfig create() {
        return new FamilyConfig();
    }

    @SafeVarargs
    public static final FamilyConfig with(Class<? extends Component>... componentTypes) {
        return new FamilyConfig().with(componentTypes);
    }

    @SafeVarargs
    public static final FamilyConfig exclude(Class<? extends Component>... componentTypes) {
        return new FamilyConfig().exclude(componentTypes);
    }
}
