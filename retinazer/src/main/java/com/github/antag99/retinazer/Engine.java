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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.antag99.retinazer.utils.Bag;
import com.github.antag99.retinazer.utils.DestroyEvent;
import com.github.antag99.retinazer.utils.Experimental;
import com.github.antag99.retinazer.utils.GuidComponent;
import com.github.antag99.retinazer.utils.InitializeEvent;
import com.github.antag99.retinazer.utils.Inject;
import com.github.antag99.retinazer.utils.Mask;
import com.github.antag99.retinazer.utils.UpdateEvent;

public final class Engine {
    private final EntitySystem[] systems;

    Pool<Mask> maskPool = new Pool<Mask>() {
        @Override
        protected Mask create() {
            return new Mask();
        }

        @Override
        protected void destroy(Mask object) {
            object.clear();
        }
    };

    EngineConfig config;
    EntityManager entityManager;
    ComponentManager componentManager;
    FamilyManager familyManager;
    EventManager eventManager;
    GuidManager guidManager;
    ComponentStorage<GuidComponent> guidStorage;

    Engine(EngineConfig config) {
        this.config = config;
        List<EntitySystem> systems = new ArrayList<EntitySystem>();
        systems.add(entityManager = new EntityManager(this));
        systems.add(componentManager = new ComponentManager(this));
        systems.add(familyManager = new FamilyManager(this));
        systems.add(eventManager = new EventManager(this));
        systems.add(guidManager = new GuidManager(this));
        systems.addAll((Collection<? extends EntitySystem>) config.getSystems());
        this.systems = systems.toArray(new EntitySystem[0]);
        guidStorage = componentManager.getStorage(GuidComponent.class);
        initialize();
    }

    public EngineConfig getConfig() {
        return config;
    }

    private void initialize() {
        for (int i = 0, n = systems.length; i < n; i++) {
            injectDependencies(systems[i]);
        }
        for (int i = 0, n = systems.length; i < n; i++) {
            addEventListener(systems[i]);
        }
        familyManager.addEntityListener(Family.with(GuidComponent.class), guidManager);
        dispatchEvent(new InitializeEvent());
    }

    private void destroy() {
        for (Entity entity : getEntities())
            entity.destroy();
        entityManager.applyEntityRemovals();
        dispatchEvent(new DestroyEvent());
        for (int i = 0, n = systems.length; i < n; i++) {
            removeEventListener(systems[i]);
        }
        for (int i = 0, n = systems.length; i < n; i++) {
            uninjectDependencies(systems[i]);
        }
        entityManager.reset();
        guidManager.reset();
        eventManager.reset();
        familyManager.reset();
    }

    /**
     * Resets this engine. This does the following:
     * <ul>
     * <li>Destroys and removes all entities</li>
     * <li>Calls {@link EntitySystem#destroy()} for each system</li>
     * <li>Removes all event listeners</li>
     * <li>Calls {@link #uninjectDependencies(Object)} with each system</li>
     * <li>Removes all remaining entities (no notifications here)</li>
     * <li>Calls {@link #injectDependencies(Object)} with each system</li>
     * <li>Adds all systems as event listeners</li>
     * <li>Calls {@link EntitySystem#initialize()} for each system</li>
     * </ul>
     *
     * After this method has been called, the engine should be in it's initial
     * state, same as right after it was created. (This can, however, be
     * affected by improper implementations of registered systems.)
     */
    public void reset() {
        destroy();
        initialize();
    }

    public void injectDependencies(Object consumer) {
        Class<?> type = consumer.getClass();

        for (Field field : Internal.getAllFields(type)) {
            Inject inject = field.getAnnotation(Inject.class);

            if (inject != null) {
                Object dependency = null;

                for (DependencyProvider provider : config.getDependencyProviders()) {
                    dependency = provider.getDependency(field, consumer, this);
                    if (dependency != null) {
                        break;
                    }
                }

                if (dependency == null) {
                    throw new IllegalArgumentException("Missing dependency for field " + field.getName());
                }

                try {
                    field.setAccessible(true);
                    field.set(consumer, dependency);
                } catch (IllegalAccessException ex) {
                    throw new AssertionError(ex);
                }
            }
        }
    }

    public void uninjectDependencies(Object consumer) {
        Class<?> type = consumer.getClass();

        for (Field field : Internal.getAllFields(type)) {
            Inject inject = field.getAnnotation(Inject.class);

            if (inject != null) {
                try {
                    field.setAccessible(true);
                    field.set(consumer, null);
                } catch (IllegalAccessException ex) {
                    throw new AssertionError(ex);
                }
            }
        }
    }

    /**
     * <p>
     * Updates this engine. This does the following:
     * <ul>
     * <li>Fires an {@link UpdateEvent} to be handled by interested systems</li>
     * <li>Adds newly created entities</li>
     * <li>Applies pending component operations</li>
     * <li>Removes deleted entities</li>
     * </ul>
     * </p>
     */
    public void update() {
        dispatchEvent(new UpdateEvent());
        entityManager.applyEntityAdditions();
        componentManager.applyComponentChanges();
        entityManager.applyEntityRemovals();
    }

    /**
     * <p>
     * Creates a new entity. This entity will be assigned a index, which is not
     * shared with any existing entity. Note that no guid is assigned to this
     * entity; the {@link #createEntity(long)} method should be used if a guid
     * is needed (typically, networked entities have a guid whereas local entities
     * have not).
     * </p>
     *
     * <p>
     * All created entities are not added until the end of the current
     * {@link #update()} call. This is done to avoid issues with modifying
     * entities while iterating over them.
     * </p>
     *
     * @return The new entity instance
     */
    public Entity createEntity() {
        return entityManager.createEntity();
    }

    /**
     * <p>
     * Creates a new entity. This entity will be assigned a index, which is not
     * shared with any existing entity. An {@link GuidComponent} will be added
     * to the entity containing the passed guid; the guid-related operations
     * can then be used with the entity.
     * </p>
     *
     * <p>
     * All created entities are not added until the end of the current
     * {@link #update()} call. This is done to avoid issues with modifying
     * entities while iterating over them.
     * </p>
     *
     * @param guid
     *            The guid to set for the entity
     * @return The new entity instance
     */
    public Entity createEntity(long guid) {
        return entityManager.createEntity(guid);
    }

    /**
     * <p>
     * Gets the entity with the given index.
     * </p>
     *
     * @param index
     *            The index of the entity.
     * @return The entity with the given index.
     * @throws IllegalArgumentException
     *             If the entity does not exist, or hasn't been added yet.
     */
    public Entity getEntityForIndex(int index) {
        return entityManager.getEntityForIndex(index);
    }

    /**
     * <p>
     * Gets the entity with the given guid. Returns <code>null</code> if there
     * is no entity with the given guid, if it has not been added yet, or it's
     * {@link GuidComponent} has not been added yet.
     * </p>
     *
     * @param guid
     *            The guid of the entity
     * @return The entity with the given guid.
     */
    public Entity getEntityForGuid(long guid) {
        return guidManager.getEntityForGuid(guid);
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
     *            The family
     * @return {@link EntitySet} containing all entities added to this engine
     *         that matches the given family.
     */
    public EntitySet getEntitiesFor(FamilyConfig family) {
        return familyManager.getEntitiesFor(family);
    }

    /**
     * <p>
     * Gets or creates a matcher for the given family configuration
     * </p>
     *
     * @param config
     *            Configuration for the family to match
     * @return Matcher for the given family configuration
     */
    public FamilyMatcher getMatcher(FamilyConfig config) {
        return familyManager.getFamily(config);
    }

    /**
     * Gets the system of the given type. Note that only one system of a type
     * can exist in an engine configuration.
     *
     * @param systemClass The type of the system
     * @return The system
     * @throws IllegalArgumentException If the system does not exist
     */
    public <T extends EntitySystem> T getSystem(Class<T> systemClass) {
        return getSystem(systemClass, false);
    }

    /**
     * Gets the system of the given type. Note that only one system of a type
     * can exist in an engine configuration.
     *
     * @param systemClass The type of the system
     * @param optional Whether to return {@code null} if the system does not exist
     * @return The system, or {@code null} if {@code optional} is {@code true} and
     *         the system does not exist.
     * @throws IllegalArgumentException If {@code optional} is {@code false} and
     *             the system does not exist.
     */
    public <T extends EntitySystem> T getSystem(Class<T> systemClass, boolean optional) {
        for (int i = 0, n = systems.length; i < n; i++)
            if (systems[i].getClass() == systemClass)
                return systemClass.cast(systems[i]);

        if (!optional) {
            throw new IllegalArgumentException("System not registered: " + systemClass.getName());
        } else {
            return null;
        }
    }

    /**
     * Gets the {@link Bag} storing the components of the given type.
     *
     * @param componentType The component type.
     * @return The components of the given type.
     */
    @Experimental
    public <T extends Component> Bag<T> getComponentsAsBag(Class<T> componentType) {
        return componentManager.getStorage(componentType).components;
    }

    /**
     * Gets the systems registered during configuration of the engine
     */
    public Iterable<EntitySystem> getSystems() {
        return config.getSystems();
    }

    /**
     * Gets the component types registered during configuration of the engine
     */
    public Iterable<Class<? extends Component>> getComponentTypes() {
        return config.getComponentTypes();
    }

    /**
     * Gets the event types registered during configuration of the engine
     */
    public Iterable<Class<? extends Event>> getEventTypes() {
        return config.getEventTypes();
    }

    /**
     * Adds the given entity listener, which will be invoked every time an
     * entity is added or removed to/from the engine.
     *
     * @param listener The listener to add
     */
    public void addEntityListener(EntityListener listener) {
        familyManager.addEntityListener(listener);
    }

    /**
     * Adds the given entity listener, which will be invoked every time an
     * entity is added/removed to/from the set of entities matched by the
     * given family; this happens both when an entity's components are changed
     * and when it's destroyed.
     *
     * @param family The family
     * @param listener The listener
     */
    // TODO: Does this belong to EntitySet?
    public void addEntityListener(FamilyConfig family, EntityListener listener) {
        familyManager.addEntityListener(family, listener);
    }

    /**
     * Removes the given entity listener added using {@link #addEntityListener(EntityListener)}
     * or {@link #addEntityListener(FamilyConfig, EntityListener)}.
     *
     * @param listener The listener to remove
     */
    public void removeEntityListener(EntityListener listener) {
        familyManager.removeEntityListener(listener);
    }

    /**
     * Dispatches the given event to all registered listeners.
     *
     * @param event
     *            The event to dispatch.
     */
    public void dispatchEvent(Event event) {
        eventManager.dispatchEvent(event);
    }

    /**
     * Adds the given event listener and registers all it's handlers
     *
     * @param listener
     *            The listener to add.
     */
    public void addEventListener(EventListener listener) {
        eventManager.addEventListener(listener);
    }

    /**
     * Removes the given event listeners and unregisters all it's handlers
     *
     * @param listener
     *            The listener to remove.
     */
    public void removeEventListener(EventListener listener) {
        eventManager.removeEventListener(listener);
    }
}
