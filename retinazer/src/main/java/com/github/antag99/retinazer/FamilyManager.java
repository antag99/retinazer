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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.github.antag99.retinazer.utils.Bag;
import com.github.antag99.retinazer.utils.Mask;

final class FamilyManager extends EntitySystem {
    private EntityListener[] entityListeners = new EntityListener[0];
    private Bag<Mask> listenersForFamily = new Bag<Mask>();
    private Map<FamilyConfig, Integer> familyIndexes = new HashMap<FamilyConfig, Integer>();
    private Bag<FamilyMatcher> families = new Bag<FamilyMatcher>();

    private Bag<EntitySet> entitiesForFamily = new Bag<EntitySet>();

    private Engine engine;

    public FamilyManager(Engine engine) {
        this.engine = engine;
    }

    public EntitySet getEntities() {
        return getEntitiesFor(Family.EMPTY);
    }

    public EntitySet getEntitiesFor(FamilyConfig family) {
        return entitiesForFamily.get(getFamily(family).index);
    }

    public FamilyMatcher getFamily(FamilyConfig config) {
        int index = familyIndexes.containsKey(config) ? familyIndexes.get(config) : familyIndexes.size();
        if (index == familyIndexes.size()) {
            Mask components = new Mask();
            Mask excludedComponents = new Mask();

            for (Class<? extends Component> componentType : config.getComponents())
                components.set(engine.componentManager.getIndex(componentType));
            for (Class<? extends Component> componentType : config.getExcludedComponents())
                excludedComponents.set(engine.componentManager.getIndex(componentType));

            familyIndexes.put(config.clone(), index);
            families.set(index, new FamilyMatcher(components, excludedComponents, index));
            entitiesForFamily.set(index, new EntitySet(engine));
            listenersForFamily.set(index, new Mask());

            for (int i = engine.entityManager.currentEntities.nextSetBit(0); i != -1; i = engine.entityManager.currentEntities.nextSetBit(i + 1)) {
                updateFamilyMembership(engine.entityManager.getEntityForIndex(i), false);
            }
        }

        return families.get(index);
    }

    public void addEntityListener(EntityListener listener) {
        addEntityListener(Family.EMPTY, listener);
    }

    public void addEntityListener(FamilyConfig family, EntityListener listener) {
        int index = -1;
        for (int i = 0, n = entityListeners.length; i < n; ++i)
            if (entityListeners[i] == listener)
                index = i;

        if (index == -1) {
            index = entityListeners.length;
            entityListeners = Arrays.copyOf(entityListeners, index + 1);
            entityListeners[index] = listener;
        }

        listenersForFamily.get(getFamily(family).index).set(index);
    }

    public void removeEntityListener(EntityListener listener) {
        for (int index = 0; index < entityListeners.length; ++index) {
            if (entityListeners[index] == listener) {
                int lastIndex = entityListeners.length - 1;
                EntityListener last = entityListeners[lastIndex];
                entityListeners = Arrays.copyOf(entityListeners, lastIndex);
                entityListeners[index] = last;

                for (int i = 0, n = familyIndexes.size(); i < n; ++i) {
                    Mask listeners = listenersForFamily.get(i);
                    if (listeners.get(lastIndex)) {
                        listeners.set(index);
                        listeners.clear(lastIndex);
                    } else {
                        listeners.clear(index);
                    }
                }
            }
        }
    }

    public void updateFamilyMembership(Entity entity, boolean remove) {
        final Mask entityFamilies = entity.families;
        // Find families that the entity was added to/removed from, and fill
        // the bit sets with corresponding listener bits.
        Mask addListenerBits = new Mask();
        Mask removeListenerBits = new Mask();

        for (int i = 0, n = this.familyIndexes.size(); i < n; ++i) {
            final Mask listenersMask = this.listenersForFamily.get(i);
            final EntitySet familyEntities = entitiesForFamily.get(i);

            boolean belongsToFamily = entityFamilies.get(i);
            boolean matches = families.get(i).matches(entity) && !remove;

            if (belongsToFamily != matches) {
                if (matches) {
                    addListenerBits.or(listenersMask);
                    familyEntities.entities.set(entity.getIndex());
                    entityFamilies.set(i);
                } else {
                    removeListenerBits.or(listenersMask);
                    familyEntities.entities.clear(entity.getIndex());
                    entityFamilies.clear(i);
                }
                familyEntities.entityIndicesDirty = true;
            }
        }

        // Store the current listeners in a local variable, so they
        // can't be changed (the backing array is copied before modification)
        EntityListener[] items = this.entityListeners;

        for (int i = removeListenerBits.nextSetBit(0); i != -1; i = removeListenerBits.nextSetBit(i + 1)) {
            items[i].entityRemove(entity);
        }

        for (int i = addListenerBits.nextSetBit(0); i != -1; i = addListenerBits.nextSetBit(i + 1)) {
            items[i].entityAdd(entity);
        }
    }

    public void reset() {
        entityListeners = new EntityListener[0];
        listenersForFamily.clear();
        familyIndexes.clear();
        families.clear();
        entitiesForFamily.clear();
    }
}
