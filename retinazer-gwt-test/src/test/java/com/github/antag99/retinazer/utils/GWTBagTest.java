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
package com.github.antag99.retinazer.utils;

import com.github.antag99.retinazer.RetinazerTestCase;

public class GWTBagTest extends RetinazerTestCase {

    /**
     * Ensures that the elements of a bag are actually stored
     */
    public void testStorage() {
        Bag<Object> bag = new Bag<>();
        Object element0 = new Object();
        Object element1 = new Object();
        Object element2 = new Object();
        Object element3 = new Object();
        Object element4 = new Object();
        Object element5 = new Object();
        Object element6 = new Object();
        Object element7 = new Object();

        bag.set(0, element0);
        assertSame(element0, bag.get(0));
        assertSame(null, bag.get(1));
        assertSame(null, bag.get(2));
        assertSame(null, bag.get(3));
        assertSame(null, bag.get(4));
        assertSame(null, bag.get(5));
        assertSame(null, bag.get(6));
        assertSame(null, bag.get(7));

        bag.set(1, element1);
        assertSame(element0, bag.get(0));
        assertSame(element1, bag.get(1));
        assertSame(null, bag.get(2));
        assertSame(null, bag.get(3));
        assertSame(null, bag.get(4));
        assertSame(null, bag.get(5));
        assertSame(null, bag.get(6));
        assertSame(null, bag.get(7));

        bag.set(2, element2);
        assertSame(element0, bag.get(0));
        assertSame(element1, bag.get(1));
        assertSame(element2, bag.get(2));
        assertSame(null, bag.get(3));
        assertSame(null, bag.get(4));
        assertSame(null, bag.get(5));
        assertSame(null, bag.get(6));
        assertSame(null, bag.get(7));

        bag.set(3, element3);
        assertSame(element0, bag.get(0));
        assertSame(element1, bag.get(1));
        assertSame(element2, bag.get(2));
        assertSame(element3, bag.get(3));
        assertSame(null, bag.get(4));
        assertSame(null, bag.get(5));
        assertSame(null, bag.get(6));
        assertSame(null, bag.get(7));

        bag.set(4, element4);
        assertSame(element0, bag.get(0));
        assertSame(element1, bag.get(1));
        assertSame(element2, bag.get(2));
        assertSame(element3, bag.get(3));
        assertSame(element4, bag.get(4));
        assertSame(null, bag.get(5));
        assertSame(null, bag.get(6));
        assertSame(null, bag.get(7));

        bag.set(5, element5);
        assertSame(element0, bag.get(0));
        assertSame(element1, bag.get(1));
        assertSame(element2, bag.get(2));
        assertSame(element3, bag.get(3));
        assertSame(element4, bag.get(4));
        assertSame(element5, bag.get(5));
        assertSame(null, bag.get(6));
        assertSame(null, bag.get(7));

        bag.set(6, element6);
        assertSame(element0, bag.get(0));
        assertSame(element1, bag.get(1));
        assertSame(element2, bag.get(2));
        assertSame(element3, bag.get(3));
        assertSame(element4, bag.get(4));
        assertSame(element5, bag.get(5));
        assertSame(element6, bag.get(6));
        assertSame(null, bag.get(7));

        bag.set(7, element7);
        assertSame(element0, bag.get(0));
        assertSame(element1, bag.get(1));
        assertSame(element2, bag.get(2));
        assertSame(element3, bag.get(3));
        assertSame(element4, bag.get(4));
        assertSame(element5, bag.get(5));
        assertSame(element6, bag.get(6));
        assertSame(element7, bag.get(7));

        bag.clear();

        assertSame(null, bag.get(0));
        assertSame(null, bag.get(1));
        assertSame(null, bag.get(2));
        assertSame(null, bag.get(3));
        assertSame(null, bag.get(4));
        assertSame(null, bag.get(5));
        assertSame(null, bag.get(6));
        assertSame(null, bag.get(7));
    }

    /**
     * Ensures that the bag contains the default value by default
     */
    public void testDefault() {
        Bag<Object> bag = new Bag<>();
        assertEquals(null, bag.get(0));
        bag.set(0, new Object());
        assertEquals(null, bag.get(1));
        assertEquals(null, bag.get(2));
        assertEquals(null, bag.get(3));
    }

    /**
     * Ensures that the bag resizes correctly when out of capacity, that it
     * does not resize when queried for non-existing elements, and that it does
     * not resize when the default value is set.
     */
    public void testCapacity() {
        Bag<Object> bag;

        bag = new Bag<>();
        assertEquals(0, bag.buffer.length);
        bag.set(0, new Object());
        assertEquals(1, bag.buffer.length);
        bag.set(1, new Object());
        assertEquals(2, bag.buffer.length);
        bag.set(2, new Object());
        assertEquals(4, bag.buffer.length);
        bag.set(3, new Object());
        assertEquals(4, bag.buffer.length);
        bag.set(4, new Object());
        assertEquals(8, bag.buffer.length);
        bag.set(8, new Object());
        assertEquals(16, bag.buffer.length);
        bag.set(35, new Object());
        assertEquals(64, bag.buffer.length);

        bag = new Bag<>();
        for (int i = 0; i < 31; i++) {
            bag.get((1 << i) - 1);
            assertEquals(0, bag.buffer.length);
        }
        bag.get(Integer.MAX_VALUE);
        assertEquals(0, bag.buffer.length);

        bag = new Bag<>();
        for (int i = 0; i < 31; i++) {
            bag.set((1 << i) - 1, null);
            assertEquals(0, bag.buffer.length);
        }
        bag.set(Integer.MAX_VALUE, null);
        assertEquals(0, bag.buffer.length);
    }

//@off: Broken on GWT
//    public void testNextPowerOfTwo() {
//        assertEquals(1, Bag.nextPowerOfTwo(0));
//        assertEquals(1, Bag.nextPowerOfTwo(1));
//        assertEquals(2, Bag.nextPowerOfTwo(2));
//        assertEquals(4, Bag.nextPowerOfTwo(3));
//        assertEquals(1 << 31, Bag.nextPowerOfTwo((1 << 30) + 1));
//    }
//@on

//@off: Broken on GWT
//    /**
//     * When a negative index is used, an {@link IndexOutOfBoundsException} should be thrown.
//     */
//    public void testIndexOutOfBoundsException() {
//        Bag<Object> bag = new Bag<>();
//        for (int i = 0; i < 32; i++) {
//            try {
//                bag.set(-(1 << i), new Object());
//            } catch (IndexOutOfBoundsException ex) {
//                continue;
//            }
//
//            fail("IndexOutOfBoundsException expected for index " + (-(1 << i)));
//        }
//        for (int i = 0; i < 32; i++) {
//            try {
//                bag.get(-(1 << i));
//            } catch (IndexOutOfBoundsException ex) {
//                continue;
//            }
//
//            fail("IndexOutOfBoundsException expected for index " + (-(1 << i)));
//        }
//    }
//@on
}
