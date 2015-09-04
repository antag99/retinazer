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

/**
 * <p>
 * Handles are used for accessing the composition (components) of an entity. Each
 * handle internally manages an {@link Engine} reference along with an entity
 * index, which can be retrieved via {@link #idx()} and changed via {@link #idx(int)}.
 * </p>
 *
 * <p>
 * To create a handle, one of the {@link Engine#createHandle()} and
 * {@link Engine#createHandle(int)} methods can be used.
 * </p>
 *
 * <p>
 * Note that in most cases, using a plain {@code int} should be preferred, as
 * long as an {@link Engine} instance is available. Otherwise, it may be convenient
 * to use {@link Handle} rather than supplying an {@link Engine} instance along
 * with an {@code int}.
 * </p>
 */
public final class Handle {
    private Engine engine;
    private int index = -1;

    Handle(Engine engine) {
        this.engine = engine;
    }

    /**
     * Gets the engine instance tied to this handle.
     *
     * @return the engine instance tied to this handle.
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * Sets the entity referenced by this handle.
     *
     * @param index
     *            index of the entity referenced by this handle.
     * @return this handle for chaining.
     */
    public Handle idx(int index) {
        this.index = index;
        return this;
    }

    /**
     * Gets the entity referenced by this handle.
     *
     * @return index of the entity referenced by this handle.
     */
    public int idx() {
        return index;
    }

    /**
     * Duplicates this handle; returns a new instance pointing to the same entity.
     *
     * @return a copy of this handle.
     */
    public Handle cpy() {
        Handle handle = engine.createHandle();
        handle.index = index;
        return handle;
    }

    /**
     * Creates a component of the given type and adds it to the referenced entity.
     *
     * @param componentType
     *            type of the component to create.
     * @param <T>
     *            generic component type.
     * @return the component instance.
     */
    public <T extends Component> T create(Class<T> componentType) {
        return engine.componentManager.getMapper(componentType).create(index);
    }

    /**
     * Adds an existing component instance to the referenced entity. Note that
     * the {@link #create(Class)} methods should be preferred in most cases.
     *
     * @param component
     *            the component to add.
     * @param <T>
     *            generic component type.
     * @return this handle for chaining.
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> Handle add(T component) {
        ((Mapper<T>) engine.componentManager.getMapper(component.getClass()))
                .add(index, component);
        return this;
    }

    /**
     * Removes the component of the given type from the referenced entity.
     *
     * @param componentType
     *            the type of component to remove.
     * @param <T>
     *            generic component type.
     * @return this handle for chaining.
     */
    public <T extends Component> Handle remove(Class<T> componentType) {
        engine.componentManager.getMapper(componentType).remove(index);
        return this;
    }

    /**
     * Retrieves the component of the given type from the referenced entity.
     *
     * @param componentType
     *            the type of component to retrieve.
     * @param <T>
     *            generic component type.
     * @return the component; may be {@code null}.
     */
    public <T extends Component> T get(Class<T> componentType) {
        return engine.componentManager.getMapper(componentType).get(index);
    }

    /**
     * Checks whether the referenced entity has the component of the given type.
     *
     * @param componentType
     *            the type of component to check for.
     * @param <T>
     *            generic component type.
     * @return whether the entity has the component of the given type.
     */
    public <T extends Component> boolean has(Class<T> componentType) {
        return engine.componentManager.getMapper(componentType).has(index);
    }

    /**
     * Destroys the referenced entity.
     */
    public void destroy() {
        engine.destroyEntity(index);
    }
}
