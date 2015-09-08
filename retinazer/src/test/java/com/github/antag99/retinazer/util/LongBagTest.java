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

import com.github.antag99.retinazer.util.LongBag;

public class LongBagTest {

    /**
     * Ensures that the elements of a bag are actually stored
     */
    @Test
    public void testStorage() {
        LongBag bag = new LongBag();

        bag.set(0, 0);
        assertEquals(0, bag.get(0));
        assertEquals(0, bag.get(1));
        assertEquals(0, bag.get(2));
        assertEquals(0, bag.get(3));
        assertEquals(0, bag.get(4));
        assertEquals(0, bag.get(5));
        assertEquals(0, bag.get(6));
        assertEquals(0, bag.get(7));

        bag.set(1, 1);
        assertEquals(0, bag.get(0));
        assertEquals(1, bag.get(1));
        assertEquals(0, bag.get(2));
        assertEquals(0, bag.get(3));
        assertEquals(0, bag.get(4));
        assertEquals(0, bag.get(5));
        assertEquals(0, bag.get(6));
        assertEquals(0, bag.get(7));

        bag.set(2, 2);
        assertEquals(0, bag.get(0));
        assertEquals(1, bag.get(1));
        assertEquals(2, bag.get(2));
        assertEquals(0, bag.get(3));
        assertEquals(0, bag.get(4));
        assertEquals(0, bag.get(5));
        assertEquals(0, bag.get(6));
        assertEquals(0, bag.get(7));

        bag.set(3, 3);
        assertEquals(0, bag.get(0));
        assertEquals(1, bag.get(1));
        assertEquals(2, bag.get(2));
        assertEquals(3, bag.get(3));
        assertEquals(0, bag.get(4));
        assertEquals(0, bag.get(5));
        assertEquals(0, bag.get(6));
        assertEquals(0, bag.get(7));

        bag.set(4, 4);
        assertEquals(0, bag.get(0));
        assertEquals(1, bag.get(1));
        assertEquals(2, bag.get(2));
        assertEquals(3, bag.get(3));
        assertEquals(4, bag.get(4));
        assertEquals(0, bag.get(5));
        assertEquals(0, bag.get(6));
        assertEquals(0, bag.get(7));

        bag.set(5, 5);
        assertEquals(0, bag.get(0));
        assertEquals(1, bag.get(1));
        assertEquals(2, bag.get(2));
        assertEquals(3, bag.get(3));
        assertEquals(4, bag.get(4));
        assertEquals(5, bag.get(5));
        assertEquals(0, bag.get(6));
        assertEquals(0, bag.get(7));

        bag.set(6, 6);
        assertEquals(0, bag.get(0));
        assertEquals(1, bag.get(1));
        assertEquals(2, bag.get(2));
        assertEquals(3, bag.get(3));
        assertEquals(4, bag.get(4));
        assertEquals(5, bag.get(5));
        assertEquals(6, bag.get(6));
        assertEquals(0, bag.get(7));

        bag.set(7, 7);
        assertEquals(0, bag.get(0));
        assertEquals(1, bag.get(1));
        assertEquals(2, bag.get(2));
        assertEquals(3, bag.get(3));
        assertEquals(4, bag.get(4));
        assertEquals(5, bag.get(5));
        assertEquals(6, bag.get(6));
        assertEquals(7, bag.get(7));

        bag.clear();

        assertEquals(0, bag.get(0));
        assertEquals(0, bag.get(1));
        assertEquals(0, bag.get(2));
        assertEquals(0, bag.get(3));
        assertEquals(0, bag.get(4));
        assertEquals(0, bag.get(5));
        assertEquals(0, bag.get(6));
        assertEquals(0, bag.get(7));
    }

    /**
     * Ensures that the bag contains the default value by default
     */
    @Test
    public void testDefault() {
        LongBag bag = new LongBag();
        assertEquals(0, bag.get(0));
        bag.set(0, 1);
        assertEquals(0, bag.get(1));
        assertEquals(0, bag.get(2));
        assertEquals(0, bag.get(3));
    }

    /**
     * Ensures that the bag resizes correctly when out of capacity, that it
     * does not resize when queried for non-existing elements, and that it does
     * not resize when the default value is set.
     */
    @Test
    public void testCapacity() {
        LongBag bag;

        bag = new LongBag();
        assertEquals(0, bag.buffer.length);
        bag.set(0, 1);
        assertEquals(1, bag.buffer.length);
        bag.set(1, 2);
        assertEquals(2, bag.buffer.length);
        bag.set(2, 3);
        assertEquals(4, bag.buffer.length);
        bag.set(3, 4);
        assertEquals(4, bag.buffer.length);
        bag.set(4, 5);
        assertEquals(8, bag.buffer.length);
        bag.set(8, 6);
        assertEquals(16, bag.buffer.length);
        bag.set(35, 7);
        assertEquals(64, bag.buffer.length);

        bag = new LongBag();
        for (int i = 0; i < 32; i++) {
            bag.get((1 << i) - 1);
            assertEquals(0, bag.buffer.length);
        }
        bag.get(Integer.MAX_VALUE);
        assertEquals(0, bag.buffer.length);

        bag = new LongBag();
        for (int i = 0; i < 31; i++) {
            bag.set((1 << i) - 1, 0);
            assertEquals(0, bag.buffer.length);
        }
        bag.set(Integer.MAX_VALUE, 0);
        assertEquals(0, bag.buffer.length);
    }

    /**
     * When a negative index is used, an {@link IndexOutOfBoundsException} should be thrown.
     */
    @Test
    public void testIndexOutOfBoundsException() {
        LongBag bag = new LongBag();
        for (int i = 0; i < 32; i++) {
            try {
                bag.set(-(1 << i), 0);
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
