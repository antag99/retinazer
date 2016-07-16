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
package com.github.antag99.retinazer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    }

    List<EntitySystemRegistration> systems = new ArrayList<>();
    List<WireResolver> wireResolvers = new ArrayList<>();

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

        for (int i = 0, n = systems.size(); i < n; i++) {
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
