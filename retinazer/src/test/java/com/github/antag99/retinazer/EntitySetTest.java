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

import org.junit.Test;

import com.github.antag99.retinazer.component.TagA;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EntitySetTest {
    @Test
    public void testIndices() {
        Engine engine = new Engine(new EngineConfig());
        Mapper<TagA> mapper = engine.getMapper(TagA.class);
        int entity0 = engine.createEntity();
        int entity1 = engine.createEntity();
        mapper.create(entity1);
        int entity2 = engine.createEntity();
        int entity3 = engine.createEntity();
        int entity4 = engine.createEntity();
        engine.update();
        assertEquals(EngineTest.asSet(entity0, entity1, entity2, entity3, entity4),
                EngineTest.asSet(engine.getEntities()));
        assertEquals(EngineTest.asSet(entity1), EngineTest.asSet(
                engine.getFamily(Family.with(TagA.class)).getEntities()));
    }

    @Test
    public void testEquals() {
        EntitySet a = new EntitySet();
        EntitySet b = new EntitySet();
        assertEquals(a, b);
        assertEquals(a, b);
        a.edit().addEntity(0);
        assertNotEquals(a, b);
        a.edit().addEntity(54);
        assertNotEquals(a, b);
        assertNotEquals(a, new Object());
    }

    @Test
    public void testHashcode() {
        EntitySet a = new EntitySet();
        EntitySet b = new EntitySet();
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(a.hashCode(), b.hashCode());
        a.edit().addEntity(0);
        assertNotEquals(a.hashCode(), b.hashCode());
        a.edit().addEntity(54);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testToString() {
        EntitySet set = new EntitySet();
        assertEquals("[]", set.toString());
        set.edit().addEntity(0);
        assertEquals("[0]", set.toString());
        set.edit().addEntity(4);
        assertEquals("[0, 4]", set.toString());
        set.edit().addEntity(2);
        assertEquals("[0, 2, 4]", set.toString());
    }

    @Test(expected = RetinazerException.class)
    public void testUnmodifiable() {
        new EntitySet().view().edit().addEntity(0);
    }
}
