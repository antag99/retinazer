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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class BagTest {

    /**
     * Ensures that the elements of a bag are actually stored
     */
    @Test
    public void testStorage() {
        Bag<Object> bag = new Bag<>();
        Object element0 = mock(Object.class);
        Object element1 = mock(Object.class);
        Object element2 = mock(Object.class);
        Object element3 = mock(Object.class);
        Object element4 = mock(Object.class);
        Object element5 = mock(Object.class);
        Object element6 = mock(Object.class);
        Object element7 = mock(Object.class);

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
    @Test
    public void testDefault() {
        Bag<Object> bag = new Bag<>();
        assertEquals(null, bag.get(0));
        bag.set(0, mock(Object.class));
        assertEquals(null, bag.get(1));
        assertEquals(null, bag.get(2));
        assertEquals(null, bag.get(3));
    }

    /**
     * Ensures that the bag resizes correctly when out of capacity, that it
     * does not resize when queried for non-existing elements, and that it does
     * not resize when the default value is set.
     */
    @Test
    public void testCapacity() {
        Bag<Object> bag;

        bag = new Bag<>();
        assertEquals(0, bag.getCapacity());
        bag.set(0, mock(Object.class));
        assertEquals(1, bag.getCapacity());
        bag.set(1, mock(Object.class));
        assertEquals(2, bag.getCapacity());
        bag.set(2, mock(Object.class));
        assertEquals(4, bag.getCapacity());
        bag.set(3, mock(Object.class));
        assertEquals(4, bag.getCapacity());
        bag.set(4, mock(Object.class));
        assertEquals(8, bag.getCapacity());
        bag.set(8, mock(Object.class));
        assertEquals(16, bag.getCapacity());
        bag.set(35, mock(Object.class));
        assertEquals(64, bag.getCapacity());

        bag = new Bag<>();
        for (int i = 0; i < 32; i++) {
            bag.get((1 << i) - 1);
            assertEquals(0, bag.getCapacity());
        }
        bag.get(Integer.MAX_VALUE);
        assertEquals(0, bag.getCapacity());

        bag = new Bag<>();
        for (int i = 0; i < 31; i++) {
            bag.set((1 << i) - 1, null);
            assertEquals(0, bag.getCapacity());
        }
        bag.set(Integer.MAX_VALUE, null);
        assertEquals(0, bag.getCapacity());
    }

    /**
     * When a negative index is used, an {@link ArrayIndexOutOfBoundsException} should be thrown.
     */
    @Test
    public void testArrayIndexOutOfBoundsException() {
        Bag<Object> bag = new Bag<>();
        for (int i = 0; i < 32; i++) {
            try {
                bag.set(-(1 << i), mock(Object.class));
            } catch (ArrayIndexOutOfBoundsException ex) {
                continue;
            }

            fail("ArrayIndexOutOfBoundsException expected for index " + (-(1 << i)));
        }
        for (int i = 0; i < 32; i++) {
            try {
                bag.get(-(1 << i));
            } catch (ArrayIndexOutOfBoundsException ex) {
                continue;
            }

            fail("ArrayIndexOutOfBoundsException expected for index " + (-(1 << i)));
        }
    }
}
