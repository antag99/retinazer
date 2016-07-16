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

import com.github.antag99.retinazer.util.Bag;
import com.github.antag99.retinazer.util.Mask;

class PlainComponentStateBase<T extends Component> extends ComponentStateBase<T> {
    private PlainMapper<T> mapper;
    private Bag<T> components;
    private Mask componentsMask;

    PlainComponentStateBase(PlainMapper<T> mapper, Bag<T> components, Mask componentsMask) {
        super(mapper.type);

        this.mapper = mapper;
        this.components = new Bag<T>();
        this.componentsMask = new Mask();
    }

    @Override
    ComponentStateBase<T> shallowCopy() {
        return new PlainComponentStateBase<T>(mapper, new Bag<T>(), new Mask());
    }

    @Override
    void clear() {
        components.clear();
        componentsMask.clear();
    }

    @Override
    public void copyFrom(ComponentState<T> state, CopyHandler handler) {
        if (!(state instanceof PlainComponentStateBase) || ((PlainComponentStateBase<T>) state).mapper != mapper)
            throw new IllegalArgumentException();
        PlainComponentStateBase<T> otherState = (PlainComponentStateBase<T>) state;
        handler.copy(otherState.components, components);
        componentsMask.copyFrom(otherState.componentsMask);
    }

    @Override
    public ComponentState<T> copy(CopyHandler handler) {
        ComponentState<T> state = shallowCopy();
        state.copyFrom(this, handler);
        return state;
    }
}
