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
public final class CharBagTest {
    /**
     * Ensures that the elements of a bag are actually stored
     */
    @Test
    public void testStorage() {
        CharBag bag = new CharBag();
        char element0 = (char) 1;
        char element1 = (char) 1;
        char element2 = (char) 1;
        char element3 = (char) 1;
        char element4 = (char) 1;
        char element5 = (char) 1;
        char element6 = (char) 1;
        char element7 = (char) 1;

        bag.set(0, element0);
        assertEquals((Character)element0, (Character)bag.get(0));
        assertEquals((Character)(char) 0, (Character)bag.get(1));
        assertEquals((Character)(char) 0, (Character)bag.get(2));
        assertEquals((Character)(char) 0, (Character)bag.get(3));
        assertEquals((Character)(char) 0, (Character)bag.get(4));
        assertEquals((Character)(char) 0, (Character)bag.get(5));
        assertEquals((Character)(char) 0, (Character)bag.get(6));
        assertEquals((Character)(char) 0, (Character)bag.get(7));

        bag.set(1, element1);
        assertEquals((Character)element0, (Character)bag.get(0));
        assertEquals((Character)element1, (Character)bag.get(1));
        assertEquals((Character)(char) 0, (Character)bag.get(2));
        assertEquals((Character)(char) 0, (Character)bag.get(3));
        assertEquals((Character)(char) 0, (Character)bag.get(4));
        assertEquals((Character)(char) 0, (Character)bag.get(5));
        assertEquals((Character)(char) 0, (Character)bag.get(6));
        assertEquals((Character)(char) 0, (Character)bag.get(7));

        bag.set(2, element2);
        assertEquals((Character)element0, (Character)bag.get(0));
        assertEquals((Character)element1, (Character)bag.get(1));
        assertEquals((Character)element2, (Character)bag.get(2));
        assertEquals((Character)(char) 0, (Character)bag.get(3));
        assertEquals((Character)(char) 0, (Character)bag.get(4));
        assertEquals((Character)(char) 0, (Character)bag.get(5));
        assertEquals((Character)(char) 0, (Character)bag.get(6));
        assertEquals((Character)(char) 0, (Character)bag.get(7));

        bag.set(3, element3);
        assertEquals((Character)element0, (Character)bag.get(0));
        assertEquals((Character)element1, (Character)bag.get(1));
        assertEquals((Character)element2, (Character)bag.get(2));
        assertEquals((Character)element3, (Character)bag.get(3));
        assertEquals((Character)(char) 0, (Character)bag.get(4));
        assertEquals((Character)(char) 0, (Character)bag.get(5));
        assertEquals((Character)(char) 0, (Character)bag.get(6));
        assertEquals((Character)(char) 0, (Character)bag.get(7));

        bag.set(4, element4);
        assertEquals((Character)element0, (Character)bag.get(0));
        assertEquals((Character)element1, (Character)bag.get(1));
        assertEquals((Character)element2, (Character)bag.get(2));
        assertEquals((Character)element3, (Character)bag.get(3));
        assertEquals((Character)element4, (Character)bag.get(4));
        assertEquals((Character)(char) 0, (Character)bag.get(5));
        assertEquals((Character)(char) 0, (Character)bag.get(6));
        assertEquals((Character)(char) 0, (Character)bag.get(7));

        bag.set(5, element5);
        assertEquals((Character)element0, (Character)bag.get(0));
        assertEquals((Character)element1, (Character)bag.get(1));
        assertEquals((Character)element2, (Character)bag.get(2));
        assertEquals((Character)element3, (Character)bag.get(3));
        assertEquals((Character)element4, (Character)bag.get(4));
        assertEquals((Character)element5, (Character)bag.get(5));
        assertEquals((Character)(char) 0, (Character)bag.get(6));
        assertEquals((Character)(char) 0, (Character)bag.get(7));

        bag.set(6, element6);
        assertEquals((Character)element0, (Character)bag.get(0));
        assertEquals((Character)element1, (Character)bag.get(1));
        assertEquals((Character)element2, (Character)bag.get(2));
        assertEquals((Character)element3, (Character)bag.get(3));
        assertEquals((Character)element4, (Character)bag.get(4));
        assertEquals((Character)element5, (Character)bag.get(5));
        assertEquals((Character)element6, (Character)bag.get(6));
        assertEquals((Character)(char) 0, (Character)bag.get(7));

        bag.set(7, element7);
        assertEquals((Character)element0, (Character)bag.get(0));
        assertEquals((Character)element1, (Character)bag.get(1));
        assertEquals((Character)element2, (Character)bag.get(2));
        assertEquals((Character)element3, (Character)bag.get(3));
        assertEquals((Character)element4, (Character)bag.get(4));
        assertEquals((Character)element5, (Character)bag.get(5));
        assertEquals((Character)element6, (Character)bag.get(6));
        assertEquals((Character)element7, (Character)bag.get(7));

        bag.clear();

        assertEquals((Character)(char) 0, (Character)bag.get(0));
        assertEquals((Character)(char) 0, (Character)bag.get(1));
        assertEquals((Character)(char) 0, (Character)bag.get(2));
        assertEquals((Character)(char) 0, (Character)bag.get(3));
        assertEquals((Character)(char) 0, (Character)bag.get(4));
        assertEquals((Character)(char) 0, (Character)bag.get(5));
        assertEquals((Character)(char) 0, (Character)bag.get(6));
        assertEquals((Character)(char) 0, (Character)bag.get(7));
    }

    /**
     * Ensures that the bag contains the default value by default
     */
    @Test
    public void testDefault() {
        CharBag bag = new CharBag();
        assertEquals((Character)(char) 0, (Character)bag.get(0));
        bag.set(0, (char) 1);
        assertEquals((Character)(char) 0, (Character)bag.get(1));
        assertEquals((Character)(char) 0, (Character)bag.get(2));
        assertEquals((Character)(char) 0, (Character)bag.get(3));
    }

    /**
     * Ensures that the bag resizes correctly when out of capacity and that it
     * does not resize when queried for non-existing elements.
     */
    @Test
    public void testCapacity() {
        CharBag bag;

        bag = new CharBag();
        assertEquals(0, bag.buffer.length);
        bag.set(0, (char) 1);
        assertEquals(1, bag.buffer.length);
        bag.set(1, (char) 1);
        assertEquals(2, bag.buffer.length);
        bag.set(2, (char) 1);
        assertEquals(4, bag.buffer.length);
        bag.set(3, (char) 1);
        assertEquals(4, bag.buffer.length);
        bag.set(4, (char) 1);
        assertEquals(8, bag.buffer.length);
        bag.set(8, (char) 1);
        assertEquals(16, bag.buffer.length);
        bag.set(35, (char) 1);
        assertEquals(64, bag.buffer.length);

        bag = new CharBag();
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
        CharBag bag = new CharBag();
        for (int i = 0; i < 32; i++) {
            try {
                bag.set(-(1 << i), (char) 1);
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
        CharBag bag = new CharBag();
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
        CharBag bag;

        bag = new CharBag();
        bag.set(0, (char) 1);
        bag.set(1, (char) 1);
        bag.set(63, (char) 1);
        bag.clear();
        assertEquals((Character) (char) 0, (Character) bag.get(0));
        assertEquals((Character) (char) 0, (Character) bag.get(1));
        assertEquals((Character) (char) 0, (Character) bag.get(63));
        assertEquals(64, bag.buffer.length);

        Mask mask = new Mask();
        bag = new CharBag();
        bag.set(0, (char) 1);
        bag.set(1, (char) 1);
        bag.set(63, (char) 1);
        mask.set(63);
        mask.set(0);
        bag.clear(mask);
        assertEquals((Character) (char) 0, (Character) bag.get(0));
        assertNotEquals((Character) (char) 0, (Character) bag.get(1));
        assertEquals((Character) (char) 0, (Character) bag.get(63));
        mask.set(1);
        mask.set(457);
        bag.clear(mask);
        assertEquals((Character) (char) 0, (Character) bag.get(0));
        assertEquals((Character) (char) 0, (Character) bag.get(1));
        assertEquals((Character) (char) 0, (Character) bag.get(63));
        assertEquals(64, bag.buffer.length);
    }

    @Test
    public void testCopyFrom() {
        CharBag bag0, bag1;

        bag0 = new CharBag();
        bag0.set(0, (char) 1);
        bag0.set(5, (char) 1);

        bag1 = new CharBag();
        bag1.set(9, (char) 1);

        bag1.copyFrom(bag0, false);
        assertNotEquals((Character) (char) 0, (Character) bag1.get(0));
        assertNotEquals((Character) (char) 0, (Character) bag1.get(5));
        assertNotEquals((Character) (char) 0, (Character) bag1.get(9));

        bag1.copyFrom(bag0);
        assertNotEquals((Character) (char) 0, (Character) bag1.get(0));
        assertNotEquals((Character) (char) 0, (Character) bag1.get(5));
        assertEquals((Character) (char) 0, (Character) bag1.get(9));

        bag0.copyFrom(bag1);
    }
}
