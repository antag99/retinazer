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

import com.github.antag99.retinazer.utils.Experimental;
import com.github.antag99.retinazer.utils.Mask;

public final class EntitySet {
    private static class Content {
        Engine engine;
        Mask entities = new Mask();
        int modCount = 0;
        EntitySetListener[] listeners = new EntitySetListener[0];

        Content(Engine engine) {
            this.engine = engine;
        }
    }

    private Content content;

    // Unmodifiable view of this entity set. If the value is *this* object,
    // indicates that this set may not be modified.
    private EntitySet view = null;

    private Entity[] entities = new Entity[0];
    private int entitiesModCount = 0;

    private int[] indices = new int[0];
    private int indicesModCount = 0;

    private EntitySet(EntitySet source) {
        this.content = source.content;
        this.view = this;
    }

    EntitySet(Engine engine) {
        this.content = new Content(engine);
    }

    /**
     * Returns an unmodifiable view of this entity set.
     *
     * @return Unmodifiable view of this entity set.
     */
    public EntitySet unmodifiable() {
        return view != null ? view : (view = new EntitySet(this));
    }

    /**
     * Adds a listener to this entity set.
     *
     * @param listener The listener to add.
     */
    public void addListener(EntitySetListener listener) {
        EntitySetListener[] listeners = content.listeners;
        for (int i = 0, n = listeners.length; i < n; i++) {
            if (listeners[i] == listener) {
                System.arraycopy(listeners, 0, listeners, 1, i);
                listeners[0] = listener;
                return;
            }
        }
        EntitySetListener[] newListeners = new EntitySetListener[listeners.length + 1];
        System.arraycopy(listeners, 0, newListeners, 1, listeners.length);
        newListeners[0] = listener;
        content.listeners = newListeners;
    }

    /**
     * Removes a listener from this entity set.
     *
     * @param listener The listener to remove.
     */
    public void removeListener(EntitySetListener listener) {
        EntitySetListener[] listeners = content.listeners;
        for (int i = 0, n = listeners.length; i < n; i++) {
            if (listeners[i] == listener) {
                EntitySetListener[] newListeners = new EntitySetListener[listeners.length - 1];
                System.arraycopy(listeners, 0, newListeners, 0, i);
                System.arraycopy(listeners, i + 1, newListeners, i, listeners.length - i);
                return;
            }
        }
    }

    /**
     * Adds the given entity to this set. Throws an exception if this set cannot be modified.
     *
     * @param entity The entity to add.
     */
    public void addEntity(Entity entity) {
        if (entity.getEngine() != content.engine) {
            throw new IllegalArgumentException("Cannot add an entity from another engine");
        }
        if (view == this) {
            throw new IllegalArgumentException("Cannot modify the entities of this set");
        }

        content.entities.set(entity.getIndex());
        content.modCount++;

        for (EntitySetListener listener : content.listeners) {
            listener.inserted(entity);
        }
    }

    /**
     * Removes the given entity from this set. Throws an exception if this set cannot be modified.
     *
     * @param entity The entity to remove.
     */
    public void removeEntity(Entity entity) {
        if (entity.getEngine() != content.engine) {
            throw new IllegalArgumentException("Cannot remove an entity from another engine");
        }
        if (view == this) {
            throw new IllegalArgumentException("Cannot modify the entities of this set");
        }

        content.entities.clear(entity.getIndex());
        content.modCount++;

        for (EntitySetListener listener : content.listeners) {
            listener.removed(entity);
        }
    }

    /**
     * Checks if this set contains the given entity.
     *
     * @param entity The entity to check for.
     * @return Whether this set contains the given entity.
     */
    public boolean contains(Entity entity) {
        if (entity.getEngine() != content.engine) {
            throw new IllegalArgumentException("Cannot query an entity from another engine");
        }
        return content.entities.get(entity.getIndex());
    }

    /**
     * <p>
     * Returns an array containing all entities in this set. Note that whenever
     * the entity set changes, this array must be reconstructed. For maximum
     * performance, {@link #getIndices()} should be used.
     * </p>
     *
     * <p>
     * <b>WARNING:</b> Modifying this array leads to undefined behavior.
     * </p>
     *
     * @return All entities in this set.
     */
    @Experimental
    public Entity[] getEntities() {
        if (entitiesModCount != content.modCount) {
            Engine engine = content.engine;
            Mask m = content.entities;
            entities = new Entity[m.cardinality()];
            for (int i = 0, b = m.nextSetBit(0), n = entities.length; i < n; i++, b = m.nextSetBit(b + 1)) {
                entities[i] = engine.getEntityForIndex(b);
            }
            entitiesModCount = content.modCount;
        }
        return entities;
    }

    /**
     * <p>
     * Returns an array containing the indices of all entities in this set.
     * Note that whenever the entity set changes, this array must be
     * reconstructed.
     * </p>
     *
     * <p>
     * <b>WARNING:</b> Modifying this array leads to undefined behavior.
     * </p>
     *
     * @return The indices of all entities in this set.
     */
    @Experimental
    public int[] getIndices() {
        if (indicesModCount != content.modCount) {
            indices = content.entities.indices();
            indicesModCount = content.modCount;
        }
        return indices;
    }
}
