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

import com.badlogic.gdx.utils.ObjectSet;

public final class FamilyConfig {
    ObjectSet<Class<? extends Component>> components = new ObjectSet<>();
    ObjectSet<Class<? extends Component>> excludedComponents = new ObjectSet<>();

    public FamilyConfig() {
    }

    @SafeVarargs
    public final FamilyConfig with(Class<? extends Component>... componentTypes) {
        ObjectSet<Class<? extends Component>> newComponents = new ObjectSet<>();
        newComponents.addAll(components);
        for (Class<? extends Component> componentType : componentTypes) {
            if (newComponents.contains(componentType))
                throw new IllegalArgumentException(componentType.getName());
            if (excludedComponents.contains(componentType))
                throw new IllegalArgumentException(componentType.getName());
            newComponents.add(componentType);
        }
        this.components = newComponents;
        return this;
    }

    @SafeVarargs
    public final FamilyConfig exclude(Class<? extends Component>... componentTypes) {
        ObjectSet<Class<? extends Component>> newExcludedComponents = new ObjectSet<>();
        newExcludedComponents.addAll(excludedComponents);
        for (Class<? extends Component> componentType : componentTypes) {
            if (newExcludedComponents.contains(componentType))
                throw new IllegalArgumentException(componentType.getName());
            if (components.contains(componentType))
                throw new IllegalArgumentException(componentType.getName());
            newExcludedComponents.add(componentType);
        }
        this.excludedComponents = newExcludedComponents;
        return this;
    }
}
