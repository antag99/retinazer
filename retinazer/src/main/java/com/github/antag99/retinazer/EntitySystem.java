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

/**
 * Base class for system implementations.
 */
@Wire
public abstract class EntitySystem {

    /**
     * Engine instance this entity system is added to, for convenience.
     */
    protected Engine engine;

    /**
     * Framework-side initialization method. End users should not override
     * this method. Always call {@code super.setup()} when overriding this.
     */
    protected void setup() {
    }

    /**
     * Initializes this system. If you override this method, mark it {@code final}.
     */
    protected void initialize() {
    }

    /**
     * Updates this system. If you override this method, mark it {@code final}.
     */
    protected void update() {
    }
}
