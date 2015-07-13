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

import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

final class EntityIterator implements Iterator<Entity> {
    private EntityManager entityManager;
    private BitSet bits;
    private int index = 0;
    private int previousIndex = -1;

    public EntityIterator(EntityManager entityManager, BitSet bits) {
        this.entityManager = entityManager;
        this.bits = bits;
    }

    @Override
    public boolean hasNext() {
        return bits.nextSetBit(index) != -1;
    }

    @Override
    public Entity next() {
        if (!hasNext())
            throw new NoSuchElementException();
        int entityIndex = bits.nextSetBit(index);
        index = entityIndex + 1;
        previousIndex = entityIndex;
        return entityManager.getEntityForIndex(entityIndex);
    }

    @Override
    public void remove() {
        if (previousIndex == -1)
            throw new IllegalStateException();
        entityManager.getEntityForIndex(previousIndex).destroy();
        previousIndex = -1;
    }
}