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

import org.junit.Test;
import static org.junit.Assert.*;

// This class is auto-generated; do not modify! @off
@SuppressWarnings("all")
public final class FloatBagTest {
    /**
     * Ensures that the elements of a bag are actually stored
     */
    @Test
    public void testStorage() {
        FloatBag bag = new FloatBag();
        float element0 = 1f;
        float element1 = 1f;
        float element2 = 1f;
        float element3 = 1f;
        float element4 = 1f;
        float element5 = 1f;
        float element6 = 1f;
        float element7 = 1f;

        bag.set(0, element0);
        assertEquals((Float)element0, (Float)bag.get(0));
        assertEquals((Float)0f, (Float)bag.get(1));
        assertEquals((Float)0f, (Float)bag.get(2));
        assertEquals((Float)0f, (Float)bag.get(3));
        assertEquals((Float)0f, (Float)bag.get(4));
        assertEquals((Float)0f, (Float)bag.get(5));
        assertEquals((Float)0f, (Float)bag.get(6));
        assertEquals((Float)0f, (Float)bag.get(7));

        bag.set(1, element1);
        assertEquals((Float)element0, (Float)bag.get(0));
        assertEquals((Float)element1, (Float)bag.get(1));
        assertEquals((Float)0f, (Float)bag.get(2));
        assertEquals((Float)0f, (Float)bag.get(3));
        assertEquals((Float)0f, (Float)bag.get(4));
        assertEquals((Float)0f, (Float)bag.get(5));
        assertEquals((Float)0f, (Float)bag.get(6));
        assertEquals((Float)0f, (Float)bag.get(7));

        bag.set(2, element2);
        assertEquals((Float)element0, (Float)bag.get(0));
        assertEquals((Float)element1, (Float)bag.get(1));
        assertEquals((Float)element2, (Float)bag.get(2));
        assertEquals((Float)0f, (Float)bag.get(3));
        assertEquals((Float)0f, (Float)bag.get(4));
        assertEquals((Float)0f, (Float)bag.get(5));
        assertEquals((Float)0f, (Float)bag.get(6));
        assertEquals((Float)0f, (Float)bag.get(7));

        bag.set(3, element3);
        assertEquals((Float)element0, (Float)bag.get(0));
        assertEquals((Float)element1, (Float)bag.get(1));
        assertEquals((Float)element2, (Float)bag.get(2));
        assertEquals((Float)element3, (Float)bag.get(3));
        assertEquals((Float)0f, (Float)bag.get(4));
        assertEquals((Float)0f, (Float)bag.get(5));
        assertEquals((Float)0f, (Float)bag.get(6));
        assertEquals((Float)0f, (Float)bag.get(7));

        bag.set(4, element4);
        assertEquals((Float)element0, (Float)bag.get(0));
        assertEquals((Float)element1, (Float)bag.get(1));
        assertEquals((Float)element2, (Float)bag.get(2));
        assertEquals((Float)element3, (Float)bag.get(3));
        assertEquals((Float)element4, (Float)bag.get(4));
        assertEquals((Float)0f, (Float)bag.get(5));
        assertEquals((Float)0f, (Float)bag.get(6));
        assertEquals((Float)0f, (Float)bag.get(7));

        bag.set(5, element5);
        assertEquals((Float)element0, (Float)bag.get(0));
        assertEquals((Float)element1, (Float)bag.get(1));
        assertEquals((Float)element2, (Float)bag.get(2));
        assertEquals((Float)element3, (Float)bag.get(3));
        assertEquals((Float)element4, (Float)bag.get(4));
        assertEquals((Float)element5, (Float)bag.get(5));
        assertEquals((Float)0f, (Float)bag.get(6));
        assertEquals((Float)0f, (Float)bag.get(7));

        bag.set(6, element6);
        assertEquals((Float)element0, (Float)bag.get(0));
        assertEquals((Float)element1, (Float)bag.get(1));
        assertEquals((Float)element2, (Float)bag.get(2));
        assertEquals((Float)element3, (Float)bag.get(3));
        assertEquals((Float)element4, (Float)bag.get(4));
        assertEquals((Float)element5, (Float)bag.get(5));
        assertEquals((Float)element6, (Float)bag.get(6));
        assertEquals((Float)0f, (Float)bag.get(7));

        bag.set(7, element7);
        assertEquals((Float)element0, (Float)bag.get(0));
        assertEquals((Float)element1, (Float)bag.get(1));
        assertEquals((Float)element2, (Float)bag.get(2));
        assertEquals((Float)element3, (Float)bag.get(3));
        assertEquals((Float)element4, (Float)bag.get(4));
        assertEquals((Float)element5, (Float)bag.get(5));
        assertEquals((Float)element6, (Float)bag.get(6));
        assertEquals((Float)element7, (Float)bag.get(7));

        bag.clear();

        assertEquals((Float)0f, (Float)bag.get(0));
        assertEquals((Float)0f, (Float)bag.get(1));
        assertEquals((Float)0f, (Float)bag.get(2));
        assertEquals((Float)0f, (Float)bag.get(3));
        assertEquals((Float)0f, (Float)bag.get(4));
        assertEquals((Float)0f, (Float)bag.get(5));
        assertEquals((Float)0f, (Float)bag.get(6));
        assertEquals((Float)0f, (Float)bag.get(7));
    }

    /**
     * Ensures that the bag contains the default value by default
     */
    @Test
    public void testDefault() {
        FloatBag bag = new FloatBag();
        assertEquals((Float)0f, (Float)bag.get(0));
        bag.set(0, 1f);
        assertEquals((Float)0f, (Float)bag.get(1));
        assertEquals((Float)0f, (Float)bag.get(2));
        assertEquals((Float)0f, (Float)bag.get(3));
    }

    /**
     * Ensures that the bag resizes correctly when out of capacity and that it
     * does not resize when queried for non-existing elements.
     */
    @Test
    public void testCapacity() {
        FloatBag bag;

        bag = new FloatBag();
        assertEquals(0, bag.buffer.length);
        bag.set(0, 1f);
        assertEquals(1, bag.buffer.length);
        bag.set(1, 1f);
        assertEquals(2, bag.buffer.length);
        bag.set(2, 1f);
        assertEquals(4, bag.buffer.length);
        bag.set(3, 1f);
        assertEquals(4, bag.buffer.length);
        bag.set(4, 1f);
        assertEquals(8, bag.buffer.length);
        bag.set(8, 1f);
        assertEquals(16, bag.buffer.length);
        bag.set(35, 1f);
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
                bag.set(-(1 << i), 1f);
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

    /**
     * When a negative capacity is used, a {@link NegativeArraySizeException} should be thrown.
     */
    @Test
    public void testNegativeArraySizeException() {
        FloatBag bag = new FloatBag();
        for (int i = 0; i < 32; i++) {
            try {
                bag.ensureCapacity(-(1 << i));
            } catch (NegativeArraySizeException ex) {
                if (ex.getClass() == NegativeArraySizeException.class)
                    continue;
            }

            fail("NegativeArraySizeException expected for capacity " + (-(1 << i)));
        }
    }

    /**
     * Ensures that the clear() and clear(Mask) methods work properly.
     */
    @Test
    public void testClear() {
        FloatBag bag;

        bag = new FloatBag();
        bag.set(0, 1f);
        bag.set(1, 1f);
        bag.set(63, 1f);
        bag.clear();
        assertEquals((Float) 0f, (Float) bag.get(0));
        assertEquals((Float) 0f, (Float) bag.get(1));
        assertEquals((Float) 0f, (Float) bag.get(63));
        assertEquals(64, bag.buffer.length);

        Mask mask = new Mask();
        bag = new FloatBag();
        bag.set(0, 1f);
        bag.set(1, 1f);
        bag.set(63, 1f);
        mask.set(63);
        mask.set(0);
        bag.clear(mask);
        assertEquals((Float) 0f, (Float) bag.get(0));
        assertNotEquals((Float) 0f, (Float) bag.get(1));
        assertEquals((Float) 0f, (Float) bag.get(63));
        mask.set(1);
        mask.set(457);
        bag.clear(mask);
        assertEquals((Float) 0f, (Float) bag.get(0));
        assertEquals((Float) 0f, (Float) bag.get(1));
        assertEquals((Float) 0f, (Float) bag.get(63));
        assertEquals(64, bag.buffer.length);
    }

    @Test
    public void testCopyFrom() {
        FloatBag bag0, bag1;

        bag0 = new FloatBag();
        bag0.set(0, 1f);
        bag0.set(5, 1f);

        bag1 = new FloatBag();
        bag1.set(9, 1f);

        bag1.copyFrom(bag0, false);
        assertNotEquals((Float) 0f, (Float) bag1.get(0));
        assertNotEquals((Float) 0f, (Float) bag1.get(5));
        assertNotEquals((Float) 0f, (Float) bag1.get(9));

        bag1.copyFrom(bag0);
        assertNotEquals((Float) 0f, (Float) bag1.get(0));
        assertNotEquals((Float) 0f, (Float) bag1.get(5));
        assertEquals((Float) 0f, (Float) bag1.get(9));

        bag0.copyFrom(bag1);
    }
}
