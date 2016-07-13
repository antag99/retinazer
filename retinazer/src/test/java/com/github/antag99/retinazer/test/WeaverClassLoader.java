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
package com.github.antag99.retinazer.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.github.antag99.retinazer.weaver.Weaver;
import com.github.antag99.retinazer.weaver.Weaver.WeaverHandler;
import com.github.antag99.retinazer.weaver.WeaverException;

/**
 * {@code ClassLoader} that processes classes using the {@link Weaver weaver}.
 */
public final class WeaverClassLoader extends ClassLoader {
    private Weaver weaver;
    private String[] packageNames;

    /**
     * Creates a new {@code WeaverClassLoader}.
     *
     * @param parent
     *            The parent class loader.
     * @param packageNames
     *            Packages of classes to be weaved; subpackages are automatically included.
     */
    public WeaverClassLoader(ClassLoader parent, String... packageNames) {
        super(parent);

        this.packageNames = Arrays.copyOf(packageNames, packageNames.length);
        this.weaver = new Weaver(new WeaverHandler() {
            @Override
            public byte[] findClass(String internalName) {
                return getBytes(internalName);
            }

            @Override
            public void saveClass(String internalName, byte[] classFile) {
                String name = internalName.replace('/', '.');
                defineClass(name, classFile, 0, classFile.length);
            }
        });
    }

    /**
     * Checks if the given class should be weaved.
     *
     * @param name
     *            Class name (e.g. {@code java.lang.Object}.
     * @return whether the class should be weaved.
     */
    private boolean canWeave(String name) {
        for (String packageName : packageNames)
            if (name.startsWith(packageName + "."))
                return true;
        return false;
    }

    /**
     * Gets the class file for the class with the given internal name.
     *
     * @param internalName
     *            Internal name (e.g. {@code java/lang/Object}.
     * @return the class file.
     */
    private byte[] getBytes(String internalName) {
        InputStream inputStream = getResourceAsStream(internalName + ".class");
        if (inputStream == null)
            return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[2048];
            int count;
            while ((count = inputStream.read(buffer, 0, buffer.length)) != -1)
                outputStream.write(buffer, 0, count);
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new WeaverException(ex);
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (!canWeave(name))
            return super.loadClass(name, resolve);
        String internalName = name.replace('.', '/');
        weaver.process(internalName);
        return findLoadedClass(name);
    }
}
