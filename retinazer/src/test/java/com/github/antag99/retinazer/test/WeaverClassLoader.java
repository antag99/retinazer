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
public class WeaverClassLoader extends ClassLoader {
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
