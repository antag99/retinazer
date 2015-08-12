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

import java.util.HashMap;
import java.util.Map;

import com.github.antag99.retinazer.utils.Bag;
import com.github.antag99.retinazer.utils.Mask;

final class FamilyManager extends EntitySystem {
    private Map<FamilyConfig, Integer> familyIndexes = new HashMap<>();
    private Bag<Family> families = new Bag<>();
    private Bag<EntitySet> entitiesForFamily = new Bag<>();
    private Engine engine;

    public FamilyManager(Engine engine) {
        this.engine = engine;
    }

    public EntitySet getEntities() {
        return getEntitiesFor(Family.EMPTY);
    }

    public EntitySet getEntitiesFor(FamilyConfig family) {
        return entitiesForFamily.get(getFamily(family).index).unmodifiable();
    }

    public Family getFamily(FamilyConfig config) {
        int index = familyIndexes.containsKey(config) ? familyIndexes.get(config) : familyIndexes.size();
        if (index == familyIndexes.size()) {
            Mask components = new Mask();
            Mask excludedComponents = new Mask();
            @SuppressWarnings("unchecked")
            Class<? extends Component>[] componentsArray = config.getComponents().toArray(new Class[0]);
            @SuppressWarnings("unchecked")
            Class<? extends Component>[] excludedComponentsArray = config.getExcludedComponents().toArray(new Class[0]);
            for (Class<? extends Component> componentType : componentsArray)
                components.set(engine.componentManager.getIndex(componentType));
            for (Class<? extends Component> componentType : excludedComponentsArray)
                excludedComponents.set(engine.componentManager.getIndex(componentType));

            familyIndexes.put(config.clone(), index);
            families.set(index, new Family(components, excludedComponents,
                    componentsArray, excludedComponentsArray, index));
            entitiesForFamily.set(index, new EntitySet(engine));

            for (int i = engine.entityManager.currentEntities.nextSetBit(0); i != -1; i = engine.entityManager.currentEntities.nextSetBit(i + 1)) {
                updateFamilyMembership(engine.entityManager.getEntityForIndex(i), false);
            }
        }

        return families.get(index);
    }

    public void updateFamilyMembership(Entity entity, boolean remove) {
        final Mask entityFamilies = entity.families;

        for (int i = 0, n = this.familyIndexes.size(); i < n; ++i) {
            final Family family = families.get(i);
            final EntitySet familyContent = entitiesForFamily.get(i);

            boolean belongsToFamily = entityFamilies.get(i);
            boolean matches = family.matches(entity) && !remove;

            if (belongsToFamily != matches) {
                if (matches) {
                    familyContent.addEntity(entity);
                    entityFamilies.set(i);
                } else {
                    familyContent.removeEntity(entity);
                    entityFamilies.clear(i);
                }
            }
        }
    }
}
