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

import java.util.Objects;

import com.badlogic.gdx.utils.Array;

/**
 * Stores configuration for an {@link Engine} instance.
 */
public final class EngineConfig {
    static final class EntitySystemRegistration {
        final EntitySystem system;
        final Priority priority;

        EntitySystemRegistration(EntitySystem system, Priority priority) {
            this.system = system;
            this.priority = priority;
        }
    }

    /**
     * Creates a new engine configuration with the default values.
     */
    public EngineConfig() {
        wireResolvers.add(new DefaultWireResolver());
        wireResolvers.add(new MapperWireResolver());
    }

    Array<EntitySystemRegistration> systems = new Array<>();
    Array<WireResolver> wireResolvers = new Array<>(WireResolver.class);

    /**
     * Registers a system.
     *
     * @param system
     *            system to register.
     * @return {@code this} for chaining.
     */
    public EngineConfig addSystem(EntitySystem system) {
        return addSystem(system, Priority.DEFAULT);
    }

    /**
     * Registers a system.
     *
     * @param system
     *            system to register.
     * @param priority
     *            priority of the system.
     * @return {@code this} for chaining.
     */
    public EngineConfig addSystem(EntitySystem system, Priority priority) {
        Objects.requireNonNull(system, "system cannot be null");
        Objects.requireNonNull(priority, "priority cannot be null");
        Class<? extends EntitySystem> systemType = system.getClass();

        for (int i = 0, n = systems.size; i < n; i++) {
            if (systems.get(i).system.getClass() == systemType) {
                throw new IllegalArgumentException(
                        "System of type " + systemType.getName() + " has already been registered");
            }
        }

        systems.add(new EntitySystemRegistration(system, priority));
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
        Objects.requireNonNull(resolver, "resolver cannot be null");
        wireResolvers.add(resolver);
        return this;
    }
}
