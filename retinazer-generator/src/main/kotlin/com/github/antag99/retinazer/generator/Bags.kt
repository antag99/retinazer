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
package com.github.antag99.retinazer.generator


enum class Bags(
        val rawBagName: String,
        val rawTypeName: String,
        val defaultValue: String,
        val testValue: String,
        val wrapperTypeName: String,
        val genericBagName: String = rawBagName,
        val genericTypeName: String = rawTypeName
) {
    BYTE(
            rawBagName = "ByteBag",
            rawTypeName = "byte",
            wrapperTypeName = "Byte",
            defaultValue = "(byte) 0",
            testValue = "(byte) 1"
    ),
    SHORT(
            rawBagName = "ShortBag",
            rawTypeName = "short",
            wrapperTypeName = "Short",
            defaultValue = "(short) 0",
            testValue = "(short) 1"
    ),
    CHAR(
            rawBagName = "CharBag",
            rawTypeName = "char",
            wrapperTypeName = "Character",
            defaultValue = "(char) 0",
            testValue = "(char) 1"
    ),
    INT(
            rawBagName = "IntBag",
            rawTypeName = "int",
            wrapperTypeName = "Integer",
            defaultValue = "0",
            testValue = "1"
    ),
    LONG(
            rawBagName = "LongBag",
            rawTypeName = "long",
            wrapperTypeName = "Long",
            defaultValue = "0L",
            testValue = "1L"
    ),
    FLOAT(
            rawBagName = "FloatBag",
            rawTypeName = "float",
            wrapperTypeName = "Float",
            defaultValue = "0f",
            testValue = "1f"
    ),
    DOUBLE(
            rawBagName = "DoubleBag",
            rawTypeName = "double",
            wrapperTypeName = "Double",
            defaultValue = "0d",
            testValue = "1d"
    ),
    BOOLEAN(
            rawBagName = "BooleanBag",
            rawTypeName = "boolean",
            wrapperTypeName = "Boolean",
            defaultValue = "false",
            testValue = "true"
    ),
    OBJECT(
            rawBagName = "Bag",
            rawTypeName = "Object",
            wrapperTypeName = "T",
            defaultValue = "null",
            testValue = "(T) new Object()",
            genericBagName = "Bag<T>",
            genericTypeName = "T"
    );

    val generatedCode: String =
"""${LICENSE_HEADER.trim()}
package com.github.antag99.retinazer.util;

/**
 * A bag is an automatically expanding array.
 */
$GENERATED_NOTICE
@SuppressWarnings("all")
public final class $genericBagName implements AnyBag<$genericBagName> {
${
    if(rawBagName == "Bag")
"""
    static int nextPowerOfTwo(int value) {
        if (value == 0) {
            return 1;
        }
        value--;
        value |= value >>> 1;
        value |= value >>> 2;
        value |= value >>> 4;
        value |= value >>> 8;
        value |= value >>> 16;
        return value + 1;
    }
"""
    else ""
}
    /**
     * Backing buffer of this bag.
     */
    @Experimental
    public $rawTypeName[] buffer;

    /**
     * Creates a new {@code $rawBagName} with an initial capacity of {@code 0}.
     */
    public $rawBagName() {
        buffer = new $rawTypeName[0];
    }

    @Override
    public void copyFrom($genericBagName from) {
        copyFrom(from, true);
    }

    @Override
    public void copyFrom($genericBagName from, boolean clearExceeding) {
        copyFrom(from, from.buffer.length, clearExceeding);
    }

    @Override
    public void copyFrom($genericBagName from, int length) {
        copyFrom(from, length, true);
    }

    @Override
    public void copyFrom($genericBagName from, int length, boolean clearExceeding) {
        copyFrom(from, 0, length, clearExceeding);
    }

    @Override
    public void copyFrom($genericBagName from, int fromOffset, int length) {
        copyFrom(from, fromOffset, length, true);
    }

    @Override
    public void copyFrom($genericBagName from, int fromOffset, int length, boolean clearExceeding) {
        if (buffer.length < length)
            buffer = new $rawTypeName[length];
        // Maximum number of elements that can be copied from the given buffer
        int copyLength = Math.min(length, from.buffer.length - fromOffset);
        System.arraycopy(from.buffer, fromOffset, buffer, 0, copyLength);
        if (clearExceeding && buffer.length > copyLength) {
            $rawTypeName[] buffer = this.buffer;
            for (int i = copyLength, n = buffer.length; i < n; i++)
                buffer[i] = $defaultValue;
        }
    }

    @Override
    public void copyPartFrom($genericBagName from, int fromOffset, int toOffset, int length) {
        ensureCapacity(toOffset + length);
        // Maximum number of elements that can be copied from the given buffer
        int maxLength = from.buffer.length - fromOffset;
        System.arraycopy(from.buffer, fromOffset, buffer, toOffset, Math.min(length, maxLength));
        if (maxLength < length) {
            $rawTypeName[] buffer = this.buffer;
            for (int i = toOffset + maxLength, n = toOffset + length; i < n; i++)
                buffer[i] = $defaultValue;
        }
    }

    @Override
    public void ensureCapacity(int capacity) {
        if (capacity < 0)
            throw new NegativeArraySizeException(String.valueOf(capacity));
        if (this.buffer.length >= capacity)
            return;
        int newCapacity = Bag.nextPowerOfTwo(capacity);
        $rawTypeName[] newBuffer = new $rawTypeName[newCapacity];
        System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
        this.buffer = newBuffer;
    }

    /**
     * Gets the element at the given index. Returns {@code $defaultValue} if it does not exist.
     *
     * @param index
     *            Index of the element. The size of the buffer will not be increased if the index is greater.
     */
    public $genericTypeName get(int index) {
        if (index < 0)
            throw new IndexOutOfBoundsException("index < 0: " + index);

        return index >= buffer.length ? $defaultValue : ($genericTypeName) buffer[index];
    }

    /**
     * Sets the element at the given index.
     *
     * @param index
     *            Index of the element. The size of the buffer will be increased if necessary.
     * @param value
     *            Value to set.
     */
    public void set(int index, $genericTypeName value) {
        if (index < 0)
            throw new IndexOutOfBoundsException("index < 0: " + index);

        ensureCapacity(index + 1);

        buffer[index] = ($genericTypeName) value;
    }

    @Override
    public void clear() {
        $rawTypeName[] buffer = this.buffer;
        for (int i = 0, n = buffer.length; i < n; ++i)
            buffer[i] = $defaultValue;
    }

    @Override
    public void clear(Mask mask) {
        $rawTypeName[] buffer = this.buffer;
        for (int i = mask.nextSetBit(0), n = buffer.length; i != -1 && i < n; i = mask.nextSetBit(i + 1))
            buffer[i] = $defaultValue;
    }
}
"""

    val generatedTestCode: String =
"""${LICENSE_HEADER.trim()}
package com.github.antag99.retinazer.util;

import org.junit.Test;
import static org.junit.Assert.*;

$GENERATED_NOTICE
@SuppressWarnings("all")
public final class ${genericBagName.replace("Bag", "BagTest")} {
    /**
     * Ensures that the elements of a bag are actually stored
     */
    @Test
    public void testStorage() {
        $genericBagName bag = new $genericBagName();
        $genericTypeName element0 = $testValue;
        $genericTypeName element1 = $testValue;
        $genericTypeName element2 = $testValue;
        $genericTypeName element3 = $testValue;
        $genericTypeName element4 = $testValue;
        $genericTypeName element5 = $testValue;
        $genericTypeName element6 = $testValue;
        $genericTypeName element7 = $testValue;

        bag.set(0, element0);
        assertEquals(($wrapperTypeName)element0, ($wrapperTypeName)bag.get(0));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(1));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(2));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(3));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(4));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(5));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(6));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(7));

        bag.set(1, element1);
        assertEquals(($wrapperTypeName)element0, ($wrapperTypeName)bag.get(0));
        assertEquals(($wrapperTypeName)element1, ($wrapperTypeName)bag.get(1));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(2));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(3));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(4));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(5));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(6));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(7));

        bag.set(2, element2);
        assertEquals(($wrapperTypeName)element0, ($wrapperTypeName)bag.get(0));
        assertEquals(($wrapperTypeName)element1, ($wrapperTypeName)bag.get(1));
        assertEquals(($wrapperTypeName)element2, ($wrapperTypeName)bag.get(2));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(3));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(4));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(5));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(6));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(7));

        bag.set(3, element3);
        assertEquals(($wrapperTypeName)element0, ($wrapperTypeName)bag.get(0));
        assertEquals(($wrapperTypeName)element1, ($wrapperTypeName)bag.get(1));
        assertEquals(($wrapperTypeName)element2, ($wrapperTypeName)bag.get(2));
        assertEquals(($wrapperTypeName)element3, ($wrapperTypeName)bag.get(3));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(4));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(5));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(6));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(7));

        bag.set(4, element4);
        assertEquals(($wrapperTypeName)element0, ($wrapperTypeName)bag.get(0));
        assertEquals(($wrapperTypeName)element1, ($wrapperTypeName)bag.get(1));
        assertEquals(($wrapperTypeName)element2, ($wrapperTypeName)bag.get(2));
        assertEquals(($wrapperTypeName)element3, ($wrapperTypeName)bag.get(3));
        assertEquals(($wrapperTypeName)element4, ($wrapperTypeName)bag.get(4));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(5));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(6));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(7));

        bag.set(5, element5);
        assertEquals(($wrapperTypeName)element0, ($wrapperTypeName)bag.get(0));
        assertEquals(($wrapperTypeName)element1, ($wrapperTypeName)bag.get(1));
        assertEquals(($wrapperTypeName)element2, ($wrapperTypeName)bag.get(2));
        assertEquals(($wrapperTypeName)element3, ($wrapperTypeName)bag.get(3));
        assertEquals(($wrapperTypeName)element4, ($wrapperTypeName)bag.get(4));
        assertEquals(($wrapperTypeName)element5, ($wrapperTypeName)bag.get(5));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(6));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(7));

        bag.set(6, element6);
        assertEquals(($wrapperTypeName)element0, ($wrapperTypeName)bag.get(0));
        assertEquals(($wrapperTypeName)element1, ($wrapperTypeName)bag.get(1));
        assertEquals(($wrapperTypeName)element2, ($wrapperTypeName)bag.get(2));
        assertEquals(($wrapperTypeName)element3, ($wrapperTypeName)bag.get(3));
        assertEquals(($wrapperTypeName)element4, ($wrapperTypeName)bag.get(4));
        assertEquals(($wrapperTypeName)element5, ($wrapperTypeName)bag.get(5));
        assertEquals(($wrapperTypeName)element6, ($wrapperTypeName)bag.get(6));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(7));

        bag.set(7, element7);
        assertEquals(($wrapperTypeName)element0, ($wrapperTypeName)bag.get(0));
        assertEquals(($wrapperTypeName)element1, ($wrapperTypeName)bag.get(1));
        assertEquals(($wrapperTypeName)element2, ($wrapperTypeName)bag.get(2));
        assertEquals(($wrapperTypeName)element3, ($wrapperTypeName)bag.get(3));
        assertEquals(($wrapperTypeName)element4, ($wrapperTypeName)bag.get(4));
        assertEquals(($wrapperTypeName)element5, ($wrapperTypeName)bag.get(5));
        assertEquals(($wrapperTypeName)element6, ($wrapperTypeName)bag.get(6));
        assertEquals(($wrapperTypeName)element7, ($wrapperTypeName)bag.get(7));

        bag.clear();

        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(0));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(1));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(2));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(3));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(4));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(5));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(6));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(7));
    }

    /**
     * Ensures that the bag contains the default value by default
     */
    @Test
    public void testDefault() {
        $genericBagName bag = new $genericBagName();
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(0));
        bag.set(0, $testValue);
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(1));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(2));
        assertEquals(($wrapperTypeName)$defaultValue, ($wrapperTypeName)bag.get(3));
    }

    /**
     * Ensures that the bag resizes correctly when out of capacity and that it
     * does not resize when queried for non-existing elements.
     */
    @Test
    public void testCapacity() {
        $genericBagName bag;

        bag = new $genericBagName();
        assertEquals(0, bag.buffer.length);
        bag.set(0, $testValue);
        assertEquals(1, bag.buffer.length);
        bag.set(1, $testValue);
        assertEquals(2, bag.buffer.length);
        bag.set(2, $testValue);
        assertEquals(4, bag.buffer.length);
        bag.set(3, $testValue);
        assertEquals(4, bag.buffer.length);
        bag.set(4, $testValue);
        assertEquals(8, bag.buffer.length);
        bag.set(8, $testValue);
        assertEquals(16, bag.buffer.length);
        bag.set(35, $testValue);
        assertEquals(64, bag.buffer.length);

        bag = new $genericBagName();
        for (int i = 0; i < 32; i++) {
            bag.get((1 << i) - 1);
            assertEquals(0, bag.buffer.length);
        }
        bag.get(Integer.MAX_VALUE);
        assertEquals(0, bag.buffer.length);
    }
${
    if (rawBagName == "Bag")
"""
    @Test
    public void testNextPowerOfTwo() {
        assertEquals(1, Bag.nextPowerOfTwo(0));
        assertEquals(1, Bag.nextPowerOfTwo(1));
        assertEquals(2, Bag.nextPowerOfTwo(2));
        assertEquals(4, Bag.nextPowerOfTwo(3));
        assertEquals(1 << 31, Bag.nextPowerOfTwo((1 << 30) + 1));
    }
"""
    else ""
}
    /**
     * When a negative index is used, an {@link IndexOutOfBoundsException} should be thrown.
     */
    @Test
    public void testIndexOutOfBoundsException() {
        $genericBagName bag = new $genericBagName();
        for (int i = 0; i < 32; i++) {
            try {
                bag.set(-(1 << i), $testValue);
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
        $genericBagName bag = new $genericBagName();
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
        $genericBagName bag;

        bag = new $genericBagName();
        bag.set(0, $testValue);
        bag.set(1, $testValue);
        bag.set(63, $testValue);
        bag.clear();
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag.get(0));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag.get(1));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag.get(63));
        assertEquals(64, bag.buffer.length);

        Mask mask = new Mask();
        bag = new $genericBagName();
        bag.set(0, $testValue);
        bag.set(1, $testValue);
        bag.set(63, $testValue);
        mask.set(63);
        mask.set(0);
        bag.clear(mask);
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag.get(0));
        assertNotEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag.get(1));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag.get(63));
        mask.set(1);
        mask.set(457);
        bag.clear(mask);
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag.get(0));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag.get(1));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag.get(63));
        assertEquals(64, bag.buffer.length);
    }

    @Test
    public void testCopyFrom() {
        // Test copyFrom(bag) and copyFrom(bag, clearExceeding)
        $genericBagName bag0, bag1;
        $genericTypeName e0 = $testValue, e1 = $testValue, e2 = $testValue, e3 = $testValue;

        bag0 = new $genericBagName();
        bag0.set(0, e0);
        bag0.set(3, e1);
        bag0.set(9, e2);
        bag1 = new $genericBagName();
        bag1.copyFrom(bag0);
        assertEquals(($wrapperTypeName) e0, ($wrapperTypeName) bag1.get(0));
        assertEquals(($wrapperTypeName) e1, ($wrapperTypeName) bag1.get(3));
        assertEquals(($wrapperTypeName) e2, ($wrapperTypeName) bag1.get(9));
        bag1 = new $genericBagName();
        bag1.set(53, e3);
        bag1.copyFrom(bag0, false);
        assertEquals(($wrapperTypeName) e0, ($wrapperTypeName) bag1.get(0));
        assertEquals(($wrapperTypeName) e1, ($wrapperTypeName) bag1.get(3));
        assertEquals(($wrapperTypeName) e2, ($wrapperTypeName) bag1.get(9));
        assertEquals(($wrapperTypeName) e3, ($wrapperTypeName) bag1.get(53));
        bag1 = new $genericBagName();
        bag1.set(53, e3);
        bag1.copyFrom(bag0, true);
        assertEquals(($wrapperTypeName) e0, ($wrapperTypeName) bag1.get(0));
        assertEquals(($wrapperTypeName) e1, ($wrapperTypeName) bag1.get(3));
        assertEquals(($wrapperTypeName) e2, ($wrapperTypeName) bag1.get(9));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(53));

        // Test copyFrom(bag, length) and copyFrom(bag, length, clearExceeding)
        bag0 = new $genericBagName();
        bag0.set(4, e0);
        bag0.set(7, e1);
        bag1 = new $genericBagName();
        bag1.copyFrom(bag0, 3);
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(0));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(1));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(2));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(3));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(4));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(5));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(6));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(7));
        bag1 = new $genericBagName();
        bag1.copyFrom(bag0, 5);
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(0));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(1));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(2));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(3));
        assertEquals(($wrapperTypeName) e0, ($wrapperTypeName) bag1.get(4));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(5));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(6));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(7));
        bag1 = new $genericBagName();
        bag1.set(8, e2);
        bag1.copyFrom(bag0, 8, true);
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(0));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(1));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(2));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(3));
        assertEquals(($wrapperTypeName) e0, ($wrapperTypeName) bag1.get(4));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(5));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(6));
        assertEquals(($wrapperTypeName) e1, ($wrapperTypeName) bag1.get(7));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(8));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(9));

        // Test copyFrom(bag, fromOffset, length) and copyFrom(bag, fromOffset, length, clearExceeding)
        bag0 = new $genericBagName();
        bag0.set(0, e0);
        bag0.set(4, e1);
        bag0.set(6, e2);
        bag1 = new $genericBagName();
        bag1.copyFrom(bag0, 3, 2);
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(0));
        assertEquals(($wrapperTypeName) e1, ($wrapperTypeName) bag1.get(1));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(2));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(3));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(4));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(5));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(6));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(7));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(8));
        bag0 = new $genericBagName();
        bag0.set(0, e0);
        bag0.set(4, e1);
        bag0.set(10, e2);
        bag1 = new $genericBagName();
        bag1.set(7, e3);
        bag1.copyFrom(bag0, 3, 2, false);
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(0));
        assertEquals(($wrapperTypeName) e1, ($wrapperTypeName) bag1.get(1));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(2));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(3));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(4));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(5));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(6));
        assertEquals(($wrapperTypeName) e3, ($wrapperTypeName) bag1.get(7));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(8));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(9));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) bag1.get(10));
    }

    @Test
    public void testCopyPartFrom() {
        $genericBagName b0, b1;
        $genericTypeName e0 = $testValue, e1 = $testValue, e2 = $testValue, e3 = $testValue;
        b0 = new $genericBagName();
        b0.set(5, e0);
        b0.set(14, e1);
        b0.set(21, e2);
        b1 = new $genericBagName();
        b1.set(0, e3);
        b1.set(33, e0);
        b1.copyPartFrom(b0, 14, 1, 33);
        assertEquals(($wrapperTypeName) e1, ($wrapperTypeName) b1.get(1));
        assertEquals(($wrapperTypeName) e2, ($wrapperTypeName) b1.get(8));
        assertEquals(($wrapperTypeName) e3, ($wrapperTypeName) b1.get(0));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) b1.get(32));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) b1.get(33));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) b1.get(34));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) b1.get(35));
        b0 = new $genericBagName();
        b0.set(5, e0);
        b0.set(14, e1);
        b0.set(21, e2);
        b1 = new $genericBagName();
        b1.set(0, e3);
        b1.copyPartFrom(b0, 14, 1, 8);
        assertEquals(($wrapperTypeName) e1, ($wrapperTypeName) b1.get(1));
        assertEquals(($wrapperTypeName) e2, ($wrapperTypeName) b1.get(8));
        assertEquals(($wrapperTypeName) e3, ($wrapperTypeName) b1.get(0));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) b1.get(32));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) b1.get(33));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) b1.get(34));
        assertEquals(($wrapperTypeName) $defaultValue, ($wrapperTypeName) b1.get(35));
    }
}
"""
}
