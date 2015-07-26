package com.github.antag99.retinazer;

import java.lang.reflect.Field;

public final class EngineProvider implements DependencyProvider {
    @Override
    public Object getDependency(Field field, Object consumer, Engine engine) {
        if (field.getType() != Engine.class)
            return null;
        return engine;
    }
}
