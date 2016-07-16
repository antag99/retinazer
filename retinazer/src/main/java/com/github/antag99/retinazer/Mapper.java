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

    /**
     * Gets the {@code ComponentState} for this {@code Mapper}.
     *
     * @return the {@code ComponentState} for this mapper.
     */
    @SuppressWarnings("unchecked")
    public final ComponentState<T> getState() {
        return (ComponentState<T>) engine.state.componentState.get(typeIndex);
    }

    /**
     * Creates a {@code ComponentStateBase} for this {@code Mapper}.
     *
     * @return the {@code ComponentStateBase}.
     */
    protected abstract ComponentStateBase<T> createState();
}
