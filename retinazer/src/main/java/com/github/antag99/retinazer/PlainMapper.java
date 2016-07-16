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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.github.antag99.retinazer.util.Bag;
import com.github.antag99.retinazer.util.Experimental;
import com.github.antag99.retinazer.util.Mask;

@Experimental
public final class PlainMapper<T extends Component> extends Mapper<T> {

    /** No-arg constructor for the component */
    final Constructor<T> constructor;

    /** Stores components */
    final Bag<T> components = new Bag<T>();

    PlainMapper(Engine engine, Class<T> type, int typeIndex) {
        super(engine, type, typeIndex);

        try {
            constructor = type.getConstructor();
            constructor.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            throw new RetinazerException("Component type " + type.getName()
                    + " does not expose a zero-argument constructor");
        }
    }

    @Override
    public T create(int entity) {
        checkCreate(entity);
        T instance;
        try {
            instance = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new AssertionError(ex);
        } catch (InvocationTargetException ex) {
            throw Internal.sneakyThrow(ex.getCause());
        }
        components.set(entity, instance);
        return instance;
    }

    @Override
    public T get(int entity) {
        return components.get(entity);
    }

    @Override
    protected void applyComponentChanges() {
        Bag<T> components = this.components;
        Mask componentsMask = this.componentsMask;
        Mask removeMask = this.removeMask;
        components.clear(removeMask);
        componentsMask.andNot(removeMask);
    }

    @Override
    protected ComponentStateBase<T> createState() {
        return new PlainComponentStateBase<T>(this, components, componentsMask) {
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
