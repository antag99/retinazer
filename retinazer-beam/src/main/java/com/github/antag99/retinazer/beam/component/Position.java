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
package com.github.antag99.retinazer.beam.component;

import com.badlogic.gdx.math.Vector2;
import com.github.antag99.retinazer.Component;

/**
 * The position of an entity in a room.
 */
public final class Position implements Component {
    /** The X value of this component */
    public float x;
    /** The Y value of this component */
    public float y;

    /**
     * Creates a new component with the value {@code (0, 0)}.
     */
    public Position() {
        this(0f, 0f);
    }

    /**
     * Creates a new component of this type with the given value.
     * 
     * @param x The X value of the component
     * @param y The Y value of the component
     */
    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the value of this component
     *
     * @param x The X value of this component
     * @param y The Y value of this component
     * @return This component instance
     */
    public Position xy(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Sets the value of this component
     *
     * @param v The value of this component
     * @return This component instance
     */
    public Position xy(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    /**
     * Returns a new {@link Vector2} set to the value of this component.
     *
     * @return A new {@link Vector2} with the value of this component.
     */
    public Vector2 v() {
        return v(new Vector2());
    }

    /**
     * Sets the value of the given {@link Vector2} to the value of this component.
     *
     * @param out The {@link Vector2} to set the value of.
     * @return The vector passed in.
     */
    public Vector2 v(Vector2 out) {
        return out.set(x, y);
    }
}
