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
 * Pool implementation for recycling objects; this saves the performance impact
 * of garbage collection, which is mostly important for weak mobile devices.
 * </p>
 * <p>
 * This class is not public API to avoid name clashes with various game libraries.
 * </p>
 *
 * @param <T> Type of objects to be recycled
 */
abstract class Pool<T> {
    private Object[] buffer;
    private int last = -1;

    public Pool() {
        this(16);
    }

    public Pool(int capacity) {
        buffer = new Object[capacity];
    }

    /**
     * Creates a new object; called when this pool has run out of available objects
     *
     * @return The new object
     */
    protected abstract T create();

    /**
     * Destroys the given object; called when it is freed to reset to pristine state
     *
     * @param object The object to destroy
     */
    protected void destroy(T object) {
    }

    @SuppressWarnings("unchecked")
    public T obtain() {
        if (last == -1)
            return create();
        return (T) buffer[last--];
    }

    public void free(T object) {
        if (last < buffer.length) {
            destroy(object);
            buffer[++last] = object;
        }
    }
}
