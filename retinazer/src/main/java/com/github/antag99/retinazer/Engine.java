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
import java.util.Collection;
import java.util.List;

import com.github.antag99.retinazer.utils.Inject;
import com.github.antag99.retinazer.utils.GuidComponent;

public final class Engine {
    private final EntitySystem[] systems;
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
    GuidManager guidManager;
    @Inject
    ComponentMapper<GuidComponent> guidMapper;

    Engine(EngineConfig config) {
        this.config = config;
        List<EntitySystem> systems = new ArrayList<EntitySystem>();
        systems.add(new EntityManager(config));
        systems.add(new ComponentManager(config));
        systems.add(new FamilyManager(config));
        systems.add(new EventManager(config));
        systems.add(new GuidManager(config));
        systems.addAll((Collection<? extends EntitySystem>) config.getSystems());
        this.systems = systems.toArray(new EntitySystem[0]);
        initialize();
    }

    public EngineConfig getConfig() {
        return config;
    }

    private void initialize() {
        injectDependencies(this);
        for (int i = 0, n = systems.length; i < n; ++i) {
            injectDependencies(systems[i]);
        }
        eventManager.registerEventHandlers();
        familyManager.addEntityListener(Family.with(GuidComponent.class), guidManager);
        for (int i = 0, n = systems.length; i < n; ++i) {
            systems[i].initialize();
        }
    }

    private void destroy() {
        for (Entity entity : getEntities())
            entity.destroy();
        entityManager.applyEntityRemovals();
        for (int i = 0, n = systems.length; i < n; ++i) {
            systems[i].destroy();
        }
        for (int i = 0, n = systems.length; i < n; ++i) {
            uninjectDependencies(systems[i]);
        }
        entityManager.reset();
        eventManager.reset();
        familyManager.reset();
        guidManager.reset();
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
                } else if (dependencyType.isPrimitive()) {
                    throw new IllegalArgumentException("Invalid dependency type: " + dependencyType.getName());
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
                } catch (IllegalAccessException ex) {
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
                } catch (IllegalAccessException ex) {
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

    public Entity createEntity(long guid) {
        return entityManager.createEntity(guid);
    }

    public Entity getEntityForIndex(int index) {
        return entityManager.getEntityForIndex(index);
    }

    public Entity getEntityForGuid(long guid) {
        return guidManager.getEntityForGuid(guid);
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
        for (int i = 0, n = systems.length; i < n; i++)
            if (systems[i].getClass() == systemClass)
                return systemClass.cast(systems[i]);
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
        for (int i = 0, n = systems.length; i < n; ++i)
            systems[i].beforeUpdate();
        for (int i = 0, n = systems.length; i < n; ++i)
            systems[i].update();
        for (int i = 0, n = systems.length; i < n; ++i)
            systems[i].afterUpdate();
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
