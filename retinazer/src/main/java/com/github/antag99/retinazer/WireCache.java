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

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;

final class WireCache {
    private final Engine engine;
    private final Field[] fields;
    private final WireResolver[] wireResolvers;

    public WireCache(Engine engine, Class<?> type, WireResolver[] wireResolvers) {
        List<Field> fields = new ArrayList<>();

        List<Class<?>> hierarchy = new ArrayList<>();
        for (Class<?> current = type; current != Object.class; current = current.getSuperclass())
            hierarchy.add(current);

        boolean inheritWire = false;

        for (int i = hierarchy.size() - 1; i >= 0; i--) {
            Class<?> cls = hierarchy.get(i);
            boolean classWire = inheritWire;
            classWire |= ClassReflection.getDeclaredAnnotation(cls, Wire.class) != null;
            classWire &= ClassReflection.getDeclaredAnnotation(cls, SkipWire.class) == null;

            for (Field field : ClassReflection.getDeclaredFields(cls)) {
                field.setAccessible(true);

                if (field.isStatic())
                    continue;

                if (field.isSynthetic())
                    continue;

                boolean fieldWire = classWire;
                fieldWire |= field.getDeclaredAnnotation(Wire.class) != null;
                fieldWire &= field.getDeclaredAnnotation(SkipWire.class) == null;

                if (fieldWire) {
                    fields.add(field);
                }
            }

            inheritWire = classWire;
        }

        this.engine = engine;
        this.fields = fields.toArray(new Field[0]);
        this.wireResolvers = wireResolvers;
    }

    public void wire(Object object) {
        final Field[] fields = this.fields;
        final WireResolver[] wireResolvers = this.wireResolvers;

        iterate: for (int i = 0; i < fields.length; i++) {
            final Field field = fields[i];
            for (int ii = 0; ii < wireResolvers.length; ii++) {
                try {
                    if (wireResolvers[ii].wire(engine, object, field)) {
                        continue iterate;
                    }
                } catch (Throwable ex) {
                    throw Internal.sneakyThrow(ex);
                }
            }

            throw new RetinazerException("Failed to wire field " +
                    field.getName() + " of " +
                    field.getDeclaringClass().getName() + "; no resolver");
        }
    }

    public void unwire(Object object) {
        final Field[] fields = this.fields;
        final WireResolver[] wireResolvers = this.wireResolvers;

        iterate: for (int i = 0; i < fields.length; i++) {
            final Field field = fields[i];
            for (int ii = 0; ii < wireResolvers.length; ii++) {
                try {
                    if (wireResolvers[ii].unwire(engine, object, field)) {
                        continue iterate;
                    }
                } catch (Throwable ex) {
                    throw Internal.sneakyThrow(ex);
                }
            }

            throw new RetinazerException("Failed to unwire field " +
                    field.getName() + " of " +
                    field.getDeclaringClass().getName() + "; no resolver");
        }
    }
}
