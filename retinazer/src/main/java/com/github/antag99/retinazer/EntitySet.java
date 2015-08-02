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

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.github.antag99.retinazer.utils.Experimental;
import com.github.antag99.retinazer.utils.Mask;

public final class EntitySet implements Iterable<Entity> {
    private EntitySetContent content;

    private Entity[] entities = new Entity[0];
    private int entitiesModCount = 0;

    private int[] indices = new int[0];
    private int indicesModCount = 0;

    EntitySet(EntitySetContent content) {
        this.content = content;
    }

    /**
     * <p>
     * Returns an array containing all entities in this set. Note that whenever
     * the entity set changes, this array must be reconstructed. For maximum
     * performance, {@link #getIndices()} should be used.
     * </p>
     *
     * <p>
     * <b>WARNING:</b> Modifying this array leads to undefined behavior.
     * </p>
     *
     * @return All entities in this set.
     */
    @Experimental
    public Entity[] getEntities() {
        if (entitiesModCount != content.modCount) {
            Engine engine = content.engine;
            Mask m = content.entities;
            entities = new Entity[m.cardinality()];
            for (int i = 0, b = m.nextSetBit(0), n = entities.length; i < n; i++, b = m.nextSetBit(b + 1)) {
                entities[i] = engine.getEntityForIndex(b);
            }
            entitiesModCount = content.modCount;
        }
        return entities;
    }

    /**
     * <p>
     * Returns an array containing the indices of all entities in this set.
     * Note that whenever the entity set changes, this array must be
     * reconstructed.
     * </p>
     *
     * <p>
     * <b>WARNING:</b> Modifying this array leads to undefined behavior.
     * </p>
     *
     * @return The indices of all entities in this set.
     */
    @Experimental
    public int[] getIndices() {
        if (indicesModCount != content.modCount) {
            indices = content.entities.indices();
            indicesModCount = content.modCount;
        }
        return indices;
    }

    private final class EntityIterator implements Iterator<Entity> {
        private Entity[] entities = getEntities();
        private int iterationIndex = 0;
        private int previousIndex = -1;

        @Override
        public boolean hasNext() {
            return iterationIndex < entities.length;
        }

        @Override
        public Entity next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return entities[previousIndex = iterationIndex++];
        }

        @Override
        public void remove() {
            if (previousIndex == -1)
                throw new IllegalStateException();
            content.engine.getEntityForIndex(previousIndex).destroy();
            previousIndex = -1;
        }
    }

    @Override
    public Iterator<Entity> iterator() {
        return new EntityIterator();
    }

    public void process(EntityProcessor processor) {
        final Entity[] entities = getEntities();
        for (int i = 0, n = entities.length; i < n; i++) {
            processor.process(entities[i]);
        }
    }

    public boolean includes(Entity entity) {
        if (entity.getEngine() != content.engine)
            throw new IllegalArgumentException();
        return content.entities.get(entity.getIndex());
    }
}
