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
import org.junit.runner.RunWith;

import com.github.antag99.retinazer.system.EngineStateSystem;
import com.github.antag99.retinazer.test.PackedTestRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PackedTestRunner.class)
public final class EngineStateTest {
    @Test
    public void testEngineState() {
        Engine engine = new Engine(new EngineConfig()
                .addSystem(new EngineStateSystem()));
        EngineState state = engine.getState().copy();
        EngineStateSystem system = engine.getSystem(EngineStateSystem.class);
        int entity = system.createEntity();
        assertTrue(system.entityExists(entity));
        engine.getState().copyFrom(state);
        assertFalse(system.entityExists(entity));
        entity = system.createEntity();
        engine.update();
        assertTrue(system.entityExists(entity));
        system.setFloat(entity, 42f);
        engine.getState().copyFrom(state);
        assertTrue(system.getFloat(entity) == 0f);
        system.setFloat(entity, 42f);
        state.copyFrom(engine.getState());
        system.setFloat(entity, 84f);
        assertTrue(system.getFloat(entity) == 84f);
        engine.getState().copyFrom(state);
        assertTrue(system.getFloat(entity) == 42f);
    }
}
