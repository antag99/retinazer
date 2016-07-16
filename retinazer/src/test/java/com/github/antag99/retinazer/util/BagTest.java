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
        // Test copyFrom(bag) and copyFrom(bag, clearExceeding)
        Bag<T> bag0, bag1;
        T e0 = (T) new Object(), e1 = (T) new Object(), e2 = (T) new Object(), e3 = (T) new Object();

        bag0 = new Bag<T>();
        bag0.set(0, e0);
        bag0.set(3, e1);
        bag0.set(9, e2);
        bag1 = new Bag<T>();
        bag1.copyFrom(bag0);
        assertEquals((T) e0, (T) bag1.get(0));
        assertEquals((T) e1, (T) bag1.get(3));
        assertEquals((T) e2, (T) bag1.get(9));
        bag1 = new Bag<T>();
        bag1.set(53, e3);
        bag1.copyFrom(bag0, false);
        assertEquals((T) e0, (T) bag1.get(0));
        assertEquals((T) e1, (T) bag1.get(3));
        assertEquals((T) e2, (T) bag1.get(9));
        assertEquals((T) e3, (T) bag1.get(53));
        bag1 = new Bag<T>();
        bag1.set(53, e3);
        bag1.copyFrom(bag0, true);
        assertEquals((T) e0, (T) bag1.get(0));
        assertEquals((T) e1, (T) bag1.get(3));
        assertEquals((T) e2, (T) bag1.get(9));
        assertEquals((T) null, (T) bag1.get(53));

        // Test copyFrom(bag, length) and copyFrom(bag, length, clearExceeding)
        bag0 = new Bag<T>();
        bag0.set(4, e0);
        bag0.set(7, e1);
        bag1 = new Bag<T>();
        bag1.copyFrom(bag0, 3);
        assertEquals((T) null, (T) bag1.get(0));
        assertEquals((T) null, (T) bag1.get(1));
        assertEquals((T) null, (T) bag1.get(2));
        assertEquals((T) null, (T) bag1.get(3));
        assertEquals((T) null, (T) bag1.get(4));
        assertEquals((T) null, (T) bag1.get(5));
        assertEquals((T) null, (T) bag1.get(6));
        assertEquals((T) null, (T) bag1.get(7));
        bag1 = new Bag<T>();
        bag1.copyFrom(bag0, 5);
        assertEquals((T) null, (T) bag1.get(0));
        assertEquals((T) null, (T) bag1.get(1));
        assertEquals((T) null, (T) bag1.get(2));
        assertEquals((T) null, (T) bag1.get(3));
        assertEquals((T) e0, (T) bag1.get(4));
        assertEquals((T) null, (T) bag1.get(5));
        assertEquals((T) null, (T) bag1.get(6));
        assertEquals((T) null, (T) bag1.get(7));
        bag1 = new Bag<T>();
        bag1.set(8, e2);
        bag1.copyFrom(bag0, 8, true);
        assertEquals((T) null, (T) bag1.get(0));
        assertEquals((T) null, (T) bag1.get(1));
        assertEquals((T) null, (T) bag1.get(2));
        assertEquals((T) null, (T) bag1.get(3));
        assertEquals((T) e0, (T) bag1.get(4));
        assertEquals((T) null, (T) bag1.get(5));
        assertEquals((T) null, (T) bag1.get(6));
        assertEquals((T) e1, (T) bag1.get(7));
        assertEquals((T) null, (T) bag1.get(8));
        assertEquals((T) null, (T) bag1.get(9));

        // Test copyFrom(bag, fromOffset, length) and copyFrom(bag, fromOffset, length, clearExceeding)
        bag0 = new Bag<T>();
        bag0.set(0, e0);
        bag0.set(4, e1);
        bag0.set(6, e2);
        bag1 = new Bag<T>();
        bag1.copyFrom(bag0, 3, 2);
        assertEquals((T) null, (T) bag1.get(0));
        assertEquals((T) e1, (T) bag1.get(1));
        assertEquals((T) null, (T) bag1.get(2));
        assertEquals((T) null, (T) bag1.get(3));
        assertEquals((T) null, (T) bag1.get(4));
        assertEquals((T) null, (T) bag1.get(5));
        assertEquals((T) null, (T) bag1.get(6));
        assertEquals((T) null, (T) bag1.get(7));
        assertEquals((T) null, (T) bag1.get(8));
        bag0 = new Bag<T>();
        bag0.set(0, e0);
        bag0.set(4, e1);
        bag0.set(10, e2);
        bag1 = new Bag<T>();
        bag1.set(7, e3);
        bag1.copyFrom(bag0, 3, 2, false);
        assertEquals((T) null, (T) bag1.get(0));
        assertEquals((T) e1, (T) bag1.get(1));
        assertEquals((T) null, (T) bag1.get(2));
        assertEquals((T) null, (T) bag1.get(3));
        assertEquals((T) null, (T) bag1.get(4));
        assertEquals((T) null, (T) bag1.get(5));
        assertEquals((T) null, (T) bag1.get(6));
        assertEquals((T) e3, (T) bag1.get(7));
        assertEquals((T) null, (T) bag1.get(8));
        assertEquals((T) null, (T) bag1.get(9));
        assertEquals((T) null, (T) bag1.get(10));
    }

    @Test
    public void testCopyPartFrom() {
        Bag<T> b0, b1;
        T e0 = (T) new Object(), e1 = (T) new Object(), e2 = (T) new Object(), e3 = (T) new Object();
        b0 = new Bag<T>();
        b0.set(5, e0);
        b0.set(14, e1);
        b0.set(21, e2);
        b1 = new Bag<T>();
        b1.set(0, e3);
        b1.set(33, e0);
        b1.copyPartFrom(b0, 14, 1, 33);
        assertEquals((T) e1, (T) b1.get(1));
        assertEquals((T) e2, (T) b1.get(8));
        assertEquals((T) e3, (T) b1.get(0));
        assertEquals((T) null, (T) b1.get(32));
        assertEquals((T) null, (T) b1.get(33));
        assertEquals((T) null, (T) b1.get(34));
        assertEquals((T) null, (T) b1.get(35));
        b0 = new Bag<T>();
        b0.set(5, e0);
        b0.set(14, e1);
        b0.set(21, e2);
        b1 = new Bag<T>();
        b1.set(0, e3);
        b1.copyPartFrom(b0, 14, 1, 8);
        assertEquals((T) e1, (T) b1.get(1));
        assertEquals((T) e2, (T) b1.get(8));
        assertEquals((T) e3, (T) b1.get(0));
        assertEquals((T) null, (T) b1.get(32));
        assertEquals((T) null, (T) b1.get(33));
        assertEquals((T) null, (T) b1.get(34));
        assertEquals((T) null, (T) b1.get(35));
    }
}
