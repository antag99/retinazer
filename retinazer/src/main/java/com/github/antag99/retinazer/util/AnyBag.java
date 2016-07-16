/*******************************************************************************
 * Retinazer, an entity-component-system framework for Java
 *
 * Copyright (C) 2015-2016 Anton Gustafsson
 *
 * This file is part of Retinazer.
 *
 * Retinazer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Retinazer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Retinazer.  If not, see <http://www.gnu.org/licenses/>.
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
