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

/**
 * <p>
 * {@link Ordinal} is used as a way to implement extensible enumerations - it
 * associates each instance of a class with an index. Indices are assigned in
 * the order the constants are created, so it may vary depending on the
 * environment. Names are user-defined, and provide the identity of an ordinal.
 * </p>
 *
 * Implementing an ordinal is typically done as following:
 *
 * <pre>
 * <code>
 * public final class Fruit extends Ordinal&lt;Fruit&gt; {
 *     public static final OrdinalType&lt;Fruit&gt; TYPE = new OrdinalType&lt;&gt;();
 *
 *     public static final Fruit APPLE = new Fruit("Fruit.APPLE");
 *     public static final Fruit PEAR = new Fruit("Fruit.PEAR");
 *     public static final Fruit BANANA = new Fruit("Fruit.BANANA");
 *
 *     public Fruit(String name) {
 *         super(TYPE, name);
 *     }
 *
 *     /* delegate methods can be provided for convenience *&#47;
 *     public static Fruit forIndex(int index) {
 *         return TYPE.forIndex(index);
 *     }
 *
 *     public static Fruit forName(String name) {
 *         return TYPE.forName(name);
 *     }
 * }
 * </code>
 * </pre>
 *
 * @param <T> Type of this ordinal.
 */
public abstract class Ordinal<T extends Ordinal<T>> {
    private final int index;
    private final String name;

    @SuppressWarnings("unchecked")
    public Ordinal(OrdinalType<T> type, String name) {
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }

        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }

        if (type.ordinalsByName.containsKey(name)) {
            throw new IllegalArgumentException("an ordinal named " + name +
                    " already exists");
        }

        this.name = name;
        this.index = type.nextIndex++;

        type.ordinalsByIndex.set(this.index, (T) this);
        type.ordinalsByName.put(this.name, (T) this);
    }

    /**
     * Gets the index of this ordinal.
     *
     * @return the index of this ordinal.
     */
    public final int getIndex() {
        return index;
    }

    /**
     * Gets the name of this ordinal.
     *
     * @return the name of this ordinal.
     */
    public final String getName() {
        return name;
    }
}
