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

import com.github.antag99.retinazer.utils.Mask;
import com.github.antag99.retinazer.utils.GuidComponent;

/**
 * Entity provides a handle for accessing the components of an entity.
 */
public final class Entity {
    private Engine engine = null;
    private int index = -1;

    /**
     * Stores what components this entity has. This is stored with the entities
     * in order to speed up family filtering, which uses bitwise operations.
     */
    Mask components = new Mask();

    /**
     * Stores what families this entity belongs to. This is also stored in a
     * family to entities mapping, this mask is used for speeding up operations
     * like event filtering.
     */
    Mask families = new Mask();

    Entity(Engine engine, int index) {
        this.engine = engine;
        this.index = index;
    }

    /**
     * Gets the {@link Engine} this entity currently is added to
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * Gets the index of this entity in the engine it is added to. Returns -1 if
     * the entity is not added to an engine. This index can be used as implicit
     * key in sparse arrays, which is useful for local caches or other data that
     * isn't ideally stored in components.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the unique id of this entity, which can be assigned when it's added
     * to an {@link Engine}. Throws an exception if an ID has not been assigned
     * to this entity.
     */
    public long getGuid() {
        GuidComponent component = engine.guidStorage.get(this);
        if (component == null)
            throw new IllegalArgumentException("This entity has no guid");
        return component.getGuid();
    }

    /**
     * Adds the given component to this entity. This will replace any existing
     * component of the same type. Note that all component operations are
     * performed at the end of each tick.
     */
    @SuppressWarnings("unchecked")
    public void add(Component component) {
        engine.componentManager.getStorage((Class<Component>) component.getClass()).add(this, component);
    }

    /**
     * Removes the component of the given type from this entity.
     */
    public void remove(Class<? extends Component> componentType) {
        engine.componentManager.getStorage(componentType).remove(this);
    }

    /**
     * Gets the component of the given type from this entity. This returns
     * <code>null</code> if the component does not exist.
     */
    public <T extends Component> T get(Class<T> componentType) {
        return engine.componentManager.getStorage(componentType).get(this);
    }

    /**
     * Gets whether this entity has the component of the given type.
     */
    public boolean has(Class<? extends Component> componentType) {
        return engine.componentManager.getStorage(componentType).has(this);
    }

    /**
     * Gets the components of this entity.
     * </p>
     * The preferred usage pattern for this method is using a for-each loop such
     * as:
     *
     * <pre>
     * for (Component component : entity.getComponents()) {
     *     ...
     * }
     * </pre>
     */
    public Iterable<Component> getComponents() {
        return engine.componentManager.getComponents(this);
    }

    public void destroy() {
        engine.entityManager.destroyEntity(this);
    }
}
