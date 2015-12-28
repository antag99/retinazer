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

import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.antag99.retinazer.util.Bag;
import com.github.antag99.retinazer.util.Mask;

final class FamilyManager {
    private static class Key {
        ObjectSet<Class<? extends Component>> components = null;
        ObjectSet<Class<? extends Component>> excludedComponents = null;

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            // No need for a type check; this class is only used internally
            Key key = (Key) obj;
            return key.excludedComponents.equals(excludedComponents) &&
                    key.components.equals(components);
        }

        @Override
        public int hashCode() {
            // Excluded components are rarer than required components; prioritize
            // the components hashCode over excluded components hashCode.
            return 31 * excludedComponents.hashCode() + components.hashCode();
        }
    }

    private ObjectIntMap<Key> familyIndices = new ObjectIntMap<>();
    private Bag<Family> families = new Bag<>();
    private Engine engine;
    private Key lookup = new Key();
    private EntitySet entities;
    private EntitySet argument = new EntitySet();
    private Mask tmpMask = new Mask();
    private Mask tmpMatchedEntities = new Mask();

    public FamilyManager(Engine engine, EngineConfig config) {
        this.engine = engine;
    }

    public EntitySet getEntities() {
        if (entities == null) {
            entities = getFamily(new FamilyConfig()).getEntities();
        }
        return entities;
    }

    public Family getFamily(FamilyConfig config) {
        lookup.components = config.components;
        lookup.excludedComponents = config.excludedComponents;
        int index = familyIndices.get(lookup, familyIndices.size);
        if (index == familyIndices.size) {
            int i;
            int[] components = new int[config.components.size];
            int[] excludedComponents = new int[config.excludedComponents.size];

            i = 0;
            for (Class<? extends Component> componentType : config.components)
                components[i++] = engine.componentManager.getIndex(componentType);

            i = 0;
            for (Class<? extends Component> componentType : config.excludedComponents)
                excludedComponents[i++] = engine.componentManager.getIndex(componentType);

            Family family = new Family(engine, components, excludedComponents, index);
            Key key = new Key();
            key.components = config.components;
            key.excludedComponents = config.excludedComponents;
            familyIndices.put(key, index);
            families.set(index, family);

            // Find matching entities, and add them to the new family set.
            Mapper<?>[] mappers = engine.componentManager.array;
            Mask matchedEntities = new Mask().set(engine.entityManager.entities);

            for (int component : components) {
                matchedEntities.and(mappers[component].componentsMask);
            }

            for (int excludedComponent : excludedComponents) {
                matchedEntities.andNot(mappers[excludedComponent].componentsMask);
            }

            // No notifications to dispatch here
            family.entities.edit().addEntities(matchedEntities);
        }

        return families.get(index);
    }

    /**
     * Updates family membership for all entities. This will insert/remove entities
     * to/from family sets.
     */

    void updateFamilyMembership() {
        Mapper<?>[] mappers = engine.componentManager.array;

        Mask tmpMask = this.tmpMask;
        Mask tmpMatchedEntities = this.tmpMatchedEntities;

        for (int i = 0, n = familyIndices.size; i < n; i++) {
            Family family = families.get(i);
            EntitySet entities = family.entities;

            Mask matchedEntities = tmpMatchedEntities.set(engine.entityManager.entities);
            matchedEntities.andNot(engine.entityManager.remove);

            int[] components = family.components;
            for (int component : components) {
                Mapper<?> mapper = mappers[component];
                tmpMask.set(mapper.componentsMask);
                tmpMask.andNot(mapper.removeMask);
                matchedEntities.and(tmpMask);
            }

            int[] excludedComponents = family.excludedComponents;
            for (int excludedComponent : excludedComponents) {
                Mapper<?> mapper = mappers[excludedComponent];
                tmpMask.set(mapper.componentsMask);
                tmpMask.andNot(mapper.removeMask);
                matchedEntities.andNot(tmpMask);
            }

            family.insertEntities.set(matchedEntities);
            family.insertEntities.andNot(entities.getMask());
            entities.edit().addEntities(family.insertEntities);

            family.removeEntities.set(entities.getMask());
            family.removeEntities.andNot(matchedEntities);
            entities.edit().removeEntities(family.removeEntities);
        }

        for (int i = 0, n = familyIndices.size; i < n; i++) {
            Family family = families.get(i);

            if (!family.insertEntities.isEmpty()) {
                argument.edit().addEntities(family.insertEntities);
                for (EntityListener listener : family.listeners) {
                    listener.inserted(argument.view());
                }
                argument.edit().clear();
            }

            if (!family.removeEntities.isEmpty()) {
                argument.edit().addEntities(family.removeEntities);
                for (EntityListener listener : family.listeners) {
                    listener.removed(argument.view());
                }
                argument.edit().clear();
            }
        }
    }
}
