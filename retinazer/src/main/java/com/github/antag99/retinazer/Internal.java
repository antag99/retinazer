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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

final class Internal {
    private Internal() {
    }

    private static void getAllFields(Class<?> clazz, List<Field> fields) {
        for (Field field : clazz.getDeclaredFields()) {
            fields.add(field);
        }

        if (clazz != Object.class) {
            getAllFields(clazz.getSuperclass(), fields);
        }
    }

    public static Field[] getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<Field>();
        getAllFields(clazz, fields);
        return fields.toArray(new Field[0]);
    }

    private static void getAllMethods(Class<?> clazz, List<Method> methods) {
        for (Method method : clazz.getDeclaredMethods()) {
            methods.add(method);
        }

        if (clazz != Object.class) {
            getAllMethods(clazz.getSuperclass(), methods);
        }
    }

    public static Method[] getAllMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<Method>();
        getAllMethods(clazz, methods);
        return methods.toArray(new Method[0]);
    }

    @SuppressWarnings("unchecked")
    private static <T extends RuntimeException> void doSneakyThrow(Throwable throwable) {
        throw (T) throwable;
    }

    public static RuntimeException sneakyThrow(Throwable ex) {
        doSneakyThrow(ex);
        return null;
    }
}
