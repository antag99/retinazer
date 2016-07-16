/*******************************************************************************
 * Retinazer, an entity-component-system framework for Java
 *
 * Copyright (C) 2015-2016 Anton Gustafsson
 *
 * This file is part of Retinazer.
 *
 * Retinazer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Retinazer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Retinazer.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.antag99.retinazer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

final class WireCache {
    private final Engine engine;
    private final Field[] fields;
    private final WireResolver[] wireResolvers;

    private static <A extends Annotation> A findAnnotation(Annotation[] annotations, Class<A> annotationType) {
        for (Annotation annotation : annotations)
            if (annotation.annotationType() == annotationType)
                return annotationType.cast(annotation);
        return null;
    }

    public WireCache(Engine engine, Class<?> type, WireResolver[] wireResolvers) {
        List<Field> fields = new ArrayList<>();

        List<Class<?>> hierarchy = new ArrayList<>();
        for (Class<?> current = type; current != Object.class; current = current.getSuperclass())
            hierarchy.add(current);

        boolean inheritWire = false;

        for (int i = hierarchy.size() - 1; i >= 0; i--) {
            Class<?> cls = hierarchy.get(i);
            boolean classWire = inheritWire;
            classWire |= findAnnotation(cls.getDeclaredAnnotations(), Wire.class) != null;
            classWire &= findAnnotation(cls.getDeclaredAnnotations(), SkipWire.class) == null;

            for (Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);

                if (Modifier.isStatic(field.getModifiers()))
                    continue;

                if (Modifier.isFinal(field.getModifiers()))
                    continue;

                if (field.isSynthetic())
                    continue;

                boolean fieldWire = classWire;
                fieldWire |= findAnnotation(field.getDeclaredAnnotations(), Wire.class) != null;
                fieldWire &= findAnnotation(field.getDeclaredAnnotations(), SkipWire.class) == null;

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
