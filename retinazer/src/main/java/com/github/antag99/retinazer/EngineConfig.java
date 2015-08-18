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

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterable;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Stores configuration for an {@link Engine} instance.
 */
public final class EngineConfig {

    /**
     * Creates a new engine configuration with the default values.
     */
    public EngineConfig() {
        wireResolvers.add(new DefaultWireResolver());
        wireResolvers.add(new MapperWireResolver());
    }

    private ObjectMap<Class<?>, EntitySystem> systems = new ObjectMap<Class<?>, EntitySystem>();
    private ObjectMap.Values<EntitySystem> systemsView = new ObjectMap.Values<EntitySystem>(systems) {
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    };
    private Array<WireResolver> wireResolvers = new Array<>();
    private ArrayIterable<WireResolver> wireResolversView = new ArrayIterable<>(wireResolvers, false);

    /**
     * Gets the system of the given type. Note that only one system of a type
     * can exist in an engine configuration.
     *
     * @param systemType
     *            type of the system.
     * @return the system.
     * @throws IllegalArgumentException
     *             if the system does not exist.
     */
    public EntitySystem getSystem(Class<? extends EntitySystem> systemType) {
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
     * @return the system, or {@code null} if {@code optional} is {@code true}
     *         and the system does not exist.
     * @throws IllegalArgumentException
     *             if {@code optional} is {@code false} and the system does not
     *             exist.
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
     * @return the registered systems of this engine configuration
     */
    public Iterable<EntitySystem> getSystems() {
        systemsView.reset();
        return systemsView;
    }

    /**
     * Gets the registered wire resolvers of this engine configuration.
     *
     * @return the registered wire resolvers of this engine configuration.
     */
    public Iterable<WireResolver> getWireResolvers() {
        return wireResolversView;
    }

    /**
     * Registers a system.
     *
     * @param system
     *            system to register.
     * @return {@code this} for chaining.
     */
    public EngineConfig addSystem(EntitySystem system) {
        Class<? extends EntitySystem> systemType = system.getClass();
        if (systems.containsKey(systemType)) {
            throw new IllegalArgumentException(
                    "System of type " + systemType.getName() + " has already been registered");
        }
        systems.put(systemType, system);
        return this;
    }

    /**
     * Registers a wire resolver.
     *
     * @param resolver
     *            resolver to register.
     * @return {@code this} for chaining.
     */
    public EngineConfig addWireResolver(WireResolver resolver) {
        wireResolvers.add(resolver);
        return this;
    }
}
