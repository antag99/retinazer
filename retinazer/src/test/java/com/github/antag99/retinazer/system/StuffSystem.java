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
package com.github.antag99.retinazer.system;

import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.PackedComponent;
import com.github.antag99.retinazer.PackedMapper;
import com.github.antag99.retinazer.PlainMapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.retinazer.component.Stuff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class StuffSystem extends EntityProcessorSystem {
    private Mapper<Stuff> mStuff;

    private @SkipWire boolean packed;

    public StuffSystem(boolean packed) {
        super(Family.with(Stuff.class));

        this.packed = packed;
    }

    @Override
    protected void initialize() {
        if (packed) {
            assertTrue(mStuff instanceof PackedMapper);
        } else {
            assertTrue(mStuff instanceof PlainMapper);
        }
    }

    public int createStuff() {
        int entity = engine.createEntity();
        Stuff stuff = mStuff.create(entity);
        assertEquals(stuff.myBoolean, true);
        assertEquals(stuff.myByte, 42);
        assertEquals(stuff.myShort, 13);
        assertEquals(stuff.myChar, 3);
        assertEquals(stuff.myInt, 4);
        assertEquals(stuff.myLong, 42424242);
        assertEquals(stuff.myFloat, 3.141592653f, 0f);
        assertEquals(stuff.myDouble, 0.333333333333d, 0f);
        if (packed) {
            assertTrue(PackedComponent.class.isInstance(stuff));
        } else {
            assertTrue(Component.class.isInstance(stuff));
        }
        return entity;
    }

    @Override
    protected void process(int entity) {
        Stuff stuff = mStuff.get(entity);
        stuff.shuffle();
        stuff.square();
    }
}
