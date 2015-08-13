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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TestUtils {
    public static <T> Set<T> toSet(Iterable<T> items) {
        return toSet(items.iterator());
    }

    public static <T> Set<T> toSet(Iterator<T> items) {
        Set<T> set = new HashSet<T>();
        while (items.hasNext())
            set.add(items.next());
        return set;
    }

    public static <T> Map<T, Integer> toMultiset(Iterable<T> items) {
        return toMultiset(items.iterator());
    }

    public static <T> Map<T, Integer> toMultiset(Iterator<T> items) {
        Map<T, Integer> map = new HashMap<>();
        while (items.hasNext()) {
            T item = items.next();
            if (!map.containsKey(item)) {
                map.put(item, 1);
            } else {
                map.put(item, map.get(item) + 1);
            }
        }
        return map;
    }

    public static String toString(Iterable<?> iterable) {
        return toString(iterable.iterator());
    }

    public static String toString(Iterator<?> iterator) {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (iterator.hasNext())
                builder.append(",");
        }
        builder.append(']');
        return builder.toString();
    }

    public static void assertEquals(Iterable<?> expected, Iterable<?> actual) {
        if (!equals(expected, actual))
            throw new AssertionError("[expected:" + toString(expected) + ", was:" + toString(actual) + "]");
    }

    public static void assertEqualsUnordered(Iterable<?> expected, Iterable<?> actual) {
        if (!equalsUnordered(expected, actual))
            throw new AssertionError("[expected:" + toString(expected) + ", was:" + toString(actual) + "]");
    }

    public static boolean equals(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    public static boolean equals(Iterable<?> first, Iterable<?> second) {
        return equals(first.iterator(), second.iterator());
    }

    public static boolean equals(Iterator<?> first, Iterator<?> second) {
        while (first.hasNext() && second.hasNext()) {
            if (!equals(first.next(), second.next())) {
                return false;
            }
        }

        return !first.hasNext() && !second.hasNext();
    }

    public static boolean equalsUnordered(Iterable<?> first, Iterable<?> second) {
        return equalsUnordered(first.iterator(), second.iterator());
    }

    public static boolean equalsUnordered(Iterator<?> first, Iterator<?> second) {
        return toMultiset(first).equals(toMultiset(second));
    }
}
