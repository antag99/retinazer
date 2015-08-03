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

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.antag99.retinazer.utils.DestroyEvent;
import com.github.antag99.retinazer.utils.GuidComponent;
import com.github.antag99.retinazer.utils.InitializeEvent;
import com.github.antag99.retinazer.utils.UpdateEvent;

/**
 * Immutable copy-on-write engine configuration. This is used to setup static
 * properties of the engine, such as the systems to be processed, and additional
 * dependencies. Typically, an engine is created when the application is loaded,
 * and {@link Engine#reset()} is called before/after each game.
 */
public final class EngineConfig {
    private static final EngineConfig DEFAULT = new EngineConfig()
            .withComponentType(GuidComponent.class)
            .withEventType(Event.class)
            .withEventType(InitializeEvent.class)
            .withEventType(DestroyEvent.class)
            .withEventType(UpdateEvent.class)
            .withWireResolver(new DefaultWireResolver())
            .withWireResolver(new DependencyWireResolver());

    private EngineConfig() {
    }

    private final Map<Class<?>, EntitySystem> systems = new LinkedHashMap<Class<?>, EntitySystem>();
    private final Set<Class<? extends Component>> componentTypes = new LinkedHashSet<Class<? extends Component>>();
    private final Set<Class<? extends Event>> eventTypes = new LinkedHashSet<Class<? extends Event>>();
    private final List<WireResolver> wireResolvers = new ArrayList<>();
    private final Map<Class<?>, Object> dependencies = new HashMap<>();
    private final Iterable<EntitySystem> systemsView = unmodifiableCollection(systems.values());
    private final Iterable<Class<? extends Component>> componentsTypesView = unmodifiableCollection(componentTypes);
    private final Iterable<Class<? extends Event>> eventTypesView = unmodifiableCollection(eventTypes);
    private final Iterable<WireResolver> wireResolversView = unmodifiableCollection(wireResolvers);
    private final Map<Class<?>, Object> dependenciesView = unmodifiableMap(dependencies);

    /**
     * Creates a new engine configuration with the default values.
     *
     * @return The new engine configuration.
     */
    public static EngineConfig create() {
        return DEFAULT.clone();
    }

    /**
     * Gets the system of the given type. Note that only one system of a type
     * can exist in an engine configuration.
     *
     * @param systemClass The type of the system
     * @return The system
     * @throws IllegalArgumentException If the system does not exist
     */
    public EntitySystem getSystem(Class<? extends EntitySystem> systemType) {
        return getSystem(systemType, false);
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
    public EntitySystem getSystem(Class<? extends EntitySystem> systemType, boolean optional) {
        EntitySystem system = systems.get(systemType);
        if (!optional && system == null) {
            throw new IllegalArgumentException("System not registered: " + systemType);
        }
        return system;
    }

    /**
     * Gets the registered systems of this engine configuration
     *
     * @return The registered systems of this engine configuration
     */
    public Iterable<EntitySystem> getSystems() {
        return systemsView;
    }

    /**
     * Gets the registered component types of this engine configuration
     *
     * @return The registered component types of this engine configuration
     */
    public Iterable<Class<? extends Component>> getComponentTypes() {
        return componentsTypesView;
    }

    /**
     * Gets the registered event types of this engine configuration
     *
     * @return The registered event types of this engine configuration.
     */
    public Iterable<Class<? extends Event>> getEventTypes() {
        return eventTypesView;
    }

    /**
     * Gets the registered wire resolvers of this engine configuration.
     *
     * @return The registered wire resolvers of this engine configuration.
     */
    public Iterable<WireResolver> getWireResolvers() {
        return wireResolversView;
    }

    /**
     * Gets the registered dependencies of this engine configuration.
     *
     * @return The registered dependencies of this engine configuration.
     */
    public Map<Class<?>, Object> getDependencies() {
        return dependenciesView;
    }

    /**
     * Registers a system.
     *
     * @param system The system to register.
     * @return A new configuration with the system.
     */
    public EngineConfig withSystem(EntitySystem system) {
        Class<? extends EntitySystem> systemType = system.getClass();
        if (systems.containsKey(systemType)) {
            throw new IllegalArgumentException(
                    "System of type " + systemType.getName() + " has already been registered");
        }
        EngineConfig config = clone();
        config.systems.put(systemType, system);
        return config;
    }

    /**
     * Registers a component type.
     *
     * @param componentType The component type to register.
     * @return A new configuration with the component type.
     */
    public EngineConfig withComponentType(Class<? extends Component> componentType) {
        if (componentTypes.contains(componentType)) {
            throw new IllegalArgumentException(
                    "Component of type " + componentType.getName() + " has already been registered");
        }
        EngineConfig config = clone();
        config.componentTypes.add(componentType);
        return config;
    }

    /**
     * Registers an event type.
     *
     * @param eventType The event type to register.
     * @return A new configuration with the event type.
     */
    public EngineConfig withEventType(Class<? extends Event> eventType) {
        if (eventTypes.contains(eventType)) {
            throw new IllegalArgumentException(
                    "Event of type " + eventType.getName() + " has already been registered");
        }
        EngineConfig config = clone();
        config.eventTypes.add(eventType);
        return config;
    }

    /**
     * Registers a wire resolver.
     *
     * @param resolver The resolver to register.
     * @return A new configuration with the wire resolver.
     */
    public EngineConfig withWireResolver(WireResolver resolver) {
        EngineConfig config = clone();
        config.wireResolvers.add(resolver);
        return config;
    }

    /**
     * Registers a dependency, with the concrete type of the object.
     *
     * @param dependency The dependency to register.
     * @return New configuration with the dependency.
     */
    public EngineConfig withDependency(Object dependency) {
        EngineConfig config = clone();
        config.dependencies.put(dependency.getClass(), dependency);
        return config;
    }

    /**
     * Registers a dependency of the given type.
     *
     * @param type Type of the dependency.
     * @param dependency The dependency.
     * @return New configuration with the given dependency.
     */
    public <T> EngineConfig withDependency(Class<T> type, T dependency) {
        EngineConfig config = clone();
        config.dependencies.put(type, type.cast(dependency));
        return config;
    }

    @Override
    protected EngineConfig clone() {
        EngineConfig config = new EngineConfig();
        config.systems.putAll(systems);
        config.componentTypes.addAll(componentTypes);
        config.eventTypes.addAll(eventTypes);
        config.wireResolvers.addAll(wireResolvers);
        config.dependencies.putAll(dependencies);
        return config;
    }

    /**
     * Creates a new {@link Engine} instance based on this configuration
     *
     * @return The new {@link Engine} instance.
     */
    public Engine finish() {
        return new Engine(this);
    }
}
