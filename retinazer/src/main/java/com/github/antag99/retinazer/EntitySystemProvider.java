package com.github.antag99.retinazer;

import java.lang.reflect.Field;

public final class EntitySystemProvider implements DependencyProvider {
    @Override
    public Object getDependency(Field field, Object consumer, Engine engine) {
        if (!EntitySystem.class.isAssignableFrom(field.getType()))
            return null;
        return engine.getSystem(field.getType().asSubclass(EntitySystem.class), true);
    }
}
