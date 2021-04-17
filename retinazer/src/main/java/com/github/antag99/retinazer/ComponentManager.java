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

import com.github.antag99.retinazer.util.Mask;

import java.lang.reflect.InvocationTargetException;

final class ComponentManager {
    private Engine engine;

    ComponentManager(Engine engine, EngineConfig config) {
        this.engine = engine;
    }

    /*
     * Component types are mapped using an optimized hash table, that handles
     * colliding keys using an extra array, and is rebuilt every time the
     * entries are changed. The size is always a power of two, which allows
     * reducing the modulus operation to a binary AND operation. The size is
     * always four times the number of component types, to minimize the amount
     * of keys that have to be put in the extra array (stash).
     *
     * Note that component types are typically only looked up when retrieving
     * Mapper instances and when adding components using Handle.
     */
    int hashMask = 3;
    Mapper<?>[] table = new Mapper<?>[4];
    Mapper<?>[] stash = new Mapper<?>[0];
    Mapper<?>[] array = new Mapper<?>[0];

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

    /**
     * Rebuilds the map use for looking up component types. This is required
     * every time a new component type is needed, which will hopefully not be
     * during processing, but rather during initialization.
     *
     * @param <T>
     *            generic component type.
     * @param additionalType
     *            component type to add to the map.
     */
    private <T extends Component> void rebuild(Class<T> additionalType) {
        if (!PackedComponent.class.isAssignableFrom(additionalType))
            rebuild(new PlainMapper<T>(engine, additionalType, array.length));
        else {
            String componentName = additionalType.getName();
            String mapperName = componentName + "$Mapper";
            try {
                Class<?> mapperType = Class.forName(mapperName, true, additionalType.getClassLoader());
                PackedMapper<?> mapper = (PackedMapper<?>) mapperType
                        .getConstructor(Engine.class, Integer.TYPE)
                        .newInstance(engine, array.length);
                rebuild(mapper);
            } catch (ClassNotFoundException ex) {
                throw new RetinazerException(String.format("Component class %s is packed, " +
                        "but mapper %s was not found", componentName, mapperName), ex);
            } catch (NoSuchMethodException | InstantiationException | /**/
                    IllegalAccessException | InvocationTargetException ex) {
                throw new RetinazerException(String.format("Failed to initialize mapper %s", mapperName), ex);
            }
        }
    }

    private <T extends Component> void rebuild(Mapper<T> additionalMapper) {
        // Copy the array and add the new type
        Mapper<?>[] newArray = new Mapper<?>[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = additionalMapper;
        this.array = newArray;
        // Add the mapper to the engine state
        engine.state.componentState.set(
                engine.state.componentStateCount++, additionalMapper.createState());

        // Create backing hash table filled to about 25%; this is done to
        // minimize hash code collisions.
        int capacity = nextPowerOfTwo(array.length) * 4;

        hashMask = capacity - 1;
        table = new Mapper[capacity];

        // Types that conflict and need to be put in the stash + respective slots
        Mask conflictingTypes = new Mask();
        Mask conflictingSlots = new Mask();

        for (int i = 0, n = array.length; i < n; i++) {
            Mapper<?> mapper = array[i];

            // Locate slot; same as `slot = hashCode % capacity;`
            int slot = mapper.type.hashCode() & hashMask;

            // Check if an existing type shares the same slot
            if (table[slot] != null) {
                conflictingSlots.set(slot);
                conflictingTypes.set(table[slot].typeIndex);
                table[slot] = null;
            }

            if (conflictingSlots.get(slot)) {
                conflictingTypes.set(i);
            } else {
                table[slot] = mapper;
            }
        }

        // Put conflicting types in the stash
        int[] indices = conflictingTypes.getIndices();
        stash = new Mapper[indices.length];
        for (int i = 0, n = indices.length; i < n; i++) {
            stash[i] = array[indices[i]];
        }
    }

    int getIndex(Class<? extends Component> componentType) {
        return getMapper(componentType).typeIndex;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    <T extends Component> Mapper<T> getMapper(Class<T> componentType) {
        int slot = componentType.hashCode() & hashMask;
        Mapper storage = table[slot];
        if (storage != null) {
            if (storage.type == componentType) {
                return storage;
            }
        } else {
            Mapper<?>[] stash = this.stash;
            for (int i = 0, n = stash.length; i < n; i++) {
                if (stash[i].type == componentType) {
                    return (Mapper<T>) stash[i];
                }
            }
        }

        // Component type not found, add it to the map
        rebuild(componentType);
        return getMapper(componentType);
    }

    void applyComponentChanges() {
        for (int i = 0, n = array.length; i < n; ++i) {
            array[i].applyComponentChanges();
        }
    }
}
