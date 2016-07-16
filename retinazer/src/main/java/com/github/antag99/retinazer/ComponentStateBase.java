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

abstract class ComponentStateBase<T extends Component> implements ComponentState<T> {
    private Class<T> type;

    public ComponentStateBase(Class<T> type) {
        this.type = type;
    }

    abstract void clear();

    abstract ComponentStateBase<T> shallowCopy();

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public final void copyFrom(ComponentState<T> other) {
        copyFrom(other, DEFAULT_COPY_HANDLER);
    }

    @Override
    public abstract void copyFrom(ComponentState<T> other, CopyHandler handler);

    @Override
    public final ComponentState<T> copy() {
        return copy(DEFAULT_COPY_HANDLER);
    }

    @Override
    public abstract ComponentState<T> copy(CopyHandler handler);
}
