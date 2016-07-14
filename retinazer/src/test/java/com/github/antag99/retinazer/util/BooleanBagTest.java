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
public final class BooleanBagTest {
    /**
     * Ensures that the elements of a bag are actually stored
     */
    @Test
    public void testStorage() {
        BooleanBag bag = new BooleanBag();
        boolean element0 = true;
        boolean element1 = true;
        boolean element2 = true;
        boolean element3 = true;
        boolean element4 = true;
        boolean element5 = true;
        boolean element6 = true;
        boolean element7 = true;

        bag.set(0, element0);
        assertEquals((Boolean)element0, (Boolean)bag.get(0));
        assertEquals((Boolean)false, (Boolean)bag.get(1));
        assertEquals((Boolean)false, (Boolean)bag.get(2));
        assertEquals((Boolean)false, (Boolean)bag.get(3));
        assertEquals((Boolean)false, (Boolean)bag.get(4));
        assertEquals((Boolean)false, (Boolean)bag.get(5));
        assertEquals((Boolean)false, (Boolean)bag.get(6));
        assertEquals((Boolean)false, (Boolean)bag.get(7));

        bag.set(1, element1);
        assertEquals((Boolean)element0, (Boolean)bag.get(0));
        assertEquals((Boolean)element1, (Boolean)bag.get(1));
        assertEquals((Boolean)false, (Boolean)bag.get(2));
        assertEquals((Boolean)false, (Boolean)bag.get(3));
        assertEquals((Boolean)false, (Boolean)bag.get(4));
        assertEquals((Boolean)false, (Boolean)bag.get(5));
        assertEquals((Boolean)false, (Boolean)bag.get(6));
        assertEquals((Boolean)false, (Boolean)bag.get(7));

        bag.set(2, element2);
        assertEquals((Boolean)element0, (Boolean)bag.get(0));
        assertEquals((Boolean)element1, (Boolean)bag.get(1));
        assertEquals((Boolean)element2, (Boolean)bag.get(2));
        assertEquals((Boolean)false, (Boolean)bag.get(3));
        assertEquals((Boolean)false, (Boolean)bag.get(4));
        assertEquals((Boolean)false, (Boolean)bag.get(5));
        assertEquals((Boolean)false, (Boolean)bag.get(6));
        assertEquals((Boolean)false, (Boolean)bag.get(7));

        bag.set(3, element3);
        assertEquals((Boolean)element0, (Boolean)bag.get(0));
        assertEquals((Boolean)element1, (Boolean)bag.get(1));
        assertEquals((Boolean)element2, (Boolean)bag.get(2));
        assertEquals((Boolean)element3, (Boolean)bag.get(3));
        assertEquals((Boolean)false, (Boolean)bag.get(4));
        assertEquals((Boolean)false, (Boolean)bag.get(5));
        assertEquals((Boolean)false, (Boolean)bag.get(6));
        assertEquals((Boolean)false, (Boolean)bag.get(7));

        bag.set(4, element4);
        assertEquals((Boolean)element0, (Boolean)bag.get(0));
        assertEquals((Boolean)element1, (Boolean)bag.get(1));
        assertEquals((Boolean)element2, (Boolean)bag.get(2));
        assertEquals((Boolean)element3, (Boolean)bag.get(3));
        assertEquals((Boolean)element4, (Boolean)bag.get(4));
        assertEquals((Boolean)false, (Boolean)bag.get(5));
        assertEquals((Boolean)false, (Boolean)bag.get(6));
        assertEquals((Boolean)false, (Boolean)bag.get(7));

        bag.set(5, element5);
        assertEquals((Boolean)element0, (Boolean)bag.get(0));
        assertEquals((Boolean)element1, (Boolean)bag.get(1));
        assertEquals((Boolean)element2, (Boolean)bag.get(2));
        assertEquals((Boolean)element3, (Boolean)bag.get(3));
        assertEquals((Boolean)element4, (Boolean)bag.get(4));
        assertEquals((Boolean)element5, (Boolean)bag.get(5));
        assertEquals((Boolean)false, (Boolean)bag.get(6));
        assertEquals((Boolean)false, (Boolean)bag.get(7));

        bag.set(6, element6);
        assertEquals((Boolean)element0, (Boolean)bag.get(0));
        assertEquals((Boolean)element1, (Boolean)bag.get(1));
        assertEquals((Boolean)element2, (Boolean)bag.get(2));
        assertEquals((Boolean)element3, (Boolean)bag.get(3));
        assertEquals((Boolean)element4, (Boolean)bag.get(4));
        assertEquals((Boolean)element5, (Boolean)bag.get(5));
        assertEquals((Boolean)element6, (Boolean)bag.get(6));
        assertEquals((Boolean)false, (Boolean)bag.get(7));

        bag.set(7, element7);
        assertEquals((Boolean)element0, (Boolean)bag.get(0));
        assertEquals((Boolean)element1, (Boolean)bag.get(1));
        assertEquals((Boolean)element2, (Boolean)bag.get(2));
        assertEquals((Boolean)element3, (Boolean)bag.get(3));
        assertEquals((Boolean)element4, (Boolean)bag.get(4));
        assertEquals((Boolean)element5, (Boolean)bag.get(5));
        assertEquals((Boolean)element6, (Boolean)bag.get(6));
        assertEquals((Boolean)element7, (Boolean)bag.get(7));

        bag.clear();

        assertEquals((Boolean)false, (Boolean)bag.get(0));
        assertEquals((Boolean)false, (Boolean)bag.get(1));
        assertEquals((Boolean)false, (Boolean)bag.get(2));
        assertEquals((Boolean)false, (Boolean)bag.get(3));
        assertEquals((Boolean)false, (Boolean)bag.get(4));
        assertEquals((Boolean)false, (Boolean)bag.get(5));
        assertEquals((Boolean)false, (Boolean)bag.get(6));
        assertEquals((Boolean)false, (Boolean)bag.get(7));
    }

    /**
     * Ensures that the bag contains the default value by default
     */
    @Test
    public void testDefault() {
        BooleanBag bag = new BooleanBag();
        assertEquals((Boolean)false, (Boolean)bag.get(0));
        bag.set(0, true);
        assertEquals((Boolean)false, (Boolean)bag.get(1));
        assertEquals((Boolean)false, (Boolean)bag.get(2));
        assertEquals((Boolean)false, (Boolean)bag.get(3));
    }

    /**
     * Ensures that the bag resizes correctly when out of capacity and that it
     * does not resize when queried for non-existing elements.
     */
    @Test
    public void testCapacity() {
        BooleanBag bag;

        bag = new BooleanBag();
        assertEquals(0, bag.buffer.length);
        bag.set(0, true);
        assertEquals(1, bag.buffer.length);
        bag.set(1, true);
        assertEquals(2, bag.buffer.length);
        bag.set(2, true);
        assertEquals(4, bag.buffer.length);
        bag.set(3, true);
        assertEquals(4, bag.buffer.length);
        bag.set(4, true);
        assertEquals(8, bag.buffer.length);
        bag.set(8, true);
        assertEquals(16, bag.buffer.length);
        bag.set(35, true);
        assertEquals(64, bag.buffer.length);

        bag = new BooleanBag();
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
        BooleanBag bag = new BooleanBag();
        for (int i = 0; i < 32; i++) {
            try {
                bag.set(-(1 << i), true);
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
        BooleanBag bag = new BooleanBag();
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
        BooleanBag bag;

        bag = new BooleanBag();
        bag.set(0, true);
        bag.set(1, true);
        bag.set(63, true);
        bag.clear();
        assertEquals((Boolean) false, (Boolean) bag.get(0));
        assertEquals((Boolean) false, (Boolean) bag.get(1));
        assertEquals((Boolean) false, (Boolean) bag.get(63));
        assertEquals(64, bag.buffer.length);

        Mask mask = new Mask();
        bag = new BooleanBag();
        bag.set(0, true);
        bag.set(1, true);
        bag.set(63, true);
        mask.set(63);
        mask.set(0);
        bag.clear(mask);
        assertEquals((Boolean) false, (Boolean) bag.get(0));
        assertNotEquals((Boolean) false, (Boolean) bag.get(1));
        assertEquals((Boolean) false, (Boolean) bag.get(63));
        mask.set(1);
        mask.set(457);
        bag.clear(mask);
        assertEquals((Boolean) false, (Boolean) bag.get(0));
        assertEquals((Boolean) false, (Boolean) bag.get(1));
        assertEquals((Boolean) false, (Boolean) bag.get(63));
        assertEquals(64, bag.buffer.length);
    }

    @Test
    public void testCopyFrom() {
        // Test copyFrom(bag) and copyFrom(bag, clearExceeding)
        BooleanBag bag0, bag1;
        boolean e0 = true, e1 = true, e2 = true, e3 = true;

        bag0 = new BooleanBag();
        bag0.set(0, e0);
        bag0.set(3, e1);
        bag0.set(9, e2);
        bag1 = new BooleanBag();
        bag1.copyFrom(bag0);
        assertEquals((Boolean) e0, (Boolean) bag1.get(0));
        assertEquals((Boolean) e1, (Boolean) bag1.get(3));
        assertEquals((Boolean) e2, (Boolean) bag1.get(9));
        bag1 = new BooleanBag();
        bag1.set(53, e3);
        bag1.copyFrom(bag0, false);
        assertEquals((Boolean) e0, (Boolean) bag1.get(0));
        assertEquals((Boolean) e1, (Boolean) bag1.get(3));
        assertEquals((Boolean) e2, (Boolean) bag1.get(9));
        assertEquals((Boolean) e3, (Boolean) bag1.get(53));
        bag1 = new BooleanBag();
        bag1.set(53, e3);
        bag1.copyFrom(bag0, true);
        assertEquals((Boolean) e0, (Boolean) bag1.get(0));
        assertEquals((Boolean) e1, (Boolean) bag1.get(3));
        assertEquals((Boolean) e2, (Boolean) bag1.get(9));
        assertEquals((Boolean) false, (Boolean) bag1.get(53));

        // Test copyFrom(bag, length) and copyFrom(bag, length, clearExceeding)
        bag0 = new BooleanBag();
        bag0.set(4, e0);
        bag0.set(7, e1);
        bag1 = new BooleanBag();
        bag1.copyFrom(bag0, 3);
        assertEquals((Boolean) false, (Boolean) bag1.get(0));
        assertEquals((Boolean) false, (Boolean) bag1.get(1));
        assertEquals((Boolean) false, (Boolean) bag1.get(2));
        assertEquals((Boolean) false, (Boolean) bag1.get(3));
        assertEquals((Boolean) false, (Boolean) bag1.get(4));
        assertEquals((Boolean) false, (Boolean) bag1.get(5));
        assertEquals((Boolean) false, (Boolean) bag1.get(6));
        assertEquals((Boolean) false, (Boolean) bag1.get(7));
        bag1 = new BooleanBag();
        bag1.copyFrom(bag0, 5);
        assertEquals((Boolean) false, (Boolean) bag1.get(0));
        assertEquals((Boolean) false, (Boolean) bag1.get(1));
        assertEquals((Boolean) false, (Boolean) bag1.get(2));
        assertEquals((Boolean) false, (Boolean) bag1.get(3));
        assertEquals((Boolean) e0, (Boolean) bag1.get(4));
        assertEquals((Boolean) false, (Boolean) bag1.get(5));
        assertEquals((Boolean) false, (Boolean) bag1.get(6));
        assertEquals((Boolean) false, (Boolean) bag1.get(7));
        bag1 = new BooleanBag();
        bag1.set(8, e2);
        bag1.copyFrom(bag0, 8, true);
        assertEquals((Boolean) false, (Boolean) bag1.get(0));
        assertEquals((Boolean) false, (Boolean) bag1.get(1));
        assertEquals((Boolean) false, (Boolean) bag1.get(2));
        assertEquals((Boolean) false, (Boolean) bag1.get(3));
        assertEquals((Boolean) e0, (Boolean) bag1.get(4));
        assertEquals((Boolean) false, (Boolean) bag1.get(5));
        assertEquals((Boolean) false, (Boolean) bag1.get(6));
        assertEquals((Boolean) e1, (Boolean) bag1.get(7));
        assertEquals((Boolean) false, (Boolean) bag1.get(8));
        assertEquals((Boolean) false, (Boolean) bag1.get(9));

        // Test copyFrom(bag, fromOffset, length) and copyFrom(bag, fromOffset, length, clearExceeding)
        bag0 = new BooleanBag();
        bag0.set(0, e0);
        bag0.set(4, e1);
        bag0.set(6, e2);
        bag1 = new BooleanBag();
        bag1.copyFrom(bag0, 3, 2);
        assertEquals((Boolean) false, (Boolean) bag1.get(0));
        assertEquals((Boolean) e1, (Boolean) bag1.get(1));
        assertEquals((Boolean) false, (Boolean) bag1.get(2));
        assertEquals((Boolean) false, (Boolean) bag1.get(3));
        assertEquals((Boolean) false, (Boolean) bag1.get(4));
        assertEquals((Boolean) false, (Boolean) bag1.get(5));
        assertEquals((Boolean) false, (Boolean) bag1.get(6));
        assertEquals((Boolean) false, (Boolean) bag1.get(7));
        assertEquals((Boolean) false, (Boolean) bag1.get(8));
        bag0 = new BooleanBag();
        bag0.set(0, e0);
        bag0.set(4, e1);
        bag0.set(10, e2);
        bag1 = new BooleanBag();
        bag1.set(7, e3);
        bag1.copyFrom(bag0, 3, 2, false);
        assertEquals((Boolean) false, (Boolean) bag1.get(0));
        assertEquals((Boolean) e1, (Boolean) bag1.get(1));
        assertEquals((Boolean) false, (Boolean) bag1.get(2));
        assertEquals((Boolean) false, (Boolean) bag1.get(3));
        assertEquals((Boolean) false, (Boolean) bag1.get(4));
        assertEquals((Boolean) false, (Boolean) bag1.get(5));
        assertEquals((Boolean) false, (Boolean) bag1.get(6));
        assertEquals((Boolean) e3, (Boolean) bag1.get(7));
        assertEquals((Boolean) false, (Boolean) bag1.get(8));
        assertEquals((Boolean) false, (Boolean) bag1.get(9));
        assertEquals((Boolean) false, (Boolean) bag1.get(10));
    }

    @Test
    public void testCopyPartFrom() {
        BooleanBag b0, b1;
        boolean e0 = true, e1 = true, e2 = true, e3 = true;
        b0 = new BooleanBag();
        b0.set(5, e0);
        b0.set(14, e1);
        b0.set(21, e2);
        b1 = new BooleanBag();
        b1.set(0, e3);
        b1.set(33, e0);
        b1.copyPartFrom(b0, 14, 1, 33);
        assertEquals((Boolean) e1, (Boolean) b1.get(1));
        assertEquals((Boolean) e2, (Boolean) b1.get(8));
        assertEquals((Boolean) e3, (Boolean) b1.get(0));
        assertEquals((Boolean) false, (Boolean) b1.get(32));
        assertEquals((Boolean) false, (Boolean) b1.get(33));
        assertEquals((Boolean) false, (Boolean) b1.get(34));
        assertEquals((Boolean) false, (Boolean) b1.get(35));
        b0 = new BooleanBag();
        b0.set(5, e0);
        b0.set(14, e1);
        b0.set(21, e2);
        b1 = new BooleanBag();
        b1.set(0, e3);
        b1.copyPartFrom(b0, 14, 1, 8);
        assertEquals((Boolean) e1, (Boolean) b1.get(1));
        assertEquals((Boolean) e2, (Boolean) b1.get(8));
        assertEquals((Boolean) e3, (Boolean) b1.get(0));
        assertEquals((Boolean) false, (Boolean) b1.get(32));
        assertEquals((Boolean) false, (Boolean) b1.get(33));
        assertEquals((Boolean) false, (Boolean) b1.get(34));
        assertEquals((Boolean) false, (Boolean) b1.get(35));
    }
}
