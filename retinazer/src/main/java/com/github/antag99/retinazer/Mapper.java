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

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.antag99.retinazer.utils.Bag;
import com.github.antag99.retinazer.utils.Mask;

/**
 * {@code Mapper} is used for accessing the components of a specific type. This
 * offers better performance than looking up the type of a component on the fly.
 *
 * @param <T> the component type.
 */
public final class Mapper<T extends Component> {
    Engine engine;
    Class<T> type;
    int typeIndex;
    Constructor constructor;
    Bag<T> components = new Bag<T>();
    Mask componentsMask = new Mask();
    IntArray removeComponents = new IntArray();
    Mask removeComponentsMask = new Mask();
    IntArray insertComponents = new IntArray();
    Mask insertComponentsMask = new Mask();

    Mapper(Engine engine, Class<T> type, int typeIndex) {
        this.engine = engine;
        this.type = type;
        this.typeIndex = typeIndex;
        try {
            this.constructor = ClassReflection.getConstructor(type);
            this.constructor.setAccessible(true);
        } catch (ReflectionException ex) {
            if (ex.getCause() instanceof RuntimeException)
                throw (RuntimeException) ex.getCause();
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
            throw new IllegalArgumentException("Component type " + type.getName()
                    + " does not expose a zero-argument constructor");
        }

        try {
            @SuppressWarnings("unchecked")
            T instance = (T) constructor.newInstance();
            add(entity, instance);
            return instance;
        } catch (ReflectionException ex) {
            // GWT compatibility hack - no InvocationTargetException emulation
            if ("java.lang.reflect.InvocationTargetException".equals(
                    ex.getCause().getClass().getName()))
                throw Internal.sneakyThrow(ex.getCause().getCause());
            throw new AssertionError(ex);
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
        insertComponents.add(entity);
        insertComponentsMask.set(entity);
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

        if (removeComponentsMask.get(entity)) {
            return;
        }

        engine.dirty = true;
        removeComponents.add(entity);
        removeComponentsMask.set(entity);
    }

    /**
     * Retrieves a component of the type handled by this mapper. Returns {@code null}
     * if the specified entity does not have a component of the type.
     *
     * @param handle
     *            handle pointing to the entity.
     * @return the component; may be {@code null}.
     */
    public T get(Handle handle) {
        return get(handle.getEntity());
    }

    /**
     * Checks whether the specified entity has a component of the type handled by
     * this mapper.
     *
     * @param handle
     *            handle pointing to the entity.
     * @return whether the entity has the component of the type handled by this mapper.
     */
    public boolean has(Handle handle) {
        return has(handle.getEntity());
    }

    /**
     * Creates a component of the type handled by this mapper for the given entity.
     *
     * @param handle
     *            handle pointing to the entity.
     * @return the created component.
     */
    public T create(Handle handle) {
        return create(handle.getEntity());
    }

    /**
     * Adds a component of the type represented by this mapper. The operation
     * will take effect immediately, but notifications will be delayed until
     * the next call to {@link Engine#flush()}. Note that it is <b>not</b>
     * permitted to replace an existing component; {@link #remove(Handle)} must
     * be called first (and bear in mind that removals are delayed).
     *
     * @param handle
     *            handle pointing to the entity.
     * @param instance
     *            the component instance.
     */
    public void add(Handle handle, T instance) {
        add(handle.getEntity(), instance);
    }

    /**
     * Removes a component of the type represented by this mapper. Calling this
     * method multiple times does not result in an exception, neither does
     * attempting to remove a component that does not exist. Removal operations
     * will be delayed until the next call to {@link Engine#flush()}.
     *
     * @param handle
     *            handle pointing to the entity.
     */
    public void remove(Handle handle) {
        remove(handle.getEntity());
    }

}
