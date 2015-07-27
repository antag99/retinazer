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

import com.github.antag99.retinazer.utils.Mask;

public final class EntitySet implements Iterable<Entity> {
    // Engine the entities belong to
    final Engine engine;
    // Mask updated by FamilyManager
    final Mask entities = new Mask();
    // Entity indices, for faster iteration
    int[] entityIndices = new int[0];
    boolean entityIndicesDirty = false;

    EntitySet(Engine engine) {
        this.engine = engine;
    }

    private int[] getEntityIndices() {
        if (entityIndicesDirty) {
            entityIndicesDirty = false;
            entityIndices = entities.indices();
        }
        return entityIndices;
    }

    private final class EntityIterator implements Iterator<Entity> {
        private int[] entityIndices = getEntityIndices();
        private int iterationIndex = 0;
        private int previousIndex = -1;

        @Override
        public boolean hasNext() {
            return iterationIndex < entityIndices.length;
        }

        @Override
        public Entity next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return engine.getEntityForIndex(entityIndices[previousIndex = iterationIndex++]);
        }

        @Override
        public void remove() {
            if (previousIndex == -1)
                throw new IllegalStateException();
            engine.getEntityForIndex(previousIndex).destroy();
            previousIndex = -1;
        }
    }

    @Override
    public Iterator<Entity> iterator() {
        return new EntityIterator();
    }

    public void process(EntityProcessor processor) {
        final Engine engine = this.engine;
        final int[] entityIndices = getEntityIndices();
        for (int i = 0, n = entityIndices.length; i < n; i++) {
            processor.process(engine.getEntityForIndex(entityIndices[i]));
        }
    }

    public boolean includes(Entity entity) {
        if (entity.getEngine() != engine)
            throw new IllegalArgumentException();
        return entities.get(entity.getIndex());
    }
}
