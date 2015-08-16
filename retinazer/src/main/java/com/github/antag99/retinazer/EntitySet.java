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
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.github.antag99.retinazer.utils.Mask;

public final class EntitySet {
    private static Pool<IntArray> pool = Pools.get(IntArray.class);

    private static class Content {
        Mask entities = new Mask();
        int modCount = 0;
        EntitySetListener[] listeners = new EntitySetListener[0];
    }

    private Content content;

    // Unmodifiable view of this entity set. If the value is *this* object,
    // indicates that this set may not be modified.
    private EntitySet view = null;

    private IntArray indices = new IntArray();
    private int indicesModCount = 0;

    public EntitySet() {
        this.content = new Content();
    }

    private EntitySet(EntitySet source) {
        this.content = source.content;
        this.view = this;
    }

    private void checkModification() {
        if (view == this) {
            throw new IllegalArgumentException("Cannot modify the entities of this set");
        }
        content.modCount++;
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
     * Adds the given entity to this set. Throws an exception if this set
     * cannot be modified.
     *
     * @param entity
     *            the entity to add.
     */
    public void addEntity(int entity) {
        IntArray array = pool.obtain();
        array.add(entity);
        addEntities(array);
        array.clear();
        pool.free(array);
    }

    /**
     * Inserts entities into this {@link EntitySet}. Note that this will trigger
     * notifications for <b>all</b> entities in the given array; independent of
     * whether or not they were already in the set. Throws an exception if this
     * set cannot be modified.
     *
     * @param entities
     *            indices of the entities to insert.
     */
    public void addEntities(IntArray entities) {
        checkModification();
        if (entities.size > 0) {
            int[] items = entities.items;
            for (int i = 0, n = items.length; i < n; i++)
                content.entities.set(items[i]);
            for (EntitySetListener listener : content.listeners) {
                listener.inserted(entities);
            }
        }
    }

    public void addEntities(Mask entities) {
        checkModification();
        content.entities.or(entities);
        new Object().hashCode();
        IntArray array = pool.obtain();
        entities.getIndices(array);
        if (array.size > 0) {
            for (EntitySetListener listener : content.listeners) {
                listener.inserted(array);
            }
        }
        array.clear();
        pool.free(array);
    }

    /**
     * Removes the given entity from this set. Throws an exception if this set
     * cannot be modified.
     *
     * @param entity
     *            the entity to remove.
     */
    public void removeEntity(int entity) {
        IntArray array = pool.obtain();
        array.add(entity);
        removeEntities(array);
        array.clear();
        pool.free(array);
    }

    /**
     * Removes entities from this {@link EntitySet}. Note that this will trigger
     * notifications for <b>all</b> entities in the given array; independent of
     * whether or not they were already in the set. Throws an exception if this
     * set cannot be modified.
     *
     * @param entities
     *            indices of the entities to remove.
     */
    public void removeEntities(IntArray entities) {
        checkModification();
        int[] items = entities.items;
        for (int i = 0, n = items.length; i < n; i++)
            content.entities.clear(items[i]);
        for (EntitySetListener listener : content.listeners) {
            listener.removed(entities);
        }
    }

    public void removeEntities(Mask entities) {
        checkModification();
        content.entities.andNot(entities);
        IntArray array = pool.obtain();
        entities.getIndices(array);
        if (array.size > 0) {
            for (EntitySetListener listener : content.listeners) {
                listener.removed(array);
            }
        }
        array.clear();
        pool.free(array);
    }

    /**
     * Checks if this set contains the given entity.
     *
     * @param entity
     *            the entity to check for.
     * @return whether this set contains the given entity.
     */
    public boolean contains(int entity) {
        return content.entities.get(entity);
    }

    /**
     * Returns the entities contained in this entity set. Do <b>not</b> modify
     * this, as it inevitably leads to undefined behavior.
     *
     * @return the entities contained in this set.
     */
    public Mask getMask() {
        return content.entities;
    }

    /**
     * Returns an array containing the indices of all entities in this set.
     * Note that whenever the entity set changes, this array must be
     * reconstructed. Do <b>not</b> modify this, as it inevitably leads to
     * undefined behavior.
     *
     * @return the indices of all entities in this set.
     */
    public IntArray getIndices() {
        if (indicesModCount != content.modCount) {
            indices.clear();
            content.entities.getIndices(indices);
            indicesModCount = content.modCount;
        }
        return indices;
    }
}
