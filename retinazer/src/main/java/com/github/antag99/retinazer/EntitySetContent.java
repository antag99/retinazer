package com.github.antag99.retinazer;

import com.github.antag99.retinazer.utils.Mask;

final class EntitySetContent {
    // Engine the entities belong to
    public final Engine engine;
    // Mask updated by FamilyManager
    public final Mask entities = new Mask();
    // Modification count, for invalidating caches
    public int modCount = 0;
    // Default entity set
    public EntitySet defaultSet;

    public EntitySetContent(Engine engine) {
        this.engine = engine;
        this.defaultSet = new EntitySet(this);
    }
}
