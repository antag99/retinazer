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
