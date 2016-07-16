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

import java.lang.reflect.Field;

/**
 * WireResolver is used for wiring/un-wiring fields marked with {@link Wire}.
 * Multiple resolvers can be registered to an {@link EngineConfig}.
 *
 * @see Wire
 */
public interface WireResolver {

    /**
     * Wires the field of the given object.
     *
     * @param engine The engine instance.
     * @param object The object to wire.
     * @param field The field of the object.
     * @return Whether this resolver handled the given field.
     * @throws Throwable If an unexpected error occurred.
     */
    public boolean wire(Engine engine, Object object, Field field) throws Throwable;

    /**
     * Un-wires the field of the given object.
     *
     * @param engine The engine instance.
     * @param object The object to wire.
     * @param field The field of the object.
     * @return Whether this resolver handled the given field.
     * @throws Throwable If an unexpected error occurred.
     */
    public boolean unwire(Engine engine, Object object, Field field) throws Throwable;
}
