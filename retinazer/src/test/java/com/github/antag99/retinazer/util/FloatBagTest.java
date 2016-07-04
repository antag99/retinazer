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

import com.github.antag99.retinazer.util.FloatBag;

public class FloatBagTest {

    /**
     * Ensures that the elements of a bag are actually stored
     */
    @Test
    public void testStorage() {
        FloatBag bag = new FloatBag();

        bag.set(0, 0f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(0f, bag.get(1), 0f);
        assertEquals(0f, bag.get(2), 0f);
        assertEquals(0f, bag.get(3), 0f);
        assertEquals(0f, bag.get(4), 0f);
        assertEquals(0f, bag.get(5), 0f);
        assertEquals(0f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);

        bag.set(1, 1f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(1f, bag.get(1), 0f);
        assertEquals(0f, bag.get(2), 0f);
        assertEquals(0f, bag.get(3), 0f);
        assertEquals(0f, bag.get(4), 0f);
        assertEquals(0f, bag.get(5), 0f);
        assertEquals(0f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);

        bag.set(2, 2f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(1f, bag.get(1), 0f);
        assertEquals(2f, bag.get(2), 0f);
        assertEquals(0f, bag.get(3), 0f);
        assertEquals(0f, bag.get(4), 0f);
        assertEquals(0f, bag.get(5), 0f);
        assertEquals(0f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);

        bag.set(3, 3f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(1f, bag.get(1), 0f);
        assertEquals(2f, bag.get(2), 0f);
        assertEquals(3f, bag.get(3), 0f);
        assertEquals(0f, bag.get(4), 0f);
        assertEquals(0f, bag.get(5), 0f);
        assertEquals(0f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);

        bag.set(4, 4f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(1f, bag.get(1), 0f);
        assertEquals(2f, bag.get(2), 0f);
        assertEquals(3f, bag.get(3), 0f);
        assertEquals(4f, bag.get(4), 0f);
        assertEquals(0f, bag.get(5), 0f);
        assertEquals(0f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);

        bag.set(5, 5f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(1f, bag.get(1), 0f);
        assertEquals(2f, bag.get(2), 0f);
        assertEquals(3f, bag.get(3), 0f);
        assertEquals(4f, bag.get(4), 0f);
        assertEquals(5f, bag.get(5), 0f);
        assertEquals(0f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);

        bag.set(6, 6f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(1f, bag.get(1), 0f);
        assertEquals(2f, bag.get(2), 0f);
        assertEquals(3f, bag.get(3), 0f);
        assertEquals(4f, bag.get(4), 0f);
        assertEquals(5f, bag.get(5), 0f);
        assertEquals(6f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);

        bag.set(7, 7f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(1f, bag.get(1), 0f);
        assertEquals(2f, bag.get(2), 0f);
        assertEquals(3f, bag.get(3), 0f);
        assertEquals(4f, bag.get(4), 0f);
        assertEquals(5f, bag.get(5), 0f);
        assertEquals(6f, bag.get(6), 0f);
        assertEquals(7f, bag.get(7), 0f);

        bag.clear();

        assertEquals(0f, bag.get(0), 0f);
        assertEquals(0f, bag.get(1), 0f);
        assertEquals(0f, bag.get(2), 0f);
        assertEquals(0f, bag.get(3), 0f);
        assertEquals(0f, bag.get(4), 0f);
        assertEquals(0f, bag.get(5), 0f);
        assertEquals(0f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);
    }

    /**
     * Ensures that the bag contains the default value by default
     */
    @Test
    public void testDefault() {
        FloatBag bag = new FloatBag();
        assertEquals(0f, bag.get(0), 0f);
        bag.set(0, 1f);
        assertEquals(0f, bag.get(1), 0f);
        assertEquals(0f, bag.get(2), 0f);
        assertEquals(0f, bag.get(3), 0f);
    }

    /**
     * Ensures that the bag resizes correctly when out of capacity, that it
     * does not resize when queried for non-existing elements, and that it does
     * not resize when the default value is set.
     */
    @Test
    public void testCapacity() {
        FloatBag bag;

        bag = new FloatBag();
        assertEquals(0, bag.buffer.length);
        bag.set(0, 1f);
        assertEquals(1, bag.buffer.length);
        bag.set(1, 2f);
        assertEquals(2, bag.buffer.length);
        bag.set(2, 3f);
        assertEquals(4, bag.buffer.length);
        bag.set(3, 4f);
        assertEquals(4, bag.buffer.length);
        bag.set(4, 5f);
        assertEquals(8, bag.buffer.length);
        bag.set(8, 6f);
        assertEquals(16, bag.buffer.length);
        bag.set(35, 7f);
        assertEquals(64, bag.buffer.length);

        bag = new FloatBag();
        for (int i = 0; i < 32; i++) {
            bag.get((1 << i) - 1);
            assertEquals(0, bag.buffer.length);
        }
        bag.get(Integer.MAX_VALUE);
        assertEquals(0, bag.buffer.length);
    }

    /**
     * When a negative index is used, an {@link IndexOutOfBoundsException} should be thrown.
     */
    @Test
    public void testIndexOutOfBoundsException() {
        FloatBag bag = new FloatBag();
        for (int i = 0; i < 32; i++) {
            try {
                bag.set(-(1 << i), 0f);
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
