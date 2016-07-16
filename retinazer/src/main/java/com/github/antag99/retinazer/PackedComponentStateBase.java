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

import java.lang.reflect.InvocationTargetException;

import com.github.antag99.retinazer.util.Mask;
import com.github.antag99.retinazer.util.ObjectProperty;
import com.github.antag99.retinazer.util.Property;

class PackedComponentStateBase<T extends Component> extends ComponentStateBase<T> {
    private PackedMapper<T> mapper;
    private Property<?, ?>[] properties;
    private Mask componentsMask;

    PackedComponentStateBase(PackedMapper<T> mapper, Property<?, ?>[] properties, Mask componentsMask) {
        super(mapper.type);

        this.mapper = mapper;
        this.properties = properties;
        this.componentsMask = componentsMask;
    }

    @Override
    ComponentStateBase<T> shallowCopy() {
        // Copy property definitions, without copying the state
        Property<?, ?>[] properties = mapper.getProperties();
        Property<?, ?>[] copyProperties = new Property[properties.length];
        for (int i = 0; i < copyProperties.length; i++) {
            Property<?, ?> property = properties[i];
            if (property instanceof ObjectProperty) {
                property = new ObjectProperty<>(property.getName(), property.getType());
            } else {
                try {
                    property = property.getClass().getConstructor(String.class)
                            .newInstance(property.getName());
                } catch (InstantiationException | IllegalAccessException | /**/
                        InvocationTargetException | NoSuchMethodException ex) {
                    throw new AssertionError(ex);
                }
            }
            copyProperties[i] = property;
        }
        return new PackedComponentStateBase<>(mapper, copyProperties, new Mask());
    }

    @Override
    void clear() {
        for (Property<?, ?> property : properties)
            property.getBag().clear();
        componentsMask.clear();
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void copyFrom(ComponentState<T> state, CopyHandler handler) {
        if (!(state instanceof PackedComponentStateBase) || ((PackedComponentStateBase) state).mapper != mapper)
            throw new IllegalArgumentException();

        PackedComponentStateBase<T> otherState = (PackedComponentStateBase<T>) state;
        Property<?, ?>[] otherProperties = otherState.properties;

        for (int i = 0; i < properties.length; i++) {
            Property property = properties[i];
            Property otherProperty = otherProperties[i];
            handler.copy(otherProperty.getBag(), property.getBag());
        }

        componentsMask.copyFrom(otherState.componentsMask);
    }

    @Override
    public ComponentState<T> copy(CopyHandler handler) {
        ComponentState<T> state = shallowCopy();
        state.copyFrom(this, handler);
        return state;
    }
}
