/*******************************************************************************
 * Retinazer, an entity-component-system framework for Java
 *
 * Copyright (C) 2015-2016 Anton Gustafsson
 *
 * This file is part of Retinazer.
 *
 * Retinazer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Retinazer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Retinazer.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.antag99.retinazer;

import java.util.HashSet;
import java.util.Set;

public final class FamilyConfig {
    Set<Class<? extends Component>> components = new HashSet<>();
    Set<Class<? extends Component>> excludedComponents = new HashSet<>();

    public FamilyConfig() {
    }

    @SafeVarargs
    public final FamilyConfig with(Class<? extends Component>... componentTypes) {
        Set<Class<? extends Component>> newComponents = new HashSet<>();
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
        Set<Class<? extends Component>> newExcludedComponents = new HashSet<>();
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
