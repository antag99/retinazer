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
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.github.antag99.retinazer.utils.Bag;
import com.github.antag99.retinazer.utils.Mask;

final class FamilyManager {
    private static Pool<Mask> pool = Pools.get(Mask.class);

    private ObjectIntMap<FamilyConfig> familyIndices = new ObjectIntMap<>();
    private Bag<Family> families = new Bag<>();
    private Engine engine;

    public FamilyManager(Engine engine, EngineConfig config) {
        this.engine = engine;
    }

    public EntitySet getEntities() {
        return getEntitiesFor(Family.EMPTY);
    }

    public EntitySet getEntitiesFor(FamilyConfig family) {
        return getFamily(family).entities.unmodifiable();
    }

    public Family getFamily(FamilyConfig config) {
        int index = familyIndices.get(config, familyIndices.size);
        if (index == familyIndices.size) {
            int i;
            int[] components = new int[config.getComponents().size()];
            int[] excludedComponents = new int[config.getExcludedComponents().size()];

            i = 0;
            for (Class<? extends Component> componentType : config.getComponents())
                components[i++] = engine.componentManager.getIndex(componentType);

            i = 0;
            for (Class<? extends Component> componentType : config.getExcludedComponents())
                excludedComponents[i++] = engine.componentManager.getIndex(componentType);

            Family family = new Family(engine, components, excludedComponents, index);
            familyIndices.put(config, index);
            families.set(index, family);

            // Find matching entities, and add them to the new family set.
            Mapper<?>[] mappers = engine.componentManager.array;
            Mask matchedEntities = pool.obtain().set(engine.entityManager.entities);

            for (int component : components) {
                matchedEntities.and(mappers[component].componentsMask);
            }

            for (int excludedComponent : excludedComponents) {
                matchedEntities.andNot(mappers[excludedComponent].componentsMask);
            }

            family.entities.addEntities(matchedEntities);

            pool.free(matchedEntities);
        }

        return families.get(index);
    }

    /**
     * Updates family membership for all entities. This will insert/remove entities
     * to/from family sets.
     */

    void updateFamilyMembership() {
        Mapper<?>[] mappers = engine.componentManager.array;

        Mask tmpMask = pool.obtain();

        for (int i = 0, n = familyIndices.size; i < n; i++) {
            int[] components = families.get(i).components;
            int[] excludedComponents = families.get(i).excludedComponents;
            EntitySet entities = families.get(i).entities;

            Mask matchedEntities = pool.obtain().set(engine.entityManager.entities);

            for (int component : components) {
                Mapper<?> mapper = mappers[component];
                tmpMask.set(mapper.componentsMask);
                tmpMask.andNot(mapper.removeComponentsMask);
                matchedEntities.and(tmpMask);
            }

            for (int excludedComponent : excludedComponents) {
                Mapper<?> mapper = mappers[excludedComponent];
                tmpMask.set(mapper.componentsMask);
                tmpMask.andNot(mapper.removeComponentsMask);
                matchedEntities.andNot(tmpMask);
            }

            Mask insertFamilyEntities = pool.obtain().set(matchedEntities);
            insertFamilyEntities.andNot(entities.getMask());
            entities.addEntities(insertFamilyEntities);
            pool.free(insertFamilyEntities);

            Mask removeFamilyEntities = pool.obtain().set(entities.getMask());
            removeFamilyEntities.andNot(matchedEntities);
            entities.removeEntities(removeFamilyEntities);
            pool.free(removeFamilyEntities);

            pool.free(matchedEntities);
        }

        pool.free(tmpMask);
    }
}
