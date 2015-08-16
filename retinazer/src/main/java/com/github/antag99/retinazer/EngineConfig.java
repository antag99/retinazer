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
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.utils.reflect.ClassReflection;

/**
 * Immutable copy-on-write engine configuration. This is used to setup static
 * properties of the engine, such as the systems to be processed, and additional
 * dependencies. Typically, an engine instance is created per game.
 */
public final class EngineConfig {
    private static final EngineConfig DEFAULT = new EngineConfig()
            .withWireResolver(new DefaultWireResolver())
            .withWireResolver(new DependencyWireResolver())
            .withWireResolver(new MapperWireResolver());

    private EngineConfig() {
    }

    private final Map<Class<?>, EntitySystem> systems = new LinkedHashMap<Class<?>, EntitySystem>();
    private final List<WireResolver> wireResolvers = new ArrayList<>();
    private final Map<Class<?>, Object> dependencies = new HashMap<>();
    private final Iterable<EntitySystem> systemsView = unmodifiableCollection(systems.values());
    private final Iterable<WireResolver> wireResolversView = unmodifiableCollection(wireResolvers);
    private final Map<Class<?>, Object> dependenciesView = unmodifiableMap(dependencies);

    /**
     * Creates a new engine configuration with the default values.
     *
     * @return The new engine configuration.
     */
    public static EngineConfig create() {
        return DEFAULT.copy();
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
        EngineConfig config = copy();
        config.systems.put(systemType, system);
        return config;
    }

    /**
     * Registers a wire resolver.
     *
     * @param resolver The resolver to register.
     * @return A new configuration with the wire resolver.
     */
    public EngineConfig withWireResolver(WireResolver resolver) {
        EngineConfig config = copy();
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
        EngineConfig config = copy();
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
        if (!ClassReflection.isInstance(type, dependency)) {
            throw new ClassCastException("Cannot cast " + dependency.getClass()
                    .getName() + " to " + type.getClass().getName());
        }
        EngineConfig config = copy();
        config.dependencies.put(type, dependency);
        return config;
    }

    private EngineConfig copy() {
        EngineConfig config = new EngineConfig();
        config.systems.putAll(systems);
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
