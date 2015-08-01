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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.github.antag99.retinazer.utils.Bag;
import com.github.antag99.retinazer.utils.Mask;

final class ComponentManager extends EntitySystem {
    private Engine engine;

    /*
     * Component types are mapped using an optimized hash table, that handles
     * colliding keys using an extra array, and doesn't permit modification
     * once initialized. The size is always a power of two, which allows
     * reducing the modulus operation to a binary AND operation. The size is
     * always four times the number of component types, to minimize the amount
     * of keys that have to be put in the extra array (stash).
     *
     * Note that this is done to achieve maximum performance without using
     * so-called "component mappers" or other clever stuff.
     *
     * Usually, component types won't have to be looked up for every entity,
     * but rather once per iteration, as the weaver optimizes retrieval.
     */
    private final int hashMask;
    private final ComponentStorage<?>[] table;
    private final Class<? extends Component>[] stashTypes;
    private final ComponentStorage<?>[] stashTable;
    private final ComponentStorage<?>[] array;

    static int nextPowerOfTwo(int value) {
        if (value == 0) {
            return 1;
        }
        value--;
        value |= value >>> 1;
        value |= value >>> 2;
        value |= value >>> 4;
        value |= value >>> 8;
        value |= value >>> 16;
        return value + 1;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    ComponentManager(Engine engine) {
        this.engine = engine;

        Collection<Class<? extends Component>> componentTypes = (Collection) engine.getComponentTypes();

        // Let the table size have a load factor of about 0.25
        int capacity = nextPowerOfTwo(componentTypes.size()) * 4;

        hashMask = capacity - 1;
        table = new ComponentStorage[capacity];
        array = new ComponentStorage[componentTypes.size()];

        Bag<Class<? extends Component>> types = new Bag<>();
        List<Class<? extends Component>> conflictingTypes = new ArrayList<>();
        for (Class<? extends Component> type : componentTypes) {
            int slot = type.hashCode() & hashMask;

            // Search for colliding types that will be put in the stash
            for (Class<? extends Component> conflictingType : conflictingTypes) {
                if (slot == (conflictingType.hashCode() & hashMask)) {
                    conflictingTypes.add(type);
                    break;
                }
            }

            if (types.get(slot) != null) {
                conflictingTypes.add(types.get(slot));
                conflictingTypes.add(type);
                types.set(slot, null);
            }

            // Else, add it to the table
            if (!conflictingTypes.contains(type)) {
                types.set(slot, type);
            }
        }

        int nextIndex = 0;

        for (int i = 0, n = types.getCapacity(); i < n; i++) {
            if (types.get(i) != null) {
                table[i] = new ComponentStorage<>(types.get(i), nextIndex++);
                array[table[i].index] = table[i];
            }
        }

        stashTypes = new Class[conflictingTypes.size()];
        stashTable = new ComponentStorage[conflictingTypes.size()];

        for (int i = 0, n = conflictingTypes.size(); i < n; i++) {
            stashTypes[i] = conflictingTypes.get(i);
            stashTable[i] = new ComponentStorage<>(stashTypes[i], nextIndex++);
            array[stashTable[i].index] = stashTable[i];
        }
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
            return array[componentIndex].get(entity);
        }

        @Override
        public void remove() {
            if (previousIndex == -1)
                throw new IllegalStateException();
            array[previousIndex].remove(entity);
            previousIndex = -1;
        }
    }

    int getIndex(Class<? extends Component> componentType) {
        return getStorage(componentType).index;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    <T extends Component> ComponentStorage<T> getStorage(Class<T> componentType) {
        int slot = componentType.hashCode() & hashMask;
        ComponentStorage storage = table[slot];
        if (storage != null) {
            return storage;
        }
        for (int i = 0, n = stashTypes.length; i < n; i++) {
            if (stashTypes[i] == componentType) {
                return (ComponentStorage<T>) stashTable[i];
            }
        }
        throw new IllegalArgumentException(
                "Component type has not been registered: " +
                        componentType.getName());
    }

    Iterable<Component> getComponents(final Entity entity) {
        return new Iterable<Component>() {
            @Override
            public Iterator<Component> iterator() {
                return new ComponentIterator(entity);
            }
        };
    }

    void destroyComponents(Entity entity) {
        for (ComponentStorage<?> storage : array) {
            storage.remove(entity);
        }
    }

    void applyComponentChanges() {
        for (int i = 0, n = array.length; i < n; ++i) {
            @SuppressWarnings("unchecked")
            ComponentStorage<Component> storage = (ComponentStorage<Component>) array[i];
            if (!storage.dirty) {
                continue;
            }
            storage.dirty = false;

            Mask componentsAdded = engine.maskPool.obtain().set(storage.componentsAdded);
            Mask componentsRemoved = engine.maskPool.obtain().set(storage.componentsRemoved);
            storage.componentsAdded.clear();
            storage.componentsRemoved.clear();

            for (int ii = componentsRemoved.nextSetBit(0); ii != -1; ii = componentsRemoved.nextSetBit(ii + 1)) {
                Entity entity = engine.entityManager.getEntityForIndex(ii);
                entity.components.clear(i);
                engine.familyManager.updateFamilyMembership(entity, false);
            }

            for (int ii = componentsRemoved.nextSetBit(0); ii != -1; ii = componentsRemoved.nextSetBit(ii + 1)) {
                storage.components.set(ii, null);
            }

            for (int ii = componentsAdded.nextSetBit(0); ii != -1; ii = componentsAdded.nextSetBit(ii + 1)) {
                storage.components.set(ii, storage.nextComponents.get(ii));
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
