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

import com.badlogic.gdx.utils.ObjectMap;
import com.github.antag99.retinazer.util.Bag;

public final class OrdinalType<T extends Ordinal<T>> {
    int nextIndex = 0;
    Bag<T> ordinalsByIndex = new Bag<>();
    ObjectMap<String, T> ordinalsByName = new ObjectMap<>();

    /**
     * Gets the ordinal with the given index.
     *
     * @param index
     *            index of the ordinal.
     * @return The ordinal with the given index.
     */
    public T forIndex(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index out of range: " + index);
        }

        T ordinal = ordinalsByIndex.get(index);
        if (ordinal == null) {
            throw new IllegalArgumentException("No such ordinal with index: " + index);
        }

        return ordinal;
    }

    /**
     * Gets the ordinal with the given name.
     *
     * @param name
     *            name of the ordinal.
     * @return The ordinal with the given name.
     */
    public T forName(String name) {
        if (name == null) {
            throw new NullPointerException("name must not be null");
        }

        T ordinal = ordinalsByName.get(name);
        if (ordinal == null) {
            throw new IllegalArgumentException("No such ordinal with name: " + name);
        }

        return ordinal;
    }
}
