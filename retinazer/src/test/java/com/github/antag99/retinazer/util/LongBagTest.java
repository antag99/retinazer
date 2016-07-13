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
public final class LongBagTest {
    /**
     * Ensures that the elements of a bag are actually stored
     */
    @Test
    public void testStorage() {
        LongBag bag = new LongBag();
        long element0 = 1L;
        long element1 = 1L;
        long element2 = 1L;
        long element3 = 1L;
        long element4 = 1L;
        long element5 = 1L;
        long element6 = 1L;
        long element7 = 1L;

        bag.set(0, element0);
        assertEquals((Long)element0, (Long)bag.get(0));
        assertEquals((Long)0L, (Long)bag.get(1));
        assertEquals((Long)0L, (Long)bag.get(2));
        assertEquals((Long)0L, (Long)bag.get(3));
        assertEquals((Long)0L, (Long)bag.get(4));
        assertEquals((Long)0L, (Long)bag.get(5));
        assertEquals((Long)0L, (Long)bag.get(6));
        assertEquals((Long)0L, (Long)bag.get(7));

        bag.set(1, element1);
        assertEquals((Long)element0, (Long)bag.get(0));
        assertEquals((Long)element1, (Long)bag.get(1));
        assertEquals((Long)0L, (Long)bag.get(2));
        assertEquals((Long)0L, (Long)bag.get(3));
        assertEquals((Long)0L, (Long)bag.get(4));
        assertEquals((Long)0L, (Long)bag.get(5));
        assertEquals((Long)0L, (Long)bag.get(6));
        assertEquals((Long)0L, (Long)bag.get(7));

        bag.set(2, element2);
        assertEquals((Long)element0, (Long)bag.get(0));
        assertEquals((Long)element1, (Long)bag.get(1));
        assertEquals((Long)element2, (Long)bag.get(2));
        assertEquals((Long)0L, (Long)bag.get(3));
        assertEquals((Long)0L, (Long)bag.get(4));
        assertEquals((Long)0L, (Long)bag.get(5));
        assertEquals((Long)0L, (Long)bag.get(6));
        assertEquals((Long)0L, (Long)bag.get(7));

        bag.set(3, element3);
        assertEquals((Long)element0, (Long)bag.get(0));
        assertEquals((Long)element1, (Long)bag.get(1));
        assertEquals((Long)element2, (Long)bag.get(2));
        assertEquals((Long)element3, (Long)bag.get(3));
        assertEquals((Long)0L, (Long)bag.get(4));
        assertEquals((Long)0L, (Long)bag.get(5));
        assertEquals((Long)0L, (Long)bag.get(6));
        assertEquals((Long)0L, (Long)bag.get(7));

        bag.set(4, element4);
        assertEquals((Long)element0, (Long)bag.get(0));
        assertEquals((Long)element1, (Long)bag.get(1));
        assertEquals((Long)element2, (Long)bag.get(2));
        assertEquals((Long)element3, (Long)bag.get(3));
        assertEquals((Long)element4, (Long)bag.get(4));
        assertEquals((Long)0L, (Long)bag.get(5));
        assertEquals((Long)0L, (Long)bag.get(6));
        assertEquals((Long)0L, (Long)bag.get(7));

        bag.set(5, element5);
        assertEquals((Long)element0, (Long)bag.get(0));
        assertEquals((Long)element1, (Long)bag.get(1));
        assertEquals((Long)element2, (Long)bag.get(2));
        assertEquals((Long)element3, (Long)bag.get(3));
        assertEquals((Long)element4, (Long)bag.get(4));
        assertEquals((Long)element5, (Long)bag.get(5));
        assertEquals((Long)0L, (Long)bag.get(6));
        assertEquals((Long)0L, (Long)bag.get(7));

        bag.set(6, element6);
        assertEquals((Long)element0, (Long)bag.get(0));
        assertEquals((Long)element1, (Long)bag.get(1));
        assertEquals((Long)element2, (Long)bag.get(2));
        assertEquals((Long)element3, (Long)bag.get(3));
        assertEquals((Long)element4, (Long)bag.get(4));
        assertEquals((Long)element5, (Long)bag.get(5));
        assertEquals((Long)element6, (Long)bag.get(6));
        assertEquals((Long)0L, (Long)bag.get(7));

        bag.set(7, element7);
        assertEquals((Long)element0, (Long)bag.get(0));
        assertEquals((Long)element1, (Long)bag.get(1));
        assertEquals((Long)element2, (Long)bag.get(2));
        assertEquals((Long)element3, (Long)bag.get(3));
        assertEquals((Long)element4, (Long)bag.get(4));
        assertEquals((Long)element5, (Long)bag.get(5));
        assertEquals((Long)element6, (Long)bag.get(6));
        assertEquals((Long)element7, (Long)bag.get(7));

        bag.clear();

        assertEquals((Long)0L, (Long)bag.get(0));
        assertEquals((Long)0L, (Long)bag.get(1));
        assertEquals((Long)0L, (Long)bag.get(2));
        assertEquals((Long)0L, (Long)bag.get(3));
        assertEquals((Long)0L, (Long)bag.get(4));
        assertEquals((Long)0L, (Long)bag.get(5));
        assertEquals((Long)0L, (Long)bag.get(6));
        assertEquals((Long)0L, (Long)bag.get(7));
    }

    /**
     * Ensures that the bag contains the default value by default
     */
    @Test
    public void testDefault() {
        LongBag bag = new LongBag();
        assertEquals((Long)0L, (Long)bag.get(0));
        bag.set(0, 1L);
        assertEquals((Long)0L, (Long)bag.get(1));
        assertEquals((Long)0L, (Long)bag.get(2));
        assertEquals((Long)0L, (Long)bag.get(3));
    }

    /**
     * Ensures that the bag resizes correctly when out of capacity and that it
     * does not resize when queried for non-existing elements.
     */
    @Test
    public void testCapacity() {
        LongBag bag;

        bag = new LongBag();
        assertEquals(0, bag.buffer.length);
        bag.set(0, 1L);
        assertEquals(1, bag.buffer.length);
        bag.set(1, 1L);
        assertEquals(2, bag.buffer.length);
        bag.set(2, 1L);
        assertEquals(4, bag.buffer.length);
        bag.set(3, 1L);
        assertEquals(4, bag.buffer.length);
        bag.set(4, 1L);
        assertEquals(8, bag.buffer.length);
        bag.set(8, 1L);
        assertEquals(16, bag.buffer.length);
        bag.set(35, 1L);
        assertEquals(64, bag.buffer.length);

        bag = new LongBag();
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
        LongBag bag = new LongBag();
        for (int i = 0; i < 32; i++) {
            try {
                bag.set(-(1 << i), 1L);
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
        LongBag bag = new LongBag();
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
        LongBag bag;

        bag = new LongBag();
        bag.set(0, 1L);
        bag.set(1, 1L);
        bag.set(63, 1L);
        bag.clear();
        assertEquals((Long) 0L, (Long) bag.get(0));
        assertEquals((Long) 0L, (Long) bag.get(1));
        assertEquals((Long) 0L, (Long) bag.get(63));
        assertEquals(64, bag.buffer.length);

        Mask mask = new Mask();
        bag = new LongBag();
        bag.set(0, 1L);
        bag.set(1, 1L);
        bag.set(63, 1L);
        mask.set(63);
        mask.set(0);
        bag.clear(mask);
        assertEquals((Long) 0L, (Long) bag.get(0));
        assertNotEquals((Long) 0L, (Long) bag.get(1));
        assertEquals((Long) 0L, (Long) bag.get(63));
        mask.set(1);
        mask.set(457);
        bag.clear(mask);
        assertEquals((Long) 0L, (Long) bag.get(0));
        assertEquals((Long) 0L, (Long) bag.get(1));
        assertEquals((Long) 0L, (Long) bag.get(63));
        assertEquals(64, bag.buffer.length);
    }

    @Test
    public void testCopyFrom() {
        LongBag bag0, bag1;

        bag0 = new LongBag();
        bag0.set(0, 1L);
        bag0.set(5, 1L);

        bag1 = new LongBag();
        bag1.set(9, 1L);

        bag1.copyFrom(bag0, false);
        assertNotEquals((Long) 0L, (Long) bag1.get(0));
        assertNotEquals((Long) 0L, (Long) bag1.get(5));
        assertNotEquals((Long) 0L, (Long) bag1.get(9));

        bag1.copyFrom(bag0);
        assertNotEquals((Long) 0L, (Long) bag1.get(0));
        assertNotEquals((Long) 0L, (Long) bag1.get(5));
        assertEquals((Long) 0L, (Long) bag1.get(9));

        bag0.copyFrom(bag1);
    }
}
