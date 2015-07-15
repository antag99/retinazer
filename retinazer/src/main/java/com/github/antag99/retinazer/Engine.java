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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.github.antag99.retinazer.utils.Inject;
import com.github.antag99.retinazer.utils.UuidComponent;

public final class Engine {
    private List<EntitySystem> systems = new ArrayList<>();
    private float deltaTime;

    EngineConfig config;
    @Inject
    EntityManager entityManager;
    @Inject
    ComponentManager componentManager;
    @Inject
    FamilyManager familyManager;
    @Inject
    EventManager eventManager;
    @Inject
    UuidManager uuidManager;
    @Inject
    ComponentMapper<UuidComponent> uuidMapper;

    Engine(EngineConfig config) {
        this.config = config;
        addSystem(new EntityManager(config));
        addSystem(new ComponentManager(config));
        addSystem(new FamilyManager(config));
        addSystem(new EventManager(config));
        addSystem(new UuidManager(config));
        config.getSystems().forEach(this::addSystem);
        initialize();
    }

    private void addSystem(EntitySystem system) {
        systems.add(system);
        systems.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));
    }

    private void initialize() {
        injectDependencies(this);
        for (int i = 0, n = systems.size(); i < n; ++i) {
            injectDependencies(systems.get(i));
        }
        eventManager.registerEventHandlers();
        familyManager.addEntityListener(Family.with(UuidComponent.class), uuidManager);
        for (int i = 0, n = systems.size(); i < n; ++i) {
            systems.get(i).initialize();
        }
    }

    private void destroy() {
        for (int i = 0, n = systems.size(); i < n; ++i) {
            systems.get(i).destroy();
        }
        for (int i = 0, n = systems.size(); i < n; ++i) {
            uninjectDependencies(systems.get(i));
        }
        entityManager.reset();
        eventManager.reset();
        familyManager.reset();
        uuidManager.reset();
        uninjectDependencies(this);
    }

    public void reset() {
        destroy();
        initialize();
    }

    public void injectDependencies(Object object) {
        Class<?> type = object.getClass();

        for (Field field : Internal.getAllFields(type)) {
            Inject inject = field.getAnnotation(Inject.class);

            if (inject != null) {
                Class<?> dependencyType = field.getType();
                Object dependency = null;

                if (dependencyType == Engine.class) {
                    dependency = this;
                } else if (dependencyType == ComponentMapper.class) {
                    Type param = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    dependency = getSystem(ComponentManager.class)
                            .getMapper(((Class<?>) param).asSubclass(Component.class));
                } else if (EntitySystem.class.isAssignableFrom(dependencyType)) {
                    dependency = getSystem(field.getType().asSubclass(EntitySystem.class));
                } else {
                    for (Object candidate : config.getDependencies()) {
                        if (dependencyType.isInstance(candidate)) {
                            dependency = candidate;
                            break;
                        }
                    }
                }

                if (dependency == null) {
                    throw new IllegalArgumentException("Missing dependency for field " + field.getName());
                }

                try {
                    field.setAccessible(true);
                    field.set(object, dependency);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new AssertionError(ex);
                }
            }
        }
    }

    public void uninjectDependencies(Object object) {
        Class<?> type = object.getClass();

        for (Field field : Internal.getAllFields(type)) {
            Inject inject = field.getAnnotation(Inject.class);

            if (inject != null) {
                try {
                    field.setAccessible(true);
                    field.set(object, null);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new AssertionError(ex);
                }
            }
        }
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }

    public <T extends Component> ComponentMapper<T> getMapper(Class<T> componentType) {
        return componentManager.getMapper(componentType);
    }

    public Entity createEntity() {
        return entityManager.createEntity();
    }

    public Entity createEntity(long uuid) {
        return entityManager.createEntity(uuid);
    }

    public Entity getEntityForIndex(int index) {
        return entityManager.getEntityForIndex(index);
    }

    public Entity getEntityForUuid(long uuid) {
        return uuidManager.getEntityForUuid(uuid);
    }

    public EntitySet getEntities() {
        return familyManager.getEntities();
    }

    public EntitySet getEntitiesFor(FamilyConfig family) {
        return familyManager.getEntitiesFor(family);
    }

    public Family getFamily(FamilyConfig config) {
        return familyManager.getFamily(config);
    }

    public <T extends EntitySystem> T getSystem(Class<T> systemClass) {
        for (int i = 0, n = systems.size(); i < n; i++)
            if (systems.get(i).getClass() == systemClass)
                return systemClass.cast(systems.get(i));
        throw new IllegalArgumentException("System not registered: " + systemClass.getName());
    }

    public Iterable<EntitySystem> getSystems() {
        return config.getSystems();
    }

    public Iterable<Class<? extends Component>> getComponentTypes() {
        return config.getComponentTypes();
    }

    public Iterable<Class<? extends Event>> getEventTypes() {
        return config.getEventTypes();
    }

    public void addEntityListener(EntityListener listener) {
        familyManager.addEntityListener(listener);
    }

    public void addEntityListener(FamilyConfig family, EntityListener listener) {
        familyManager.addEntityListener(family, listener);
    }

    public void removeEntityListener(EntityListener listener) {
        familyManager.removeEntityListener(listener);
    }

    public void update() {
        for (int i = 0, n = systems.size(); i < n; ++i)
            systems.get(i).beforeUpdate();
        for (int i = 0, n = systems.size(); i < n; ++i)
            systems.get(i).update();
        for (int i = 0, n = systems.size(); i < n; ++i)
            systems.get(i).afterUpdate();
        entityManager.applyEntityAdditions();
        componentManager.applyComponentChanges();
        entityManager.applyEntityRemovals();
    }

    public <T extends Event> void addEventListener(Class<T> eventClass, FamilyConfig family, int priority,
            EventListener<? super T> listener) {
        eventManager.addEventListener(eventClass, getFamily(family), priority, listener);
    }

    public void removeEventListener(EventListener<?> listener) {
        eventManager.removeEventListener(listener);
    }
}
