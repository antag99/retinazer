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
/** Ashley copyright notice */
/*******************************************************************************
 * Copyright 2014 See AUTHORS.ASHLEY file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.github.antag99.retinazer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.antag99.retinazer.util.Bag;
import com.github.antag99.retinazer.util.Mask;

final class FamilyManager {
    private static class Key {
        Set<Class<? extends Component>> components = null;
        Set<Class<? extends Component>> excludedComponents = null;

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

    private Map<Key, Integer> familyIndices = new HashMap<>();
    private Bag<Family> families = new Bag<>();
    private Engine engine;
    private Key lookup = new Key();
    private EntitySet entities;
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
        Integer index = familyIndices.get(lookup);
        if (index == null) {
            index = familyIndices.size();
            int i;
            int[] components = new int[config.components.size()];
            int[] excludedComponents = new int[config.excludedComponents.size()];

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
            Mask matchedEntities = new Mask().copyFrom(engine.entityManager.entities);

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

        for (int i = 0, n = familyIndices.size(); i < n; i++) {
            Family family = families.get(i);
            EntitySet entities = family.entities;

            Mask matchedEntities = tmpMatchedEntities.copyFrom(engine.entityManager.entities);
            matchedEntities.andNot(engine.entityManager.remove);

            int[] components = family.components;
            for (int component : components) {
                Mapper<?> mapper = mappers[component];
                tmpMask.copyFrom(mapper.componentsMask);
                tmpMask.andNot(mapper.removeMask);
                matchedEntities.and(tmpMask);
            }

            int[] excludedComponents = family.excludedComponents;
            for (int excludedComponent : excludedComponents) {
                Mapper<?> mapper = mappers[excludedComponent];
                tmpMask.copyFrom(mapper.componentsMask);
                tmpMask.andNot(mapper.removeMask);
                matchedEntities.andNot(tmpMask);
            }

            family.insertEntities.copyFrom(matchedEntities);
            family.insertEntities.andNot(entities.getMask());
            entities.edit().addEntities(family.insertEntities);

            family.removeEntities.copyFrom(entities.getMask());
            family.removeEntities.andNot(matchedEntities);
            entities.edit().removeEntities(family.removeEntities);
        }
    }
}
