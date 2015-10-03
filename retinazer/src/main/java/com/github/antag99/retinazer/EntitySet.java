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
import com.github.antag99.retinazer.util.Mask;

public final class EntitySet {

    private final EntitySetContent content;

    private final EntitySetEdit edit;

    private final EntitySet view;

    public EntitySet() {
        this.content = new EntitySetContent();
        this.edit = new EntitySetEdit(content);
        this.view = new EntitySet(content);
    }

    public EntitySet(EntitySet set) {
        this();

        edit().addEntities(set.getMask());
    }

    /**
     * Creates an unmodifiable view of the given content.
     */
    private EntitySet(EntitySetContent content) {
        this.content = content;
        this.edit = null;
        this.view = this;
    }

    public EntitySetEdit edit() {
        if (edit == null) {
            throw new RetinazerException("Cannot modify the entities of this set");
        }

        return edit;
    }

    /**
     * Returns an unmodifiable view of this entity set.
     *
     * @return Unmodifiable view of this entity set.
     */
    public EntitySet view() {
        return view;
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
     * Returns the entities contained in this entity set.
     * Do <b>not</b> modify this.
     *
     * @return the entities contained in this set.
     */
    public Mask getMask() {
        return content.entities;
    }

    /**
     * Returns an array containing the indices of all entities in this set.
     * Note that whenever the entity set changes, this array must be
     * reconstructed. Do <b>not</b> modify this.
     *
     * @return the indices of all entities in this set.
     */
    public IntArray getIndices() {
        if (content.indicesDirty) {
            content.indices.clear();
            content.entities.getIndices(content.indices);
            content.indicesDirty = false;
        }
        return content.indices;
    }

    public int size() {
        return content.indicesDirty ? content.entities.cardinality() : content.indices.size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EntitySet))
            return false;
        return ((EntitySet) obj).content.entities.equals(content.entities);
    }

    @Override
    public int hashCode() {
        return content.entities.hashCode();
    }

    @Override
    public String toString() {
        IntArray indices = getIndices();
        if (indices.size == 0) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        int[] items = indices.items;
        builder.append(items[0]);
        for (int i = 1, n = indices.size; i < n; i++) {
            builder.append(',');
            builder.append(' ');
            builder.append(items[i]);
        }
        builder.append(']');
        return builder.toString();
    }
}
