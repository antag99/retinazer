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

import com.github.antag99.retinazer.utils.Mask;

public final class Family {
    public static final FamilyConfig EMPTY = new FamilyConfig();

    final Mask components;
    final Mask excludedComponents;
    final Class<? extends Component>[] componentsArray;
    final Class<? extends Component>[] excludedComponentsArray;
    final int index;

    Family(
            Mask components,
            Mask excludedComponents,
            Class<? extends Component>[] componentsArray,
            Class<? extends Component>[] excludedComponentsArray,
            int index) {
        this.components = components;
        this.excludedComponents = excludedComponents;
        this.componentsArray = componentsArray;
        this.excludedComponentsArray = excludedComponentsArray;
        this.index = index;
    }

    public boolean matches(Entity entity) {
        if (!components.isSubsetOf(entity.components)) {
            return false;
        }

        if (excludedComponents.intersects(entity.components)) {
            return false;
        }

        return true;
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
        return EMPTY.with(componentTypes);
    }

    @SafeVarargs
    public static final FamilyConfig exclude(Class<? extends Component>... componentTypes) {
        return EMPTY.exclude(componentTypes);
    }
}
