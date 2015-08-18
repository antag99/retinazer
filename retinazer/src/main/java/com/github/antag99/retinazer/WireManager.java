package com.github.antag99.retinazer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

final class WireManager {
    private Engine engine;
    private WireResolver[] wireResolvers;
    private ObjectMap<Class<?>, WireCache> wireCaches;

    public WireManager(Engine engine, EngineConfig config) {
        this.engine = engine;
        Array<WireResolver> wireResolvers = new Array<>();
        for (WireResolver wireResolver : config.getWireResolvers())
            wireResolvers.add(wireResolver);
        this.wireResolvers = wireResolvers.toArray(WireResolver.class);
        this.wireCaches = new ObjectMap<>();
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
