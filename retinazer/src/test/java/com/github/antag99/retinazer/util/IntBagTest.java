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
public final class IntBagTest {
    /**
     * Ensures that the elements of a bag are actually stored
     */
    @Test
    public void testStorage() {
        IntBag bag = new IntBag();
        int element0 = 1;
        int element1 = 1;
        int element2 = 1;
        int element3 = 1;
        int element4 = 1;
        int element5 = 1;
        int element6 = 1;
        int element7 = 1;

        bag.set(0, element0);
        assertEquals((Integer)element0, (Integer)bag.get(0));
        assertEquals((Integer)0, (Integer)bag.get(1));
        assertEquals((Integer)0, (Integer)bag.get(2));
        assertEquals((Integer)0, (Integer)bag.get(3));
        assertEquals((Integer)0, (Integer)bag.get(4));
        assertEquals((Integer)0, (Integer)bag.get(5));
        assertEquals((Integer)0, (Integer)bag.get(6));
        assertEquals((Integer)0, (Integer)bag.get(7));

        bag.set(1, element1);
        assertEquals((Integer)element0, (Integer)bag.get(0));
        assertEquals((Integer)element1, (Integer)bag.get(1));
        assertEquals((Integer)0, (Integer)bag.get(2));
        assertEquals((Integer)0, (Integer)bag.get(3));
        assertEquals((Integer)0, (Integer)bag.get(4));
        assertEquals((Integer)0, (Integer)bag.get(5));
        assertEquals((Integer)0, (Integer)bag.get(6));
        assertEquals((Integer)0, (Integer)bag.get(7));

        bag.set(2, element2);
        assertEquals((Integer)element0, (Integer)bag.get(0));
        assertEquals((Integer)element1, (Integer)bag.get(1));
        assertEquals((Integer)element2, (Integer)bag.get(2));
        assertEquals((Integer)0, (Integer)bag.get(3));
        assertEquals((Integer)0, (Integer)bag.get(4));
        assertEquals((Integer)0, (Integer)bag.get(5));
        assertEquals((Integer)0, (Integer)bag.get(6));
        assertEquals((Integer)0, (Integer)bag.get(7));

        bag.set(3, element3);
        assertEquals((Integer)element0, (Integer)bag.get(0));
        assertEquals((Integer)element1, (Integer)bag.get(1));
        assertEquals((Integer)element2, (Integer)bag.get(2));
        assertEquals((Integer)element3, (Integer)bag.get(3));
        assertEquals((Integer)0, (Integer)bag.get(4));
        assertEquals((Integer)0, (Integer)bag.get(5));
        assertEquals((Integer)0, (Integer)bag.get(6));
        assertEquals((Integer)0, (Integer)bag.get(7));

        bag.set(4, element4);
        assertEquals((Integer)element0, (Integer)bag.get(0));
        assertEquals((Integer)element1, (Integer)bag.get(1));
        assertEquals((Integer)element2, (Integer)bag.get(2));
        assertEquals((Integer)element3, (Integer)bag.get(3));
        assertEquals((Integer)element4, (Integer)bag.get(4));
        assertEquals((Integer)0, (Integer)bag.get(5));
        assertEquals((Integer)0, (Integer)bag.get(6));
        assertEquals((Integer)0, (Integer)bag.get(7));

        bag.set(5, element5);
        assertEquals((Integer)element0, (Integer)bag.get(0));
        assertEquals((Integer)element1, (Integer)bag.get(1));
        assertEquals((Integer)element2, (Integer)bag.get(2));
        assertEquals((Integer)element3, (Integer)bag.get(3));
        assertEquals((Integer)element4, (Integer)bag.get(4));
        assertEquals((Integer)element5, (Integer)bag.get(5));
        assertEquals((Integer)0, (Integer)bag.get(6));
        assertEquals((Integer)0, (Integer)bag.get(7));

        bag.set(6, element6);
        assertEquals((Integer)element0, (Integer)bag.get(0));
        assertEquals((Integer)element1, (Integer)bag.get(1));
        assertEquals((Integer)element2, (Integer)bag.get(2));
        assertEquals((Integer)element3, (Integer)bag.get(3));
        assertEquals((Integer)element4, (Integer)bag.get(4));
        assertEquals((Integer)element5, (Integer)bag.get(5));
        assertEquals((Integer)element6, (Integer)bag.get(6));
        assertEquals((Integer)0, (Integer)bag.get(7));

        bag.set(7, element7);
        assertEquals((Integer)element0, (Integer)bag.get(0));
        assertEquals((Integer)element1, (Integer)bag.get(1));
        assertEquals((Integer)element2, (Integer)bag.get(2));
        assertEquals((Integer)element3, (Integer)bag.get(3));
        assertEquals((Integer)element4, (Integer)bag.get(4));
        assertEquals((Integer)element5, (Integer)bag.get(5));
        assertEquals((Integer)element6, (Integer)bag.get(6));
        assertEquals((Integer)element7, (Integer)bag.get(7));

        bag.clear();

        assertEquals((Integer)0, (Integer)bag.get(0));
        assertEquals((Integer)0, (Integer)bag.get(1));
        assertEquals((Integer)0, (Integer)bag.get(2));
        assertEquals((Integer)0, (Integer)bag.get(3));
        assertEquals((Integer)0, (Integer)bag.get(4));
        assertEquals((Integer)0, (Integer)bag.get(5));
        assertEquals((Integer)0, (Integer)bag.get(6));
        assertEquals((Integer)0, (Integer)bag.get(7));
    }

    /**
     * Ensures that the bag contains the default value by default
     */
    @Test
    public void testDefault() {
        IntBag bag = new IntBag();
        assertEquals((Integer)0, (Integer)bag.get(0));
        bag.set(0, 1);
        assertEquals((Integer)0, (Integer)bag.get(1));
        assertEquals((Integer)0, (Integer)bag.get(2));
        assertEquals((Integer)0, (Integer)bag.get(3));
    }

    /**
     * Ensures that the bag resizes correctly when out of capacity and that it
     * does not resize when queried for non-existing elements.
     */
    @Test
    public void testCapacity() {
        IntBag bag;

        bag = new IntBag();
        assertEquals(0, bag.buffer.length);
        bag.set(0, 1);
        assertEquals(1, bag.buffer.length);
        bag.set(1, 1);
        assertEquals(2, bag.buffer.length);
        bag.set(2, 1);
        assertEquals(4, bag.buffer.length);
        bag.set(3, 1);
        assertEquals(4, bag.buffer.length);
        bag.set(4, 1);
        assertEquals(8, bag.buffer.length);
        bag.set(8, 1);
        assertEquals(16, bag.buffer.length);
        bag.set(35, 1);
        assertEquals(64, bag.buffer.length);

        bag = new IntBag();
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
        IntBag bag = new IntBag();
        for (int i = 0; i < 32; i++) {
            try {
                bag.set(-(1 << i), 1);
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
        IntBag bag = new IntBag();
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
        IntBag bag;

        bag = new IntBag();
        bag.set(0, 1);
        bag.set(1, 1);
        bag.set(63, 1);
        bag.clear();
        assertEquals((Integer) 0, (Integer) bag.get(0));
        assertEquals((Integer) 0, (Integer) bag.get(1));
        assertEquals((Integer) 0, (Integer) bag.get(63));
        assertEquals(64, bag.buffer.length);

        Mask mask = new Mask();
        bag = new IntBag();
        bag.set(0, 1);
        bag.set(1, 1);
        bag.set(63, 1);
        mask.set(63);
        mask.set(0);
        bag.clear(mask);
        assertEquals((Integer) 0, (Integer) bag.get(0));
        assertNotEquals((Integer) 0, (Integer) bag.get(1));
        assertEquals((Integer) 0, (Integer) bag.get(63));
        mask.set(1);
        mask.set(457);
        bag.clear(mask);
        assertEquals((Integer) 0, (Integer) bag.get(0));
        assertEquals((Integer) 0, (Integer) bag.get(1));
        assertEquals((Integer) 0, (Integer) bag.get(63));
        assertEquals(64, bag.buffer.length);
    }

    @Test
    public void testCopyFrom() {
        // Test copyFrom(bag) and copyFrom(bag, clearExceeding)
        IntBag bag0, bag1;
        int e0 = 1, e1 = 1, e2 = 1, e3 = 1;

        bag0 = new IntBag();
        bag0.set(0, e0);
        bag0.set(3, e1);
        bag0.set(9, e2);
        bag1 = new IntBag();
        bag1.copyFrom(bag0);
        assertEquals((Integer) e0, (Integer) bag1.get(0));
        assertEquals((Integer) e1, (Integer) bag1.get(3));
        assertEquals((Integer) e2, (Integer) bag1.get(9));
        bag1 = new IntBag();
        bag1.set(53, e3);
        bag1.copyFrom(bag0, false);
        assertEquals((Integer) e0, (Integer) bag1.get(0));
        assertEquals((Integer) e1, (Integer) bag1.get(3));
        assertEquals((Integer) e2, (Integer) bag1.get(9));
        assertEquals((Integer) e3, (Integer) bag1.get(53));
        bag1 = new IntBag();
        bag1.set(53, e3);
        bag1.copyFrom(bag0, true);
        assertEquals((Integer) e0, (Integer) bag1.get(0));
        assertEquals((Integer) e1, (Integer) bag1.get(3));
        assertEquals((Integer) e2, (Integer) bag1.get(9));
        assertEquals((Integer) 0, (Integer) bag1.get(53));

        // Test copyFrom(bag, length) and copyFrom(bag, length, clearExceeding)
        bag0 = new IntBag();
        bag0.set(4, e0);
        bag0.set(7, e1);
        bag1 = new IntBag();
        bag1.copyFrom(bag0, 3);
        assertEquals((Integer) 0, (Integer) bag1.get(0));
        assertEquals((Integer) 0, (Integer) bag1.get(1));
        assertEquals((Integer) 0, (Integer) bag1.get(2));
        assertEquals((Integer) 0, (Integer) bag1.get(3));
        assertEquals((Integer) 0, (Integer) bag1.get(4));
        assertEquals((Integer) 0, (Integer) bag1.get(5));
        assertEquals((Integer) 0, (Integer) bag1.get(6));
        assertEquals((Integer) 0, (Integer) bag1.get(7));
        bag1 = new IntBag();
        bag1.copyFrom(bag0, 5);
        assertEquals((Integer) 0, (Integer) bag1.get(0));
        assertEquals((Integer) 0, (Integer) bag1.get(1));
        assertEquals((Integer) 0, (Integer) bag1.get(2));
        assertEquals((Integer) 0, (Integer) bag1.get(3));
        assertEquals((Integer) e0, (Integer) bag1.get(4));
        assertEquals((Integer) 0, (Integer) bag1.get(5));
        assertEquals((Integer) 0, (Integer) bag1.get(6));
        assertEquals((Integer) 0, (Integer) bag1.get(7));
        bag1 = new IntBag();
        bag1.set(8, e2);
        bag1.copyFrom(bag0, 8, true);
        assertEquals((Integer) 0, (Integer) bag1.get(0));
        assertEquals((Integer) 0, (Integer) bag1.get(1));
        assertEquals((Integer) 0, (Integer) bag1.get(2));
        assertEquals((Integer) 0, (Integer) bag1.get(3));
        assertEquals((Integer) e0, (Integer) bag1.get(4));
        assertEquals((Integer) 0, (Integer) bag1.get(5));
        assertEquals((Integer) 0, (Integer) bag1.get(6));
        assertEquals((Integer) e1, (Integer) bag1.get(7));
        assertEquals((Integer) 0, (Integer) bag1.get(8));
        assertEquals((Integer) 0, (Integer) bag1.get(9));

        // Test copyFrom(bag, fromOffset, length) and copyFrom(bag, fromOffset, length, clearExceeding)
        bag0 = new IntBag();
        bag0.set(0, e0);
        bag0.set(4, e1);
        bag0.set(6, e2);
        bag1 = new IntBag();
        bag1.copyFrom(bag0, 3, 2);
        assertEquals((Integer) 0, (Integer) bag1.get(0));
        assertEquals((Integer) e1, (Integer) bag1.get(1));
        assertEquals((Integer) 0, (Integer) bag1.get(2));
        assertEquals((Integer) 0, (Integer) bag1.get(3));
        assertEquals((Integer) 0, (Integer) bag1.get(4));
        assertEquals((Integer) 0, (Integer) bag1.get(5));
        assertEquals((Integer) 0, (Integer) bag1.get(6));
        assertEquals((Integer) 0, (Integer) bag1.get(7));
        assertEquals((Integer) 0, (Integer) bag1.get(8));
        bag0 = new IntBag();
        bag0.set(0, e0);
        bag0.set(4, e1);
        bag0.set(10, e2);
        bag1 = new IntBag();
        bag1.set(7, e3);
        bag1.copyFrom(bag0, 3, 2, false);
        assertEquals((Integer) 0, (Integer) bag1.get(0));
        assertEquals((Integer) e1, (Integer) bag1.get(1));
        assertEquals((Integer) 0, (Integer) bag1.get(2));
        assertEquals((Integer) 0, (Integer) bag1.get(3));
        assertEquals((Integer) 0, (Integer) bag1.get(4));
        assertEquals((Integer) 0, (Integer) bag1.get(5));
        assertEquals((Integer) 0, (Integer) bag1.get(6));
        assertEquals((Integer) e3, (Integer) bag1.get(7));
        assertEquals((Integer) 0, (Integer) bag1.get(8));
        assertEquals((Integer) 0, (Integer) bag1.get(9));
        assertEquals((Integer) 0, (Integer) bag1.get(10));
    }

    @Test
    public void testCopyPartFrom() {
        IntBag b0, b1;
        int e0 = 1, e1 = 1, e2 = 1, e3 = 1;
        b0 = new IntBag();
        b0.set(5, e0);
        b0.set(14, e1);
        b0.set(21, e2);
        b1 = new IntBag();
        b1.set(0, e3);
        b1.set(33, e0);
        b1.copyPartFrom(b0, 14, 1, 33);
        assertEquals((Integer) e1, (Integer) b1.get(1));
        assertEquals((Integer) e2, (Integer) b1.get(8));
        assertEquals((Integer) e3, (Integer) b1.get(0));
        assertEquals((Integer) 0, (Integer) b1.get(32));
        assertEquals((Integer) 0, (Integer) b1.get(33));
        assertEquals((Integer) 0, (Integer) b1.get(34));
        assertEquals((Integer) 0, (Integer) b1.get(35));
        b0 = new IntBag();
        b0.set(5, e0);
        b0.set(14, e1);
        b0.set(21, e2);
        b1 = new IntBag();
        b1.set(0, e3);
        b1.copyPartFrom(b0, 14, 1, 8);
        assertEquals((Integer) e1, (Integer) b1.get(1));
        assertEquals((Integer) e2, (Integer) b1.get(8));
        assertEquals((Integer) e3, (Integer) b1.get(0));
        assertEquals((Integer) 0, (Integer) b1.get(32));
        assertEquals((Integer) 0, (Integer) b1.get(33));
        assertEquals((Integer) 0, (Integer) b1.get(34));
        assertEquals((Integer) 0, (Integer) b1.get(35));
    }
}
