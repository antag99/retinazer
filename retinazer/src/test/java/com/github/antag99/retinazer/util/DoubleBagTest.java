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

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.antag99.retinazer.util.DoubleBag;

public class DoubleBagTest {

    /**
     * Ensures that the elements of a bag are actually stored
     */
    @Test
    public void testStorage() {
        DoubleBag bag = new DoubleBag();

        bag.set(0, 0d);
        assertEquals(0d, bag.get(0), 0d);
        assertEquals(0d, bag.get(1), 0d);
        assertEquals(0d, bag.get(2), 0d);
        assertEquals(0d, bag.get(3), 0d);
        assertEquals(0d, bag.get(4), 0d);
        assertEquals(0d, bag.get(5), 0d);
        assertEquals(0d, bag.get(6), 0d);
        assertEquals(0d, bag.get(7), 0d);

        bag.set(1, 1d);
        assertEquals(0d, bag.get(0), 0d);
        assertEquals(1d, bag.get(1), 0d);
        assertEquals(0d, bag.get(2), 0d);
        assertEquals(0d, bag.get(3), 0d);
        assertEquals(0d, bag.get(4), 0d);
        assertEquals(0d, bag.get(5), 0d);
        assertEquals(0d, bag.get(6), 0d);
        assertEquals(0d, bag.get(7), 0d);

        bag.set(2, 2d);
        assertEquals(0d, bag.get(0), 0d);
        assertEquals(1d, bag.get(1), 0d);
        assertEquals(2d, bag.get(2), 0d);
        assertEquals(0d, bag.get(3), 0d);
        assertEquals(0d, bag.get(4), 0d);
        assertEquals(0d, bag.get(5), 0d);
        assertEquals(0d, bag.get(6), 0d);
        assertEquals(0d, bag.get(7), 0d);

        bag.set(3, 3d);
        assertEquals(0d, bag.get(0), 0d);
        assertEquals(1d, bag.get(1), 0d);
        assertEquals(2d, bag.get(2), 0d);
        assertEquals(3d, bag.get(3), 0d);
        assertEquals(0d, bag.get(4), 0d);
        assertEquals(0d, bag.get(5), 0d);
        assertEquals(0d, bag.get(6), 0d);
        assertEquals(0d, bag.get(7), 0d);

        bag.set(4, 4d);
        assertEquals(0d, bag.get(0), 0d);
        assertEquals(1d, bag.get(1), 0d);
        assertEquals(2d, bag.get(2), 0d);
        assertEquals(3d, bag.get(3), 0d);
        assertEquals(4d, bag.get(4), 0d);
        assertEquals(0d, bag.get(5), 0d);
        assertEquals(0d, bag.get(6), 0d);
        assertEquals(0d, bag.get(7), 0d);

        bag.set(5, 5d);
        assertEquals(0d, bag.get(0), 0d);
        assertEquals(1d, bag.get(1), 0d);
        assertEquals(2d, bag.get(2), 0d);
        assertEquals(3d, bag.get(3), 0d);
        assertEquals(4d, bag.get(4), 0d);
        assertEquals(5d, bag.get(5), 0d);
        assertEquals(0d, bag.get(6), 0d);
        assertEquals(0d, bag.get(7), 0d);

        bag.set(6, 6d);
        assertEquals(0d, bag.get(0), 0d);
        assertEquals(1d, bag.get(1), 0d);
        assertEquals(2d, bag.get(2), 0d);
        assertEquals(3d, bag.get(3), 0d);
        assertEquals(4d, bag.get(4), 0d);
        assertEquals(5d, bag.get(5), 0d);
        assertEquals(6d, bag.get(6), 0d);
        assertEquals(0d, bag.get(7), 0d);

        bag.set(7, 7d);
        assertEquals(0d, bag.get(0), 0d);
        assertEquals(1d, bag.get(1), 0d);
        assertEquals(2d, bag.get(2), 0d);
        assertEquals(3d, bag.get(3), 0d);
        assertEquals(4d, bag.get(4), 0d);
        assertEquals(5d, bag.get(5), 0d);
        assertEquals(6d, bag.get(6), 0d);
        assertEquals(7d, bag.get(7), 0d);

        bag.clear();

        assertEquals(0d, bag.get(0), 0d);
        assertEquals(0d, bag.get(1), 0d);
        assertEquals(0d, bag.get(2), 0d);
        assertEquals(0d, bag.get(3), 0d);
        assertEquals(0d, bag.get(4), 0d);
        assertEquals(0d, bag.get(5), 0d);
        assertEquals(0d, bag.get(6), 0d);
        assertEquals(0d, bag.get(7), 0d);
    }

    /**
     * Ensures that the bag contains the default value by default
     */
    @Test
    public void testDefault() {
        DoubleBag bag = new DoubleBag();
        assertEquals(0d, bag.get(0), 0d);
        bag.set(0, 1d);
        assertEquals(0d, bag.get(1), 0d);
        assertEquals(0d, bag.get(2), 0d);
        assertEquals(0d, bag.get(3), 0d);
    }

    /**
     * Ensures that the bag resizes correctly when out of capacity, that it
     * does not resize when queried for non-existing elements, and that it does
     * not resize when the default value is set.
     */
    @Test
    public void testCapacity() {
        DoubleBag bag;

        bag = new DoubleBag();
        assertEquals(0, bag.buffer.length);
        bag.set(0, 1d);
        assertEquals(1, bag.buffer.length);
        bag.set(1, 2d);
        assertEquals(2, bag.buffer.length);
        bag.set(2, 3d);
        assertEquals(4, bag.buffer.length);
        bag.set(3, 4d);
        assertEquals(4, bag.buffer.length);
        bag.set(4, 5d);
        assertEquals(8, bag.buffer.length);
        bag.set(8, 6d);
        assertEquals(16, bag.buffer.length);
        bag.set(35, 7d);
        assertEquals(64, bag.buffer.length);

        bag = new DoubleBag();
        for (int i = 0; i < 32; i++) {
            bag.get((1 << i) - 1);
            assertEquals(0, bag.buffer.length);
        }
        bag.get(Integer.MAX_VALUE);
        assertEquals(0, bag.buffer.length);

        bag = new DoubleBag();
        for (int i = 0; i < 31; i++) {
            bag.set((1 << i) - 1, 0d);
            assertEquals(0, bag.buffer.length);
        }
        bag.set(Integer.MAX_VALUE, 0d);
        assertEquals(0, bag.buffer.length);
    }

    /**
     * When a negative index is used, an {@link IndexOutOfBoundsException} should be thrown.
     */
    @Test
    public void testIndexOutOfBoundsException() {
        DoubleBag bag = new DoubleBag();
        for (int i = 0; i < 32; i++) {
            try {
                bag.set(-(1 << i), 0d);
            } catch (IndexOutOfBoundsException ex) {
                if (ex.getClass() == IndexOutOfBoundsException.class)
                    continue;
            }

            fail("IndexOutOfBoundsException expected for index " + (-(1 << i)));
        }

        for (int i = 0; i < 32; i++) {
            try {
                bag.get(-(1 << i));
            } catch (IndexOutOfBoundsException ex) {
                if (ex.getClass() == IndexOutOfBoundsException.class)
                    continue;
            }

            fail("IndexOutOfBoundsException expected for index " + (-(1 << i)));
        }
    }
}
