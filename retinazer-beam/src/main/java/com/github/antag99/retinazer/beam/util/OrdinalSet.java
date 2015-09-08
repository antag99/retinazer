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
package com.github.antag99.retinazer.beam.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import com.github.antag99.retinazer.util.Mask;

public final class OrdinalSet<T extends Ordinal<T>> implements Iterable<T> {
    private OrdinalType<T> type;
    private Mask mask = new Mask();
    private OrdinalIterator iterator0 = new OrdinalIterator();
    private OrdinalIterator iterator1 = new OrdinalIterator();

    private final class OrdinalIterator implements Iterator<T> {
        int index = -1;
        int lastIndex = -1;
        boolean valid = false;

        @Override
        public boolean hasNext() {
            if (!valid) {
                throw new IllegalStateException("This iterator has expired");
            }
            return mask.nextSetBit(index) != -1;
        }

        @Override
        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();
            lastIndex = mask.nextSetBit(index);
            index = lastIndex + 1;
            return type.forIndex(lastIndex);
        }

        @Override
        public void remove() {
            if (lastIndex == -1)
                throw new IllegalStateException();
            mask.clear(lastIndex);
            lastIndex = -1;
        }
    }

    public OrdinalSet(OrdinalType<T> type) {
        this.type = type;
    }

    public void add(T ordinal) {
        Objects.requireNonNull(ordinal, "ordinal must not be null");
        if (type.forIndex(ordinal.getIndex()) != ordinal)
            throw new IllegalArgumentException("Invalid ordinal type");
        mask.set(ordinal.getIndex());
    }

    public void remove(T ordinal) {
        Objects.requireNonNull(ordinal, "ordinal must not be null");
        if (type.forIndex(ordinal.getIndex()) != ordinal)
            throw new IllegalArgumentException("Invalid ordinal type");
        mask.clear(ordinal.getIndex());
    }

    public boolean contains(T ordinal) {
        Objects.requireNonNull(ordinal, "ordinal must not be null");
        if (type.forIndex(ordinal.getIndex()) != ordinal)
            throw new IllegalArgumentException("Invalid ordinal type");
        return mask.get(ordinal.getIndex());
    }

    public boolean intersects(OrdinalSet<T> other) {
        if (other.type != type)
            throw new IllegalArgumentException("Invalid ordinal type");
        return mask.intersects(other.mask);
    }

    public void clear() {
        mask.clear();
    }

    @Override
    public Iterator<T> iterator() {
        if (iterator0.valid) {
            iterator0.valid = false;
            iterator1.index = 0;
            iterator1.lastIndex = -1;
            iterator1.valid = true;
            return iterator1;
        } else {
            iterator1.valid = false;
            iterator0.index = 0;
            iterator0.lastIndex = -1;
            iterator0.valid = true;
            return iterator0;
        }
    }
}
