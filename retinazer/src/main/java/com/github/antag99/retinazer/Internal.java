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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Internal {
    private Internal() {
    }

    private static void getAllFields(Class<?> clazz, Map<Class<?>, Field[]> fields) {
        fields.put(clazz, clazz.getDeclaredFields());

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && !fields.containsKey(superClass))
            getAllFields(superClass, fields);

        for (Class<?> superInterface : clazz.getInterfaces())
            if (!fields.containsKey(superInterface))
                getAllFields(superInterface, fields);
    }

    public static Field[] getAllFields(Class<?> clazz) {
        Map<Class<?>, Field[]> fields = new HashMap<>();
        getAllFields(clazz, fields);
        List<Field> allFields = new ArrayList<>();
        for (Field[] array : fields.values())
            allFields.addAll(Arrays.asList(array));
        return allFields.toArray(new Field[0]);
    }

    private static void getAllMethods(Class<?> clazz, Map<Class<?>, Method[]> methods) {
        methods.put(clazz, clazz.getDeclaredMethods());

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && !methods.containsKey(superClass))
            getAllMethods(superClass, methods);

        for (Class<?> superInterface : clazz.getInterfaces())
            if (!methods.containsKey(superInterface))
                getAllMethods(superInterface, methods);
    }

    public static Method[] getAllMethods(Class<?> clazz) {
        Map<Class<?>, Method[]> methods = new HashMap<>();
        getAllMethods(clazz, methods);
        List<Method> allMethods = new ArrayList<>();
        for (Method[] array : methods.values())
            allMethods.addAll(Arrays.asList(array));
        return allMethods.toArray(new Method[0]);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void doSneakyThrow(Throwable ex) throws T {
        throw (T) ex;
    }

    public static RuntimeException sneakyThrow(Throwable ex) {
        Internal.<RuntimeException> doSneakyThrow(ex);
        return null;
    }
}
