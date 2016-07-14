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
package com.github.antag99.retinazer.util;

/**
 * A bag is an automatically expanding array.
 */
// This class is auto-generated; do not modify! @off
@SuppressWarnings("all")
public final class BooleanBag implements AnyBag<BooleanBag> {

    /**
     * Backing buffer of this bag.
     */
    @Experimental
    public boolean[] buffer;

    /**
     * Creates a new {@code BooleanBag} with an initial capacity of {@code 0}.
     */
    public BooleanBag() {
        buffer = new boolean[0];
    }

    @Override
    public void copyFrom(BooleanBag from) {
        copyFrom(from, true);
    }

    @Override
    public void copyFrom(BooleanBag from, boolean clearExceeding) {
        copyFrom(from, from.buffer.length, clearExceeding);
    }

    @Override
    public void copyFrom(BooleanBag from, int length) {
        copyFrom(from, length, true);
    }

    @Override
    public void copyFrom(BooleanBag from, int length, boolean clearExceeding) {
        copyFrom(from, 0, length, clearExceeding);
    }

    @Override
    public void copyFrom(BooleanBag from, int fromOffset, int length) {
        copyFrom(from, fromOffset, length, true);
    }

    @Override
    public void copyFrom(BooleanBag from, int fromOffset, int length, boolean clearExceeding) {
        if (buffer.length < length)
            buffer = new boolean[length];
        // Maximum number of elements that can be copied from the given buffer
        int copyLength = Math.min(length, from.buffer.length - fromOffset);
        System.arraycopy(from.buffer, fromOffset, buffer, 0, copyLength);
        if (clearExceeding && buffer.length > copyLength) {
            boolean[] buffer = this.buffer;
            for (int i = copyLength, n = buffer.length; i < n; i++)
                buffer[i] = false;
        }
    }

    @Override
    public void copyPartFrom(BooleanBag from, int fromOffset, int toOffset, int length) {
        ensureCapacity(toOffset + length);
        // Maximum number of elements that can be copied from the given buffer
        int maxLength = from.buffer.length - fromOffset;
        System.arraycopy(from.buffer, fromOffset, buffer, toOffset, Math.min(length, maxLength));
        if (maxLength < length) {
            boolean[] buffer = this.buffer;
            for (int i = toOffset + maxLength, n = toOffset + length; i < n; i++)
                buffer[i] = false;
        }
    }

    @Override
    public void ensureCapacity(int capacity) {
        if (capacity < 0)
            throw new NegativeArraySizeException(String.valueOf(capacity));
        if (this.buffer.length >= capacity)
            return;
        int newCapacity = Bag.nextPowerOfTwo(capacity);
        boolean[] newBuffer = new boolean[newCapacity];
        System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
        this.buffer = newBuffer;
    }

    /**
     * Gets the element at the given index. Returns {@code false} if it does not exist.
     *
     * @param index
     *            Index of the element. The size of the buffer will not be increased if the index is greater.
     */
    public boolean get(int index) {
        if (index < 0)
            throw new IndexOutOfBoundsException("index < 0: " + index);

        return index >= buffer.length ? false : (boolean) buffer[index];
    }

    /**
     * Sets the element at the given index.
     *
     * @param index
     *            Index of the element. The size of the buffer will be increased if necessary.
     * @param value
     *            Value to set.
     */
    public void set(int index, boolean value) {
        if (index < 0)
            throw new IndexOutOfBoundsException("index < 0: " + index);

        ensureCapacity(index + 1);

        buffer[index] = (boolean) value;
    }

    @Override
    public void clear() {
        boolean[] buffer = this.buffer;
        for (int i = 0, n = buffer.length; i < n; ++i)
            buffer[i] = false;
    }

    @Override
    public void clear(Mask mask) {
        boolean[] buffer = this.buffer;
        for (int i = mask.nextSetBit(0), n = buffer.length; i != -1 && i < n; i = mask.nextSetBit(i + 1))
            buffer[i] = false;
    }
}
