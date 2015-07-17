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

import com.github.antag99.retinazer.utils.Bag;
import com.github.antag99.retinazer.utils.Inject;
import com.github.antag99.retinazer.utils.Mask;
import com.github.antag99.retinazer.utils.GuidComponent;

final class EntityManager extends EntitySystem {
    // The entity bag; stores all current entities
    Bag<Entity> entities = new Bag<Entity>();
    // Indices of entities that are currently active
    Mask currentEntities = new Mask();
    // Indices of entities that will be active next tick
    Mask nextEntities = new Mask();
    Mask tmpMask = new Mask();

    private @Inject Engine engine;
    private @Inject FamilyManager familyManager;
    private @Inject ComponentManager componentManager;

    public EntityManager(EngineConfig config) {
    }

    public Entity createEntity() {
        int index = nextEntities.nextClearBit(0);
        Entity entity = new Entity(engine, index);
        entities.set(index, entity);
        nextEntities.set(index);
        return entity;
    }

    public Entity createEntity(long guid) {
        Entity entity = createEntity();
        engine.guidMapper.add(entity, new GuidComponent(guid));
        return entity;
    }

    public void destroyEntity(Entity entity) {
        if (!nextEntities.get(entity.getIndex()))
            return;
        nextEntities.clear(entity.getIndex());
        componentManager.destroyComponents(entity);
    }

    public Entity getEntityForIndex(int index) {
        if (!currentEntities.get(index))
            throw new IllegalArgumentException("No such entity: " + index);
        return entities.get(index);
    }

    public void reset() {
        entities.clear();
        currentEntities.clear();
        nextEntities.clear();
    }

    public void applyEntityAdditions() {
        Mask addedEntities = tmpMask.set(nextEntities);
        addedEntities.xor(currentEntities);
        addedEntities.and(nextEntities);
        currentEntities.or(addedEntities);

        for (int i = addedEntities.nextSetBit(0); i != -1; i = addedEntities.nextSetBit(i + 1)) {
            Entity entity = entities.get(i);
            familyManager.updateFamilyMembership(entity, false);
        }
    }

    public void applyEntityRemovals() {
        Mask removedEntities = tmpMask.set(nextEntities);
        removedEntities.xor(currentEntities);
        removedEntities.and(currentEntities);
        currentEntities.xor(removedEntities);

        for (int i = removedEntities.nextSetBit(0); i != -1; i = removedEntities.nextSetBit(i + 1)) {
            Entity entity = entities.get(i);
            familyManager.updateFamilyMembership(entity, true);
            entities.set(i, null);
        }
    }
}
