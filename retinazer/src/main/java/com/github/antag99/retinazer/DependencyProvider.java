package com.github.antag99.retinazer;

import java.lang.reflect.Field;

import com.github.antag99.retinazer.utils.Inject;

/**
 * DependencyProvider is used for providing dependencies via the {@link Inject} annotation.
 */
public interface DependencyProvider {

    /**
     * Retrieves the dependency for the given consumer.
     *
     * @param field The field holding the dependency
     * @param consumer The owner of the field
     * @param engine The current engine instance
     * @return The dependency, or {@code null} if not found.
     */
    public Object getDependency(Field field, Object consumer, Engine engine);
}
