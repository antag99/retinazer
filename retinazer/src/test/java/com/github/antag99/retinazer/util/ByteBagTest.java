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
public final class ByteBagTest {
    /**
     * Ensures that the elements of a bag are actually stored
     */
    @Test
    public void testStorage() {
        ByteBag bag = new ByteBag();
        byte element0 = (byte) 1;
        byte element1 = (byte) 1;
        byte element2 = (byte) 1;
        byte element3 = (byte) 1;
        byte element4 = (byte) 1;
        byte element5 = (byte) 1;
        byte element6 = (byte) 1;
        byte element7 = (byte) 1;

        bag.set(0, element0);
        assertEquals((Byte)element0, (Byte)bag.get(0));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(1));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(2));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(3));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(4));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(5));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(6));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(7));

        bag.set(1, element1);
        assertEquals((Byte)element0, (Byte)bag.get(0));
        assertEquals((Byte)element1, (Byte)bag.get(1));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(2));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(3));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(4));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(5));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(6));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(7));

        bag.set(2, element2);
        assertEquals((Byte)element0, (Byte)bag.get(0));
        assertEquals((Byte)element1, (Byte)bag.get(1));
        assertEquals((Byte)element2, (Byte)bag.get(2));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(3));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(4));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(5));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(6));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(7));

        bag.set(3, element3);
        assertEquals((Byte)element0, (Byte)bag.get(0));
        assertEquals((Byte)element1, (Byte)bag.get(1));
        assertEquals((Byte)element2, (Byte)bag.get(2));
        assertEquals((Byte)element3, (Byte)bag.get(3));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(4));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(5));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(6));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(7));

        bag.set(4, element4);
        assertEquals((Byte)element0, (Byte)bag.get(0));
        assertEquals((Byte)element1, (Byte)bag.get(1));
        assertEquals((Byte)element2, (Byte)bag.get(2));
        assertEquals((Byte)element3, (Byte)bag.get(3));
        assertEquals((Byte)element4, (Byte)bag.get(4));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(5));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(6));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(7));

        bag.set(5, element5);
        assertEquals((Byte)element0, (Byte)bag.get(0));
        assertEquals((Byte)element1, (Byte)bag.get(1));
        assertEquals((Byte)element2, (Byte)bag.get(2));
        assertEquals((Byte)element3, (Byte)bag.get(3));
        assertEquals((Byte)element4, (Byte)bag.get(4));
        assertEquals((Byte)element5, (Byte)bag.get(5));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(6));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(7));

        bag.set(6, element6);
        assertEquals((Byte)element0, (Byte)bag.get(0));
        assertEquals((Byte)element1, (Byte)bag.get(1));
        assertEquals((Byte)element2, (Byte)bag.get(2));
        assertEquals((Byte)element3, (Byte)bag.get(3));
        assertEquals((Byte)element4, (Byte)bag.get(4));
        assertEquals((Byte)element5, (Byte)bag.get(5));
        assertEquals((Byte)element6, (Byte)bag.get(6));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(7));

        bag.set(7, element7);
        assertEquals((Byte)element0, (Byte)bag.get(0));
        assertEquals((Byte)element1, (Byte)bag.get(1));
        assertEquals((Byte)element2, (Byte)bag.get(2));
        assertEquals((Byte)element3, (Byte)bag.get(3));
        assertEquals((Byte)element4, (Byte)bag.get(4));
        assertEquals((Byte)element5, (Byte)bag.get(5));
        assertEquals((Byte)element6, (Byte)bag.get(6));
        assertEquals((Byte)element7, (Byte)bag.get(7));

        bag.clear();

        assertEquals((Byte)(byte) 0, (Byte)bag.get(0));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(1));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(2));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(3));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(4));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(5));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(6));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(7));
    }

    /**
     * Ensures that the bag contains the default value by default
     */
    @Test
    public void testDefault() {
        ByteBag bag = new ByteBag();
        assertEquals((Byte)(byte) 0, (Byte)bag.get(0));
        bag.set(0, (byte) 1);
        assertEquals((Byte)(byte) 0, (Byte)bag.get(1));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(2));
        assertEquals((Byte)(byte) 0, (Byte)bag.get(3));
    }

    /**
     * Ensures that the bag resizes correctly when out of capacity and that it
     * does not resize when queried for non-existing elements.
     */
    @Test
    public void testCapacity() {
        ByteBag bag;

        bag = new ByteBag();
        assertEquals(0, bag.buffer.length);
        bag.set(0, (byte) 1);
        assertEquals(1, bag.buffer.length);
        bag.set(1, (byte) 1);
        assertEquals(2, bag.buffer.length);
        bag.set(2, (byte) 1);
        assertEquals(4, bag.buffer.length);
        bag.set(3, (byte) 1);
        assertEquals(4, bag.buffer.length);
        bag.set(4, (byte) 1);
        assertEquals(8, bag.buffer.length);
        bag.set(8, (byte) 1);
        assertEquals(16, bag.buffer.length);
        bag.set(35, (byte) 1);
        assertEquals(64, bag.buffer.length);

        bag = new ByteBag();
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
        ByteBag bag = new ByteBag();
        for (int i = 0; i < 32; i++) {
            try {
                bag.set(-(1 << i), (byte) 1);
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
        ByteBag bag = new ByteBag();
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
        ByteBag bag;

        bag = new ByteBag();
        bag.set(0, (byte) 1);
        bag.set(1, (byte) 1);
        bag.set(63, (byte) 1);
        bag.clear();
        assertEquals((Byte) (byte) 0, (Byte) bag.get(0));
        assertEquals((Byte) (byte) 0, (Byte) bag.get(1));
        assertEquals((Byte) (byte) 0, (Byte) bag.get(63));
        assertEquals(64, bag.buffer.length);

        Mask mask = new Mask();
        bag = new ByteBag();
        bag.set(0, (byte) 1);
        bag.set(1, (byte) 1);
        bag.set(63, (byte) 1);
        mask.set(63);
        mask.set(0);
        bag.clear(mask);
        assertEquals((Byte) (byte) 0, (Byte) bag.get(0));
        assertNotEquals((Byte) (byte) 0, (Byte) bag.get(1));
        assertEquals((Byte) (byte) 0, (Byte) bag.get(63));
        mask.set(1);
        mask.set(457);
        bag.clear(mask);
        assertEquals((Byte) (byte) 0, (Byte) bag.get(0));
        assertEquals((Byte) (byte) 0, (Byte) bag.get(1));
        assertEquals((Byte) (byte) 0, (Byte) bag.get(63));
        assertEquals(64, bag.buffer.length);
    }

    @Test
    public void testCopyFrom() {
        ByteBag bag0, bag1;

        bag0 = new ByteBag();
        bag0.set(0, (byte) 1);
        bag0.set(5, (byte) 1);

        bag1 = new ByteBag();
        bag1.set(9, (byte) 1);

        bag1.copyFrom(bag0, false);
        assertNotEquals((Byte) (byte) 0, (Byte) bag1.get(0));
        assertNotEquals((Byte) (byte) 0, (Byte) bag1.get(5));
        assertNotEquals((Byte) (byte) 0, (Byte) bag1.get(9));

        bag1.copyFrom(bag0);
        assertNotEquals((Byte) (byte) 0, (Byte) bag1.get(0));
        assertNotEquals((Byte) (byte) 0, (Byte) bag1.get(5));
        assertEquals((Byte) (byte) 0, (Byte) bag1.get(9));

        bag0.copyFrom(bag1);
    }
}
