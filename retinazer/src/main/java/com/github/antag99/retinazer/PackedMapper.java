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

import com.github.antag99.retinazer.util.Experimental;
import com.github.antag99.retinazer.util.Property;

/**
 * Mapper implementation for packed components.
 *
 * @param <T> the component type
 */
@Experimental
public abstract class PackedMapper<T extends Component> extends Mapper<T> {

    // Note: Weaver-generated subclasses omit the 'type' parameter.
    public PackedMapper(Engine engine, Class<T> type, int typeIndex) {
        super(engine, type, typeIndex);
    }

    /**
     * Gets the properties of the packed component type.
     *
     * @return the properties of the packed component type.
     */
    public abstract Property<?, ?>[] getProperties();

    @Override
    protected ComponentStateBase<T> createState() {
        return new PackedComponentStateBase<T>(this, getProperties(), componentsMask) {
            @Override
            public void copyFrom(ComponentState<T> state, CopyHandler handler) {
                if (engine.update) {
                    throw new IllegalStateException("Cannot use mapper.getState()"
                            + ".copyFrom(...) inside of engine.update()");
                }

                super.copyFrom(state, handler);

                removeMask.clear();
                removeQueueMask.clear();

                engine.dirty = true;
                engine.flush();
            }
        };
    }
}
