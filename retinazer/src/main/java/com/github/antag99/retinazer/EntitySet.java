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

import com.github.antag99.retinazer.util.IntBag;
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
    public IntBag getIndices() {
        if (content.indicesDirty) {
            content.indices.clear();
            content.entities.getIndices(content.indices, 0);
            content.indicesDirty = false;
        }
        return content.indices;
    }

    public int size() {
        return content.entities.cardinality();
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
        IntBag indices = getIndices();
        if (size() == 0) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        int[] buffer = indices.buffer;
        builder.append(buffer[0]);
        for (int i = 1, n = size(); i < n; i++) {
            builder.append(',');
            builder.append(' ');
            builder.append(buffer[i]);
        }
        builder.append(']');
        return builder.toString();
    }
}
