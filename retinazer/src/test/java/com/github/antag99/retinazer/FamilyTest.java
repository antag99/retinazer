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

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.antag99.retinazer.component.TagA;

public class FamilyTest {
    @Test
    public void testHashCode() {
        Engine engine = new Engine(new EngineConfig());
        assertEquals(0, engine.getFamily(Family.create()).hashCode());
        assertEquals(1, engine.getFamily(Family.with(TagA.class)).hashCode());
        assertEquals(2, engine.getFamily(Family.exclude(TagA.class)).hashCode());
    }

    @Test
    public void testEquals() {
        Engine engine = new Engine(new EngineConfig());
        assertEquals(engine.getFamily(Family.create()), engine.getFamily(Family.create()));
        assertEquals(engine.getFamily(Family.with(TagA.class)),
                engine.getFamily(Family.with(TagA.class)));
        assertEquals(engine.getFamily(Family.exclude(TagA.class)),
                engine.getFamily(Family.exclude(TagA.class)));
        assertNotEquals(engine.getFamily(Family.with(TagA.class)),
                engine.getFamily(Family.exclude(TagA.class)));
    }

    @Test
    public void testSame() {
        Engine engine = new Engine(new EngineConfig());
        assertSame(engine.getFamily(Family.create()), engine.getFamily(Family.create()));
        assertSame(engine.getFamily(Family.with(TagA.class)),
                engine.getFamily(Family.with(TagA.class)));
        assertSame(engine.getFamily(Family.exclude(TagA.class)),
                engine.getFamily(Family.exclude(TagA.class)));
        assertNotSame(engine.getFamily(Family.with(TagA.class)),
                engine.getFamily(Family.exclude(TagA.class)));
    }
}
