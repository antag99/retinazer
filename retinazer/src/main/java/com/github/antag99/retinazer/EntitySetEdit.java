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

public final class EntitySetEdit {
    private final EntitySetContent content;

    EntitySetEdit(EntitySetContent content) {
        this.content = content;
    }

    public void addEntity(int entity) {
        if (!content.entities.get(entity)) {
            content.indicesDirty = true;
            content.entities.set(entity);
        }
    }

    public void addEntities(Mask entities) {
        if (!content.entities.isSupersetOf(entities)) {
            content.indicesDirty = true;
            content.entities.or(entities);
        }
    }

    public void removeEntity(int entity) {
        if (content.entities.get(entity)) {
            content.indicesDirty = true;
            content.entities.clear(entity);
        }
    }

    public void removeEntities(Mask entities) {
        if (content.entities.intersects(entities)) {
            content.indicesDirty = true;
            content.entities.andNot(entities);
        }
    }

    public void clear() {
        if (!content.entities.isEmpty()) {
            content.indicesDirty = true;
            content.entities.clear();
        }
    }
}
