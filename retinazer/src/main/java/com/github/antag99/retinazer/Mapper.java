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

import com.github.antag99.retinazer.util.Mask;

import static java.util.Objects.requireNonNull;

/**
 * Mappers are used for accessing and modifying components efficiently.
 *
 * @param <T> the component type.
 */
public abstract class Mapper<T extends Component> {
    /** The engine instance this mapper is tied to */
    protected final Engine engine;
    /** The component type */
    protected final Class<T> type;
    /** Unique index for the component type */
    protected final int typeIndex;
    /** Mask of current components */
    protected final Mask componentsMask = new Mask();
    /** Mask of components that will be removed */
    protected final Mask removeMask = new Mask();
    /** Mask of components to be removed later */
    protected final Mask removeQueueMask = new Mask();

    protected Mapper(Engine engine, Class<T> type, int typeIndex) {
        this.engine = requireNonNull(engine, "engine must not be null");
        this.type = requireNonNull(type, "type must not be null");
        this.typeIndex = typeIndex;
    }

    protected abstract void applyComponentChanges();

    protected final void checkCreate(int entity) {
        if (has(entity)) {
            throw new IllegalArgumentException("Cannot create a component that "
                    + "already exists: " + type.getName());
        }

        engine.dirty = true;
        componentsMask.set(entity);
    }

    /**
     * Creates a component of the type handled by this mapper for the given entity.
     *
     * @param entity
     *            the index of the entity.
     * @return the created component.
     */
    public abstract T create(int entity);

    /**
     * Retrieves a component of the type handled by this mapper. This method should
     * not be called on entities that do not have this component type.
     *
     * @param entity
     *            the index of the entity.
     * @return the component
     */
    public abstract T get(int entity);

    /**
     * Checks whether the specified entity has a component of the type handled by
     * this mapper.
     *
     * @param entity
     *            the index of the entity.
     * @return whether the entity has the component of the type handled by this mapper.
     */
    public final boolean has(int entity) {
        return componentsMask.get(entity);
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
    public final void remove(int entity) {
        if (!has(entity)) {
            return;
        }

        if (removeQueueMask.get(entity)) {
            return;
        }

        engine.dirty = true;
        removeQueueMask.set(entity);
    }
}
