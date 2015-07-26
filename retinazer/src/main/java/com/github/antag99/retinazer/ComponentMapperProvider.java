package com.github.antag99.retinazer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class ComponentMapperProvider implements DependencyProvider {
    @Override
    public Object getDependency(Field field, Object consumer, Engine engine) {
        if (field.getType() != ComponentMapper.class)
            return null;
        Type param = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        return engine.getSystem(ComponentManager.class)
                .getMapper(((Class<?>) param).asSubclass(Component.class));
    }
}
