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
package com.github.antag99.retinazer;

import java.lang.reflect.Field;

import com.github.antag99.retinazer.utils.Wire;

/**
 * WireResolver is used for wiring/un-wiring fields marked with {@link Wire}.
 * Multiple resolvers can be registered to an {@link EngineConfig}.
 *
 * @see Wire
 */
public interface WireResolver {

    /**
     * Wires the field of the given object.
     *
     * @param engine The engine instance.
     * @param object The object to wire.
     * @param field The field of the object.
     * @return Whether this resolver handled the given field.
     * @throws Throwable If an unexpected error occurred.
     */
    public boolean wire(Engine engine, Object object, Field field) throws Throwable;

    /**
     * Un-wires the field of the given object.
     *
     * @param engine The engine instance.
     * @param object The object to wire.
     * @param field The field of the object.
     * @return Whether this resolver handled the given field.
     * @throws Throwable If an unexpected error occurred.
     */
    public boolean unwire(Engine engine, Object object, Field field) throws Throwable;
}
