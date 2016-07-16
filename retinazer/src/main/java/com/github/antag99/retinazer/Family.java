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

import com.github.antag99.retinazer.util.Mask;

public final class Family {
    final Engine engine;
    final int[] components;
    final int[] excludedComponents;
    final int index;
    final EntitySet entities = new EntitySet();

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
