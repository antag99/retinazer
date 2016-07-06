/*******************************************************************************
 * Copyright (C) 2015 Anton Gustafsson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.github.antag99.retinazer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.github.antag99.retinazer.util.Bag;
import com.github.antag99.retinazer.util.Mask;

/**
 * {@code Mapper} is used for accessing the components of a specific type. This
 * offers better performance than looking up the type of a component on the fly.
 *
 * @param <T> the component type.
 */
public final class Mapper<T extends Component> {
    /** The engine instance this mapper is tied to */
    Engine engine;
    /** The component type */
    Class<T> type;
    /** Unique index for the component type */
    int typeIndex;
    /** Zero-arg constructor for the component */
    Constructor<T> constructor;

    /** Stores components */
    Bag<T> components = new Bag<T>();
    /** Mask of current components */
    Mask componentsMask = new Mask();

    /** Mask of components that will be removed */
    Mask remove = new Mask();
    /** Mask of components to be removed later */
    Mask removeQueue = new Mask();

    Mapper(Engine engine, Class<T> type, int typeIndex) {
        this.engine = engine;
        this.type = type;
        this.typeIndex = typeIndex;
        try {
            this.constructor = type.getConstructor();
            this.constructor.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            this.constructor = null;
        }
    }

    /**
     * Retrieves a component of the type handled by this mapper. Returns {@code null}
     * if the specified entity does not have a component of the type.
     *
     * @param entity
     *            the index of the entity.
     * @return the component; may be {@code null}.
     */
    public T get(int entity) {
        return components.get(entity);
    }

    /**
     * Checks whether the specified entity has a component of the type handled by
     * this mapper.
     *
     * @param entity
     *            the index of the entity.
     * @return whether the entity has the component of the type handled by this mapper.
     */
    public boolean has(int entity) {
        return components.get(entity) != null;
    }

    /**
     * Creates a component of the type handled by this mapper for the given entity.
     *
     * @param entity
     *            the index of the entity.
     * @return the created component.
     */
    public T create(int entity) {
        if (constructor == null) {
            throw new RetinazerException("Component type " + type.getName()
                    + " does not expose a zero-argument constructor");
        }

        try {
            T instance = constructor.newInstance();
            add(entity, instance);
            return instance;
        } catch (InstantiationException ex) {
            throw new AssertionError(ex);
        } catch (IllegalAccessException ex) {
            throw new AssertionError(ex);
        } catch (InvocationTargetException ex) {
            throw Internal.sneakyThrow(ex.getCause());
        }
    }

    /**
     * Adds a component of the type represented by this mapper. The operation
     * will take effect immediately, but notifications will be delayed until
     * the next call to {@link Engine#flush()}. Note that it is <b>not</b>
     * permitted to replace an existing component; {@link #remove(int)} must
     * be called first (and bear in mind that removals are delayed).
     *
     * @param entity
     *            the index of the entity.
     * @param instance
     *            the component instance.
     */
    public void add(int entity, T instance) {
        if (has(entity)) {
            throw new IllegalArgumentException("Cannot insert a component that "
                    + "already exists: " + instance.getClass().getName());
        }

        engine.dirty = true;
        components.set(entity, instance);
        componentsMask.set(entity);
    }

    /**
     * Removes a component of the type represented by this mapper. Calling this
     * method multiple times does not result in an exception, neither does
     * attempting to remove a component that does not exist. Removal operations
     * will be delayed until the next call to {@link Engine#flush()}.
     *
     * @param entity
     *            the index of the entity.
     */
    public void remove(int entity) {
        if (!has(entity)) {
            return;
        }

        if (removeQueue.get(entity)) {
            return;
        }

        engine.dirty = true;
        removeQueue.set(entity);
    }

    void applyComponentChanges() {
        Bag<T> components = this.components;
        Mask remove = this.remove;
        for (int ii = remove.nextSetBit(0); ii != -1; ii = remove.nextSetBit(ii + 1)) {
            components.set(ii, null);
        }
        componentsMask.andNot(remove);
    }
}
