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
 *
 * @param T
 *            Type of this bag.
 */
public interface AnyBag<T extends AnyBag<T>> {

    /**
     * Copies the contents from the other bag into this bag.
     * Clears any exceeding elements in the buffer of this bag.
     *
     * @param from
     *            the other bag.
     */
    void copyFrom(T from);

    /**
     * Copies the contents from the other bag into this bag.
     *
     * @param from
     *            the other bag.
     * @param clearExceeding
     *            whether to clear elements past the copied part.
     */
    void copyFrom(T from, boolean clearExceeding);

    /**
     * Copies the contents from the other bag into this bag.
     * Clears any exceeding elements in the buffer of this bag.
     *
     * @param from
     *            the other bag.
     * @param length
     *            amount of items to copy.
     */
    void copyFrom(T from, int length);

    /**
     * Copies the contents from the other bag into this bag.
     *
     * @param from
     *            the other bag.
     * @param length
     *            amount of items to copy.
     * @param clearExceeding
     *            whether to clear elements past the copied part.
     */
    void copyFrom(T from, int length, boolean clearExceeding);

    /**
     * Copies the contents from the other bag into this bag.
     * Clears any exceeding elements in the buffer of this bag.
     *
     * @param from
     *            the other bag.
     * @param fromOffset
     *            offset into the other bag.
     * @param length
     *            amount of items to copy.
     */
    void copyFrom(T from, int fromOffset, int length);

    /**
     * Copies the contents from the other bag into this bag.
     *
     * @param from
     *            the other bag.
     * @param fromOffset
     *            offset into the other bag.
     * @param length
     *            amount of items to copy.
     * @param clearExceeding
     *            whether to clear elements past the copied part.
     */
    void copyFrom(T from, int fromOffset, int length, boolean clearExceeding);

    /**
     * Copies a part from the other bag into this bag.
     *
     * @param from
     *            the other bag.
     * @param fromOffset
     *            offset into the other bag.
     * @param toOffset
     *            offset into this bag.
     * @param length
     *            amount of items to copy.
     */
    void copyPartFrom(T from, int fromOffset, int toOffset, int length);

    /**
     * Ensures the backing buffer of this bag has the specified capacity.
     *
     * @param capacity
     *            the capacity, will be increased to the nearest power of two.
     */
    void ensureCapacity(int capacity);

    /**
     * Resets all elements of this bag.
     */
    void clear();

    /**
     * Resets all elements set in the given mask. Bits in the
     * mask that exceed the capacity of this bag will be ignored.
     *
     * @param mask
     *            the mask.
     */
    void clear(Mask mask);
}
