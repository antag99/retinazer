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

import java.util.HashMap;
import java.util.Map;

final class WireManager {
    private Engine engine;
    private WireResolver[] wireResolvers;
    private Map<Class<?>, WireCache> wireCaches;

    public WireManager(Engine engine, EngineConfig config) {
        this.engine = engine;
        this.wireResolvers = config.wireResolvers.toArray(new WireResolver[0]);
        this.wireCaches = new HashMap<>();
    }

    private WireCache getCache(Class<?> type) {
        WireCache cache = wireCaches.get(type);
        if (cache == null) {
            wireCaches.put(type, cache =
                    new WireCache(engine, type, wireResolvers));
        }
        return cache;
    }

    public void wire(Object object) {
        if (object == null) {
            throw new NullPointerException("object must not be null");
        }

        getCache(object.getClass()).wire(object);
    }

    public void unwire(Object object) {
        if (object == null) {
            throw new NullPointerException("object must not be null");
        }

        getCache(object.getClass()).unwire(object);
    }
}
