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

import com.github.antag99.retinazer.ComponentState.CopyHandler;
import com.github.antag99.retinazer.util.Experimental;

/**
 * State for an {@link Engine}.
 */
@Experimental
public interface EngineState {

    /**
     * Gets a copy of this {@code EngineState}.
     *
     * @return
     *         a copy of this state.
     */
    EngineState copy();

    /**
     * Gets a copy of this {@code EngineState}.
     *
     * @param handler
     *            {@link CopyHandler} to use for copying component data.
     * @return
     *         a copy of this state.
     */
    EngineState copy(CopyHandler handler);

    /**
     * Sets this {@code EngineState} to the other {@code EngineState}.
     *
     * @param other
     *            the other state to copy from.
     */
    void copyFrom(EngineState other);

    /**
     * Sets this {@code EngineState} to the other {@code EngineState}.
     *
     * @param other
     *            the other state to copy from.
     * @param handler
     *            {@link CopyHandler} to use for copying component data.
     */
    void copyFrom(EngineState other, CopyHandler handler);

    /**
     * Gets the component state for the given component type.
     *
     * @param componentType
     *            the component type.
     * @return the component state.
     */
    <T extends Component> ComponentState<T> getComponentState(Class<T> componentType);

    /**
     * Gets the component states for this engine state.
     *
     * @return the component states for this engine state..
     */
    ComponentState<?>[] getComponentStates();
}
