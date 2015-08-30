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
package com.github.antag99.retinazer.beam.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class OrdinalTest {
    @Test
    public void testIndex() {
        assertEquals(0, TestOrdinal.TEST_0.getIndex());
        assertEquals(1, TestOrdinal.TEST_1.getIndex());
        assertEquals(2, TestOrdinal.TEST_2.getIndex());
        assertEquals(3, TestOrdinal.TEST_3.getIndex());
    }

    @Test
    public void testName() {
        assertEquals("TestOrdinal.TEST_0", TestOrdinal.TEST_0.getName());
        assertEquals("TestOrdinal.TEST_1", TestOrdinal.TEST_1.getName());
        assertEquals("TestOrdinal.TEST_2", TestOrdinal.TEST_2.getName());
        assertEquals("TestOrdinal.TEST_3", TestOrdinal.TEST_3.getName());
    }

    @Test
    public void testForIndex() {
        assertEquals(TestOrdinal.TEST_0, TestOrdinal.TYPE.forIndex(0));
        assertEquals(TestOrdinal.TEST_1, TestOrdinal.TYPE.forIndex(1));
        assertEquals(TestOrdinal.TEST_2, TestOrdinal.TYPE.forIndex(2));
        assertEquals(TestOrdinal.TEST_3, TestOrdinal.TYPE.forIndex(3));
    }

    @Test
    public void testForName() {
        assertEquals(TestOrdinal.TEST_0, TestOrdinal.TYPE.forName("TestOrdinal.TEST_0"));
        assertEquals(TestOrdinal.TEST_1, TestOrdinal.TYPE.forName("TestOrdinal.TEST_1"));
        assertEquals(TestOrdinal.TEST_2, TestOrdinal.TYPE.forName("TestOrdinal.TEST_2"));
        assertEquals(TestOrdinal.TEST_3, TestOrdinal.TYPE.forName("TestOrdinal.TEST_3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNameAlreadyInUse() {
        new TestOrdinal(TestOrdinal.TEST_0.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Ordinal<T>> void testNameIsNull() {
        new Ordinal<T>(new OrdinalType<T>(), null) {
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Ordinal<T>> void testTypeIsNull() {
        new Ordinal<T>(null, "NAME") {
        };
    }
}
