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
package com.github.antag99.retinazer.util;

import static java.util.Objects.requireNonNull;

/**
 *
 */
@Experimental
// This class is auto-generated; do not modify! @off
public final class IntProperty implements Property<Integer, IntBag> {
    private String name;
    private IntBag bag;

    public IntProperty(String name) {
        this.name = requireNonNull(name, "name must not be null");
        this.bag = new IntBag();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public IntBag getBag() {
        return bag;
    }
}
