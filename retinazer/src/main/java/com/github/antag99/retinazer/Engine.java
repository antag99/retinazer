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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.antag99.retinazer.EngineConfig.EntitySystemRegistration;

/**
 * Engine is the core class of retinazer; it manages all active entities,
 * performs system processing and initialization.
 */
public final class Engine {

    private final EntitySystem[] systems;
    private final Map<Class<? extends EntitySystem>, EntitySystem> systemsByType;

    final EntityManager entityManager;
    final ComponentManager componentManager;
    final FamilyManager familyManager;
    final WireManager wireManager;

    /** Tracks whether any components or entities have been modified; reset at every call to flush() */
    boolean dirty = false;
    /** Tracks whether this engine is within a call to update() */
    boolean update = false;

    /**
     * Creates a new {@link Engine} based on the specified configuration. Note
     * that the same configuration should <b>not</b> be reused, as system
     * implementations do not handle being registered to multiple engines.
     *
     * @param config
     *            configuration for this Engine.
     */
    public Engine(EngineConfig config) {
        entityManager = new EntityManager(this, config);
        componentManager = new ComponentManager(this, config);
        familyManager = new FamilyManager(this, config);
        wireManager = new WireManager(this, config);

        List<EntitySystemRegistration> systemRegistrations = new ArrayList<>(config.systems);

        Collections.sort(systemRegistrations, new Comparator<EntitySystemRegistration>() {
            @Override
            public int compare(EntitySystemRegistration o1, EntitySystemRegistration o2) {
                return o1.priority.ordinal() - o2.priority.ordinal();
            }
        });

        EntitySystem[] systems = new EntitySystem[systemRegistrations.size()];
        Map<Class<? extends EntitySystem>, EntitySystem> systemsByType = new HashMap<>();

        for (int i = 0, n = systemRegistrations.size(); i < n; i++) {
            systems[i] = systemRegistrations.get(i).system;
            systemsByType.put(systems[i].getClass(), systems[i]);
        }

        this.systems = systems;
        this.systemsByType = Collections.unmodifiableMap(systemsByType);

        for (EntitySystem system : systems)
            wire(system);

        for (EntitySystem system : systems)
            system.setup();

        for (EntitySystem system : systems)
            system.initialize();

        flush();
    }

    public void wire(Object object) {
        wireManager.wire(object);
    }

    public void unwire(Object object) {
        wireManager.unwire(object);
    }

    public void addEntityListener(EntityListener entityListener) {
        getFamily(new FamilyConfig()).addListener(entityListener);
    }

    public void removeEntityListener(EntityListener entityListener) {
        getFamily(new FamilyConfig()).removeListener(entityListener);
    }

    /**
     * Updates all systems, interleaved by inserting/removing entities to/from
     * entity sets.
     */
    public void update() {
        if (update) {
            throw new IllegalStateException("Cannot nest calls to update()");
        }

        update = true;

        flush();

        for (EntitySystem system : systems) {
            system.update();

            flush();
        }

        update = false;
    }

    /**
     * Resets this engine; this removes all existing entities.
     */
    public void reset() {
        if (update) {
            throw new IllegalStateException("Cannot call reset() within update()");
        }

        update = true;

        flush();

        int[] buffer = getEntities().getIndices().buffer;
        for (int i = 0, n = getEntities().size(); i < n; i++) {
            destroyEntity(buffer[i]);
        }

        flush();

        update = false;
    }

    private void flush() {
        while (dirty) {
            dirty = false;

            entityManager.remove.set(entityManager.removeQueue);
            entityManager.removeQueue.clear();

            for (Mapper<?> mapper : componentManager.array) {
                mapper.removeMask.set(mapper.removeQueueMask);
                mapper.removeMask.or(entityManager.remove);
                mapper.removeQueueMask.clear();
            }

            familyManager.updateFamilyMembership();
            componentManager.applyComponentChanges();

            entityManager.entities.andNot(entityManager.remove);
        }
    }

    /**
     * Creates a new entity. This entity will be assigned a index, which is not
     * shared with any existing entity. Note that indices are reused once the
     * entity is no longer active. The entity is immediately inserted into the
     * engine, but it won't show up in entity sets until the next system processing.
     *
     * @return index of the created entity.
     */
    public int createEntity() {
        return entityManager.createEntity();
    }

    /**
     * Destroys the entity with the given index. This will not remove the entity
     * immediately; only after the current system processing,
     *
     * @param entity
     *            the entity to destroy.
     */
    public void destroyEntity(int entity) {
        entityManager.destroyEntity(entity);
    }

    /**
     * Gets all entities added to this engine.
     *
     * @return {@link EntitySet} containing all entities added to this engine
     */
    public EntitySet getEntities() {
        return familyManager.getEntities();
    }

    /**
     * Gets or creates a family for the given configuration. Typically, it's
     * not necessary to retrieve a family directly, but rather only use
     * {@link FamilyConfig}.
     *
     * @param config
     *            configuration for the family
     * @return family for the given configuration
     */
    public Family getFamily(FamilyConfig config) {
        return familyManager.getFamily(config);
    }

    /**
     * Gets the system of the given type. Note that only one system of a type
     * can exist in an engine configuration.
     *
     * @param systemType
     *            type of the system
     * @param <T>
     *            generic type of the system.
     * @return the system
     * @throws IllegalArgumentException
     *             if the system does not exist
     */
    public <T extends EntitySystem> T getSystem(Class<T> systemType) {
        return getSystem(systemType, false);
    }

    /**
     * Gets the system of the given type. Note that only one system of a type
     * can exist in an engine configuration.
     *
     * @param systemType
     *            type of the system.
     * @param optional
     *            whether to return {@code null} if the system does not exist.
     * @param <T>
     *            generic type of the system.
     * @return the system, or {@code null} if {@code optional} is {@code true}
     *         and the system does not exist.
     * @throws IllegalArgumentException
     *             if {@code optional} is {@code false} and the system does not exist.
     */
    public <T extends EntitySystem> T getSystem(Class<T> systemType, boolean optional) {
        @SuppressWarnings("unchecked")
        T system = (T) systemsByType.get(systemType);

        if (system == null && !optional) {
            throw new IllegalArgumentException("System not registered: " + systemType.getName());
        } else {
            return (T) system;
        }
    }

    /**
     * Gets the systems registered during configuration of the engine.
     *
     * @return all systems registered during configuration of the engine.
     */
    public Iterable<EntitySystem> getSystems() {
        return systemsByType.values();
    }

    /**
     * Gets a {@link Mapper} for accessing components of the specified type.
     *
     * @param componentType
     *            component type.
     * @param <T>
     *            generic type of the component.
     * @return mapper for the specified type.
     */
    public <T extends Component> Mapper<T> getMapper(Class<T> componentType) {
        return componentManager.getMapper(componentType);
    }
}
