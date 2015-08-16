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
import java.util.List;

public final class Engine {
    private final EntitySystem[] systems;

    EngineConfig config;
    EntityManager entityManager;
    ComponentManager componentManager;
    FamilyManager familyManager;
    WireManager wireManager;

    /** Tracks whether any components or entities have been modified; reset at every call to flush() */
    boolean dirty = false;

    Engine(EngineConfig config) {
        this.config = config;

        entityManager = new EntityManager(this);
        componentManager = new ComponentManager(this);
        familyManager = new FamilyManager(this);
        wireManager = new WireManager(this);

        List<EntitySystem> systems = new ArrayList<EntitySystem>();
        systems.addAll((Collection<? extends EntitySystem>) config.getSystems());
        this.systems = systems.toArray(new EntitySystem[0]);

        for (EntitySystem system : systems)
            wire(system);

        for (EntitySystem system : systems)
            system.setEngine(this);

        for (EntitySystem system : systems)
            system.initialize();
    }

    public EngineConfig getConfig() {
        return config;
    }

    public void wire(Object object) {
        wireManager.wire(object);
    }

    public void unwire(Object object) {
        wireManager.unwire(object);
    }

    public void update() {
        while (dirty) {
            flush();
        }
        for (EntitySystem system : systems) {
            system.update();
            while (dirty) {
                flush();
            }
        }
    }

    private void flush() {
        dirty = false;
        entityManager.entities.andNot(entityManager.removeEntities);
        entityManager.removeEntities.clear();
        familyManager.updateFamilyMembership();
        componentManager.applyComponentChanges();
    }

    /**
     * Creates a new entity. This entity will be assigned a index, which is not
     * shared with any existing entity. Note that indices are reused once the
     * entity is no longer active. The entity is immediately inserted into the
     * engine, but it won't show up in entity sets until the next call to {@link #flush()}.
     *
     * @return reused handle for accessing the entity; don't hold on to this as
     *         it will be invalid once the next entity is created.
     */
    public Handle createEntity() {
        return entityManager.createEntity();
    }

    /**
     * Creates a handle for accessing the components of entities. Note that
     * this handle can be reused by changing the index it points to; it is
     * recommended to keep or pool handles in order to avoid garbage collection.
     *
     * @return an entity handle associated with this engine.
     */
    public Handle createHandle() {
        return new Handle(this);
    }

    /**
     * Creates a handle initially set to the given entity.
     *
     * @param index
     *            index of the entity.
     * @return handle for accessing components.
     * @see #createHandle()
     */
    public Handle createHandle(int index) {
        Handle handle = createHandle();
        handle.setEntity(index);
        return handle;
    }

    /**
     * Destroys the entity with the given index.
     *
     * @param entity
     *            the entity to destroy.
     */
    public void destroyEntity(int entity) {
        entityManager.destroyEntity(entity);
    }

    /**
     * <p>
     * Gets all entities added to this engine.
     * </p>
     *
     * @return {@link EntitySet} containing all entities added to this engine
     */
    public EntitySet getEntities() {
        return familyManager.getEntities();
    }

    /**
     * <p>
     * Gets all entities matching the given family that has been added to
     * this engine.
     * </p>
     *
     * @param family
     *            the family
     * @return {@link EntitySet} containing all entities added to this engine
     *         that matches the given family.
     */
    public EntitySet getEntitiesFor(FamilyConfig family) {
        return familyManager.getEntitiesFor(family);
    }

    /**
     * <p>
     * Gets or creates a family for the given configuration
     * </p>
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
     * @param systemClass
     *            type of the system
     * @return the system
     * @throws IllegalArgumentException
     *             if the system does not exist
     */
    public <T extends EntitySystem> T getSystem(Class<T> systemClass) {
        return getSystem(systemClass, false);
    }

    /**
     * Gets the system of the given type. Note that only one system of a type
     * can exist in an engine configuration.
     *
     * @param systemClass type of the system
     * @param optional whether to return {@code null} if the system does not exist
     * @return the system, or {@code null} if {@code optional} is {@code true}
     *         and the system does not exist.
     * @throws IllegalArgumentException
     *             if {@code optional} is {@code false} and the system does not exist.
     */
    // TODO: Use a map instead of linear search
    @SuppressWarnings("unchecked")
    public <T extends EntitySystem> T getSystem(Class<T> systemClass, boolean optional) {
        for (int i = 0, n = systems.length; i < n; i++)
            if (systems[i].getClass() == systemClass)
                return (T) systems[i];

        if (!optional) {
            throw new IllegalArgumentException("System not registered: " + systemClass.getName());
        } else {
            return null;
        }
    }

    /**
     * Gets the systems registered during configuration of the engine.
     *
     * @return all systems registered during configuration of the engine.
     */
    public Iterable<EntitySystem> getSystems() {
        return config.getSystems();
    }

    public <T extends Component> Mapper<T> getMapper(Class<T> componentType) {
        return componentManager.getMapper(componentType);
    }
}
