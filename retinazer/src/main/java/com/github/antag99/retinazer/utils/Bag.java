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
package com.github.antag99.retinazer.utils;

import java.util.Arrays;

public final class Bag<E> {
    private Object[] buffer;

    static int nextPowerOfTwo(int value) {
        if (value == 0) {
            return 1;
        }
        value--;
        value |= value >>> 1;
        value |= value >>> 2;
        value |= value >>> 4;
        value |= value >>> 8;
        value |= value >>> 16;
        return value + 1;
    }

    public Bag() {
        this(0);
    }

    public Bag(int capacity) {
        buffer = new Object[capacity];
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index >= buffer.length) {
            return null;
        }

        return (E) buffer[index];
    }

    public void set(int index, E value) {
        if (index >= buffer.length) {
            if (value == null) {
                return;
            }
            buffer = Arrays.copyOf(buffer, Bag.nextPowerOfTwo(index + 1));
        }

        buffer[index] = value;
    }

    public void clear() {
        for (int i = 0, n = buffer.length; i < n; ++i) {
            buffer[i] = null;
        }
    }

    @Experimental
    public int getCapacity() {
        return buffer.length;
    }

    @Experimental
    public Object[] getBuffer() {
        return buffer;
    }
}
