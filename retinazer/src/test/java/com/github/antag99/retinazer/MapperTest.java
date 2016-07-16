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

public class MapperTest {

    // This should usually not be done... bad.
    public static final class BadComponent implements Component {
        public BadComponent(int requiresAnArgument) {
        }
    }

    @Test(expected = RetinazerException.class)
    public void testNoConstructor() {
        Engine engine = new Engine(new EngineConfig());
        int entity = engine.createEntity();
        Mapper<BadComponent> mBad = engine.getMapper(BadComponent.class);
        mBad.create(entity);
    }

    // This should *never* be done
    public static final class ReallyBadComponent implements Component {
        public ReallyBadComponent() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testErrorConstructor() {
        Engine engine = new Engine(new EngineConfig());
        int entity = engine.createEntity();
        Mapper<ReallyBadComponent> mReallyBad = engine.getMapper(ReallyBadComponent.class);
        mReallyBad.create(entity);
    }

    @Test
    public void testRemoveNothing() {
        Engine engine = new Engine(new EngineConfig());
        Mapper<TagA> mFlagA = engine.getMapper(TagA.class);
        int entity = engine.createEntity();
        mFlagA.remove(entity); // nothing should happen
        engine.update();
        mFlagA.create(entity);
        mFlagA.remove(entity);
        mFlagA.remove(entity);
        engine.update();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTwice() {
        Engine engine = new Engine(new EngineConfig());
        Mapper<TagA> mFlagA = engine.getMapper(TagA.class);
        int entity = engine.createEntity();
        mFlagA.create(entity);
        mFlagA.create(entity);
    }
}
