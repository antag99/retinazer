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

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;

/**
 * Configuration for {@link DependencyResolver}.
 */
public final class DependencyConfig {
    ObjectMap<Class<?>, Object> dependencies = new ObjectMap<>();

    /**
     * Registers a dependency, with the concrete type of the given object.
     *
     * @param dependency
     *            dependency to register.
     * @return {@code this} for chaining.
     */
    public DependencyConfig addDependency(Object dependency) {
        dependencies.put(dependency.getClass(), dependency);
        return this;
    }

    /**
     * Registers a dependency of the given type.
     *
     * @param type
     *            type of the dependency.
     * @param dependency
     *            the dependency.
     * @param <T>
     *            generic type of the dependency.
     * @return {@code this} for chaining.
     */
    public <T> DependencyConfig addDependency(Class<T> type, T dependency) {
        if (!ClassReflection.isInstance(type, dependency)) {
            throw new ClassCastException("Cannot cast " + dependency.getClass()
                    .getName() + " to " + type.getClass().getName());
        }
        dependencies.put(type, dependency);
        return this;
    }
}
