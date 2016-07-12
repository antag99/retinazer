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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.github.antag99.retinazer.util.Bag;
import com.github.antag99.retinazer.util.Experimental;
import com.github.antag99.retinazer.util.Mask;

@Experimental
public final class PlainMapper<T extends Component> extends Mapper<T> {

    /** No-arg constructor for the component */
    private final Constructor<T> constructor;

    /** Stores components */
    private final Bag<T> components = new Bag<T>();

    PlainMapper(Engine engine, Class<T> type, int typeIndex) {
        super(engine, type, typeIndex);

        try {
            constructor = type.getConstructor();
            constructor.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            throw new RetinazerException("Component type " + type.getName()
                    + " does not expose a zero-argument constructor");
        }
    }

    @Override
    public T create(int entity) {
        checkCreate(entity);
        T instance;
        try {
            instance = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new AssertionError(ex);
        } catch (InvocationTargetException ex) {
            throw Internal.sneakyThrow(ex.getCause());
        }
        components.set(entity, instance);
        return instance;
    }

    @Override
    public T get(int entity) {
        return components.get(entity);
    }

    @Override
    protected void applyComponentChanges() {
        Bag<T> components = this.components;
        Mask componentsMask = this.componentsMask;
        Mask removeMask = this.removeMask;
        components.clear(removeMask);
        componentsMask.andNot(removeMask);
    }
}
