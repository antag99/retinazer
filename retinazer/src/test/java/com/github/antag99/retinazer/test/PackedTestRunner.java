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

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public final class PackedTestRunner extends BlockJUnit4ClassRunner {
    public PackedTestRunner(Class<?> klass) throws InitializationError {
        super(weaveClass(klass));
    }

    private static Class<?> weaveClass(Class<?> klass) {
        WeaverClassLoader classLoader = new WeaverClassLoader(
                klass.getClassLoader(), "com.github.antag99.retinazer");
        try {
            return Class.forName(klass.getName(), true, classLoader);
        } catch (ClassNotFoundException ex) {
            throw new AssertionError(ex);
        }
    }
}
