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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.github.antag99.retinazer.utils.Mask;

final class ComponentManager implements EntitySystem {
    private ComponentMapper<?>[] componentMappers;

    private Engine engine;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ComponentManager(Engine engine) {
        this.engine = engine;
        List<ComponentMapper<?>> componentMappers = new ArrayList<ComponentMapper<?>>();
        for (Class<? extends Component> componentType : engine.config.getComponentTypes()) {
            componentMappers.add(new ComponentMapper(componentType));
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
    public <T extends Component> ComponentMapper<T> getMapper(Class<T> componentType) {
        return (ComponentMapper<T>) componentMappers[getIndex(componentType)];
    }

    public Iterable<Component> getComponents(final Entity entity) {
        return new Iterable<Component>() {
            @Override
            public Iterator<Component> iterator() {
                return new ComponentIterator(entity);
            }
        };
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
            if (!mapper.dirty) {
                continue;
            }
            mapper.dirty = false;

            Mask componentsAdded = engine.maskPool.obtain().set(mapper.componentsAdded);
            Mask componentsRemoved = engine.maskPool.obtain().set(mapper.componentsRemoved);
            mapper.componentsAdded.clear();
            mapper.componentsRemoved.clear();

            for (int ii = componentsRemoved.nextSetBit(0); ii != -1; ii = componentsRemoved.nextSetBit(ii + 1)) {
                Entity entity = engine.entityManager.getEntityForIndex(ii);
                entity.components.clear(i);
                engine.familyManager.updateFamilyMembership(entity, false);
            }

            for (int ii = componentsRemoved.nextSetBit(0); ii != -1; ii = componentsRemoved.nextSetBit(ii + 1)) {
                mapper.components.set(ii, null);
            }

            for (int ii = componentsAdded.nextSetBit(0); ii != -1; ii = componentsAdded.nextSetBit(ii + 1)) {
                mapper.components.set(ii, mapper.nextComponents.get(ii));
            }

            for (int ii = componentsAdded.nextSetBit(0); ii != -1; ii = componentsAdded.nextSetBit(ii + 1)) {
                Entity entity = engine.entityManager.getEntityForIndex(ii);
                entity.components.set(i);
                engine.familyManager.updateFamilyMembership(entity, false);
            }

            engine.maskPool.free(componentsAdded);
            engine.maskPool.free(componentsRemoved);
        }
    }
}
