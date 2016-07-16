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
package com.github.antag99.retinazer.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.github.antag99.retinazer.util.Mask;

public class MaskTest {

    @Test
    public void testLength() {
        Mask mask;

        mask = new Mask();
        for (int i = 0, n = 128; i < n; i++) {
            assertEquals(i, mask.length());
            mask.set(i);
            assertEquals(i + 1, mask.length());
        }

        mask = new Mask();
        mask.set(4);
        assertEquals(5, mask.length());
        mask.set(63);
        assertEquals(64, mask.length());
        mask.set(64);
        assertEquals(65, mask.length());
    }

    @Test
    public void testIndices() {
        Mask mask;

        mask = new Mask();
        mask.set(1);
        mask.set(4);
        mask.set(6);
        mask.set(7);
        assertTrue(Arrays.equals(new int[] { 1, 4, 6, 7 }, mask.getIndices()));
    }
}
