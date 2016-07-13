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
public final class BagTest<T> {
    /**
     * Ensures that the elements of a bag are actually stored
     */
    @Test
    public void testStorage() {
        Bag<T> bag = new Bag<T>();
        T element0 = (T) new Object();
        T element1 = (T) new Object();
        T element2 = (T) new Object();
        T element3 = (T) new Object();
        T element4 = (T) new Object();
        T element5 = (T) new Object();
        T element6 = (T) new Object();
        T element7 = (T) new Object();

        bag.set(0, element0);
        assertEquals((T)element0, (T)bag.get(0));
        assertEquals((T)null, (T)bag.get(1));
        assertEquals((T)null, (T)bag.get(2));
        assertEquals((T)null, (T)bag.get(3));
        assertEquals((T)null, (T)bag.get(4));
        assertEquals((T)null, (T)bag.get(5));
        assertEquals((T)null, (T)bag.get(6));
        assertEquals((T)null, (T)bag.get(7));

        bag.set(1, element1);
        assertEquals((T)element0, (T)bag.get(0));
        assertEquals((T)element1, (T)bag.get(1));
        assertEquals((T)null, (T)bag.get(2));
        assertEquals((T)null, (T)bag.get(3));
        assertEquals((T)null, (T)bag.get(4));
        assertEquals((T)null, (T)bag.get(5));
        assertEquals((T)null, (T)bag.get(6));
        assertEquals((T)null, (T)bag.get(7));

        bag.set(2, element2);
        assertEquals((T)element0, (T)bag.get(0));
        assertEquals((T)element1, (T)bag.get(1));
        assertEquals((T)element2, (T)bag.get(2));
        assertEquals((T)null, (T)bag.get(3));
        assertEquals((T)null, (T)bag.get(4));
        assertEquals((T)null, (T)bag.get(5));
        assertEquals((T)null, (T)bag.get(6));
        assertEquals((T)null, (T)bag.get(7));

        bag.set(3, element3);
        assertEquals((T)element0, (T)bag.get(0));
        assertEquals((T)element1, (T)bag.get(1));
        assertEquals((T)element2, (T)bag.get(2));
        assertEquals((T)element3, (T)bag.get(3));
        assertEquals((T)null, (T)bag.get(4));
        assertEquals((T)null, (T)bag.get(5));
        assertEquals((T)null, (T)bag.get(6));
        assertEquals((T)null, (T)bag.get(7));

        bag.set(4, element4);
        assertEquals((T)element0, (T)bag.get(0));
        assertEquals((T)element1, (T)bag.get(1));
        assertEquals((T)element2, (T)bag.get(2));
        assertEquals((T)element3, (T)bag.get(3));
        assertEquals((T)element4, (T)bag.get(4));
        assertEquals((T)null, (T)bag.get(5));
        assertEquals((T)null, (T)bag.get(6));
        assertEquals((T)null, (T)bag.get(7));

        bag.set(5, element5);
        assertEquals((T)element0, (T)bag.get(0));
        assertEquals((T)element1, (T)bag.get(1));
        assertEquals((T)element2, (T)bag.get(2));
        assertEquals((T)element3, (T)bag.get(3));
        assertEquals((T)element4, (T)bag.get(4));
        assertEquals((T)element5, (T)bag.get(5));
        assertEquals((T)null, (T)bag.get(6));
        assertEquals((T)null, (T)bag.get(7));

        bag.set(6, element6);
        assertEquals((T)element0, (T)bag.get(0));
        assertEquals((T)element1, (T)bag.get(1));
        assertEquals((T)element2, (T)bag.get(2));
        assertEquals((T)element3, (T)bag.get(3));
        assertEquals((T)element4, (T)bag.get(4));
        assertEquals((T)element5, (T)bag.get(5));
        assertEquals((T)element6, (T)bag.get(6));
        assertEquals((T)null, (T)bag.get(7));

        bag.set(7, element7);
        assertEquals((T)element0, (T)bag.get(0));
        assertEquals((T)element1, (T)bag.get(1));
        assertEquals((T)element2, (T)bag.get(2));
        assertEquals((T)element3, (T)bag.get(3));
        assertEquals((T)element4, (T)bag.get(4));
        assertEquals((T)element5, (T)bag.get(5));
        assertEquals((T)element6, (T)bag.get(6));
        assertEquals((T)element7, (T)bag.get(7));

        bag.clear();

        assertEquals((T)null, (T)bag.get(0));
        assertEquals((T)null, (T)bag.get(1));
        assertEquals((T)null, (T)bag.get(2));
        assertEquals((T)null, (T)bag.get(3));
        assertEquals((T)null, (T)bag.get(4));
        assertEquals((T)null, (T)bag.get(5));
        assertEquals((T)null, (T)bag.get(6));
        assertEquals((T)null, (T)bag.get(7));
    }

    /**
     * Ensures that the bag contains the default value by default
     */
    @Test
    public void testDefault() {
        Bag<T> bag = new Bag<T>();
        assertEquals((T)null, (T)bag.get(0));
        bag.set(0, (T) new Object());
        assertEquals((T)null, (T)bag.get(1));
        assertEquals((T)null, (T)bag.get(2));
        assertEquals((T)null, (T)bag.get(3));
    }

    /**
     * Ensures that the bag resizes correctly when out of capacity and that it
     * does not resize when queried for non-existing elements.
     */
    @Test
    public void testCapacity() {
        Bag<T> bag;

        bag = new Bag<T>();
        assertEquals(0, bag.buffer.length);
        bag.set(0, (T) new Object());
        assertEquals(1, bag.buffer.length);
        bag.set(1, (T) new Object());
        assertEquals(2, bag.buffer.length);
        bag.set(2, (T) new Object());
        assertEquals(4, bag.buffer.length);
        bag.set(3, (T) new Object());
        assertEquals(4, bag.buffer.length);
        bag.set(4, (T) new Object());
        assertEquals(8, bag.buffer.length);
        bag.set(8, (T) new Object());
        assertEquals(16, bag.buffer.length);
        bag.set(35, (T) new Object());
        assertEquals(64, bag.buffer.length);

        bag = new Bag<T>();
        for (int i = 0; i < 32; i++) {
            bag.get((1 << i) - 1);
            assertEquals(0, bag.buffer.length);
        }
        bag.get(Integer.MAX_VALUE);
        assertEquals(0, bag.buffer.length);
    }

    @Test
    public void testNextPowerOfTwo() {
        assertEquals(1, Bag.nextPowerOfTwo(0));
        assertEquals(1, Bag.nextPowerOfTwo(1));
        assertEquals(2, Bag.nextPowerOfTwo(2));
        assertEquals(4, Bag.nextPowerOfTwo(3));
        assertEquals(1 << 31, Bag.nextPowerOfTwo((1 << 30) + 1));
    }

    /**
     * When a negative index is used, an {@link IndexOutOfBoundsException} should be thrown.
     */
    @Test
    public void testIndexOutOfBoundsException() {
        Bag<T> bag = new Bag<T>();
        for (int i = 0; i < 32; i++) {
            try {
                bag.set(-(1 << i), (T) new Object());
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
        Bag<T> bag = new Bag<T>();
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
        Bag<T> bag;

        bag = new Bag<T>();
        bag.set(0, (T) new Object());
        bag.set(1, (T) new Object());
        bag.set(63, (T) new Object());
        bag.clear();
        assertEquals((T) null, (T) bag.get(0));
        assertEquals((T) null, (T) bag.get(1));
        assertEquals((T) null, (T) bag.get(63));
        assertEquals(64, bag.buffer.length);

        Mask mask = new Mask();
        bag = new Bag<T>();
        bag.set(0, (T) new Object());
        bag.set(1, (T) new Object());
        bag.set(63, (T) new Object());
        mask.set(63);
        mask.set(0);
        bag.clear(mask);
        assertEquals((T) null, (T) bag.get(0));
        assertNotEquals((T) null, (T) bag.get(1));
        assertEquals((T) null, (T) bag.get(63));
        mask.set(1);
        mask.set(457);
        bag.clear(mask);
        assertEquals((T) null, (T) bag.get(0));
        assertEquals((T) null, (T) bag.get(1));
        assertEquals((T) null, (T) bag.get(63));
        assertEquals(64, bag.buffer.length);
    }

    @Test
    public void testCopyFrom() {
        Bag<T> bag0, bag1;

        bag0 = new Bag<T>();
        bag0.set(0, (T) new Object());
        bag0.set(5, (T) new Object());

        bag1 = new Bag<T>();
        bag1.set(9, (T) new Object());

        bag1.copyFrom(bag0, false);
        assertNotEquals((T) null, (T) bag1.get(0));
        assertNotEquals((T) null, (T) bag1.get(5));
        assertNotEquals((T) null, (T) bag1.get(9));

        bag1.copyFrom(bag0);
        assertNotEquals((T) null, (T) bag1.get(0));
        assertNotEquals((T) null, (T) bag1.get(5));
        assertEquals((T) null, (T) bag1.get(9));

        bag0.copyFrom(bag1);
    }
}
