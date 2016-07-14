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
public final class DoubleBag implements AnyBag<DoubleBag> {

    /**
     * Backing buffer of this bag.
     */
    @Experimental
    public double[] buffer;

    /**
     * Creates a new {@code DoubleBag} with an initial capacity of {@code 0}.
     */
    public DoubleBag() {
        buffer = new double[0];
    }

    @Override
    public void copyFrom(DoubleBag from) {
        copyFrom(from, true);
    }

    @Override
    public void copyFrom(DoubleBag from, boolean clearExceeding) {
        copyFrom(from, from.buffer.length, clearExceeding);
    }

    @Override
    public void copyFrom(DoubleBag from, int length) {
        copyFrom(from, length, true);
    }

    @Override
    public void copyFrom(DoubleBag from, int length, boolean clearExceeding) {
        copyFrom(from, 0, length, clearExceeding);
    }

    @Override
    public void copyFrom(DoubleBag from, int fromOffset, int length) {
        copyFrom(from, fromOffset, length, true);
    }

    @Override
    public void copyFrom(DoubleBag from, int fromOffset, int length, boolean clearExceeding) {
        if (buffer.length < length)
            buffer = new double[length];
        // Maximum number of elements that can be copied from the given buffer
        int copyLength = Math.min(length, from.buffer.length - fromOffset);
        System.arraycopy(from.buffer, fromOffset, buffer, 0, copyLength);
        if (clearExceeding && buffer.length > copyLength) {
            double[] buffer = this.buffer;
            for (int i = copyLength, n = buffer.length; i < n; i++)
                buffer[i] = 0d;
        }
    }

    @Override
    public void copyPartFrom(DoubleBag from, int fromOffset, int toOffset, int length) {
        ensureCapacity(toOffset + length);
        // Maximum number of elements that can be copied from the given buffer
        int maxLength = from.buffer.length - fromOffset;
        System.arraycopy(from.buffer, fromOffset, buffer, toOffset, Math.min(length, maxLength));
        if (maxLength < length) {
            double[] buffer = this.buffer;
            for (int i = toOffset + maxLength, n = toOffset + length; i < n; i++)
                buffer[i] = 0d;
        }
    }

    @Override
    public void ensureCapacity(int capacity) {
        if (capacity < 0)
            throw new NegativeArraySizeException(String.valueOf(capacity));
        if (this.buffer.length >= capacity)
            return;
        int newCapacity = Bag.nextPowerOfTwo(capacity);
        double[] newBuffer = new double[newCapacity];
        System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
        this.buffer = newBuffer;
    }

    /**
     * Gets the element at the given index. Returns {@code 0d} if it does not exist.
     *
     * @param index
     *            Index of the element. The size of the buffer will not be increased if the index is greater.
     */
    public double get(int index) {
        if (index < 0)
            throw new IndexOutOfBoundsException("index < 0: " + index);

        return index >= buffer.length ? 0d : (double) buffer[index];
    }

    /**
     * Sets the element at the given index.
     *
     * @param index
     *            Index of the element. The size of the buffer will be increased if necessary.
     * @param value
     *            Value to set.
     */
    public void set(int index, double value) {
        if (index < 0)
            throw new IndexOutOfBoundsException("index < 0: " + index);

        ensureCapacity(index + 1);

        buffer[index] = (double) value;
    }

    @Override
    public void clear() {
        double[] buffer = this.buffer;
        for (int i = 0, n = buffer.length; i < n; ++i)
            buffer[i] = 0d;
    }

    @Override
    public void clear(Mask mask) {
        double[] buffer = this.buffer;
        for (int i = mask.nextSetBit(0), n = buffer.length; i != -1 && i < n; i = mask.nextSetBit(i + 1))
            buffer[i] = 0d;
    }
}
