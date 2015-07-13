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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.github.antag99.retinazer.utils.Inject;

final class ComponentManager extends EntitySystem {
    private ComponentMapper<?>[] componentMappers;

    private @Inject EntityManager entityManager;
    private @Inject FamilyManager familyManager;

    public ComponentManager(EngineConfig config) {
        List<ComponentMapper<?>> componentMappers = new ArrayList<>();
        for (Class<? extends Component> componentType : config.getComponentTypes()) {
            componentMappers.add(new ComponentMapper<>(componentType));
        }
        this.componentMappers = componentMappers.toArray(new ComponentMapper[0]);
    }

    private final class ComponentIterator implements Iterator<Component> {
        private Entity entity;
        private int index = 0;
        private int previousIndex = -1;

        public ComponentIterator(Entity entity) {
            this.entity = entity;
        }

        @Override
        public boolean hasNext() {
            return entity.components.nextSetBit(index) != -1;
        }

        @Override
        public Component next() {
            if (!hasNext())
                throw new NoSuchElementException();
            int componentIndex = entity.components.nextSetBit(index);
            index = componentIndex + 1;
            previousIndex = componentIndex;
            return componentMappers[componentIndex].get(entity);
        }

        @Override
        public void remove() {
            if (previousIndex == -1)
                throw new IllegalStateException();
            componentMappers[previousIndex].remove(entity);
            previousIndex = -1;
        }
    }

    public int getIndex(Class<? extends Component> componentType) {
        for (int i = 0, n = componentMappers.length; i < n; i++)
            if (componentMappers[i].getType() == componentType)
                return i;
        throw new IllegalArgumentException("Component type " + componentType.getName() + " has not been registered");
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> ComponentMapper<T> getMapper(int index) {
        return (ComponentMapper<T>) componentMappers[index];
    }

    public <T extends Component> ComponentMapper<T> getMapper(Class<T> componentType) {
        return getMapper(getIndex(componentType));
    }

    public Iterable<Component> getComponents(Entity entity) {
        return () -> new ComponentIterator(entity);
    }

    public void destroyComponents(Entity entity) {
        for (int i = 0, n = componentMappers.length; i < n; ++i) {
            @SuppressWarnings("unchecked")
            ComponentMapper<Component> mapper = (ComponentMapper<Component>) componentMappers[i];
            mapper.remove(entity);
        }
    }

    public void applyComponentChanges() {
        for (int i = 0, n = componentMappers.length; i < n; ++i) {
            @SuppressWarnings("unchecked")
            ComponentMapper<Component> mapper = (ComponentMapper<Component>) componentMappers[i];

            BitSet componentsAdded = (BitSet) mapper.componentsAdded.clone();
            mapper.componentsAdded.clear();

            BitSet componentsRemoved = (BitSet) mapper.componentsRemoved.clone();
            mapper.componentsRemoved.clear();

            for (int k = componentsRemoved.nextSetBit(0); k != -1; k = componentsRemoved.nextSetBit(k + 1)) {
                mapper.components.set(k, null);
            }

            for (int k = componentsAdded.nextSetBit(0); k != -1; k = componentsAdded.nextSetBit(k + 1)) {
                mapper.components.set(k, mapper.nextComponents.get(k));
            }

            for (int k = componentsRemoved.nextSetBit(0); k != -1; k = componentsRemoved.nextSetBit(k + 1)) {
                Entity entity = entityManager.getEntityForIndex(k);
                entity.components.clear(i);
                familyManager.updateFamilyMembership(entity, false);
            }

            for (int k = componentsAdded.nextSetBit(0); k != -1; k = componentsAdded.nextSetBit(k + 1)) {
                Entity entity = entityManager.getEntityForIndex(k);
                entity.components.set(i);
                familyManager.updateFamilyMembership(entity, false);
            }
        }
    }
}
