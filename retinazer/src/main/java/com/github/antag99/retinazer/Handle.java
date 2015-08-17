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
 * Handle for modifying the components of an entity.
 */
public final class Handle {
    private Engine engine;
    private int entity = -1;

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
     * @param entity
     *            index of the entity referenced by this handle.
     * @return this handle for chaining.
     */
    public Handle setEntity(int entity) {
        this.entity = entity;
        return this;
    }

    /**
     * Gets the entity referenced by this handle.
     *
     * @return index of the entity referenced by this handle.
     */
    public int getEntity() {
        return entity;
    }

    /**
     * Duplicates this handle; returns a new instance pointing to the same entity.
     *
     * @return a copy of this handle.
     */
    public Handle duplicate() {
        Handle handle = engine.createHandle();
        handle.entity = entity;
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
        return engine.componentManager.getMapper(componentType).create(entity);
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
                .add(entity, component);
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
        ((Mapper<T>) engine.componentManager.getMapper(componentType))
                .remove(entity);
        return this;
    }

    /**
     * Destroys the referenced entity.
     */
    public void destroy() {
        engine.destroyEntity(entity);
    }
}
