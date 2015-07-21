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

import static java.util.Collections.unmodifiableSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Immutable copy-on-write family configuration.
 */
public final class FamilyConfig {
    private final Set<Class<? extends Component>> components = new LinkedHashSet<Class<? extends Component>>();
    private final Set<Class<? extends Component>> componentsView = unmodifiableSet(components);
    private final Set<Class<? extends Component>> excludedComponents = new LinkedHashSet<Class<? extends Component>>();
    private final Set<Class<? extends Component>> excludedComponentsView = unmodifiableSet(excludedComponents);

    FamilyConfig() {
    }

    @SafeVarargs
    public final FamilyConfig with(Class<? extends Component>... componentTypes) {
        FamilyConfig config = clone();
        for (Class<? extends Component> componentType : componentTypes) {
            config.excludedComponents.remove(componentType);
            config.components.add(componentType);
        }
        return config;
    }

    @SafeVarargs
    public final FamilyConfig exclude(Class<? extends Component>... componentTypes) {
        FamilyConfig config = clone();
        for (Class<? extends Component> componentType : componentTypes) {
            config.components.remove(componentType);
            config.excludedComponents.add(componentType);
        }
        return config;
    }

    @Override
    protected FamilyConfig clone() {
        FamilyConfig config = new FamilyConfig();
        config.components.addAll(components);
        config.excludedComponents.addAll(excludedComponents);
        return config;
    }

    Set<Class<? extends Component>> getComponents() {
        return componentsView;
    }

    Set<Class<? extends Component>> getExcludedComponents() {
        return excludedComponentsView;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FamilyConfig))
            return false;
        FamilyConfig config = (FamilyConfig) obj;
        return components.equals(config.components) &&
                excludedComponents.equals(config.excludedComponents);
    }

    @Override
    public int hashCode() {
        return components.hashCode() * 31 + excludedComponents.hashCode();
    }
}
