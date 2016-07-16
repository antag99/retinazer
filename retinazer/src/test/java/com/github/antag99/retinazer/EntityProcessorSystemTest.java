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

import com.github.antag99.retinazer.test.PackedTestRunner;
import com.github.antag99.retinazer.util.Mask;

import static org.junit.Assert.assertEquals;

@RunWith(PackedTestRunner.class)
public class EntityProcessorSystemTest {
    public static final class TestEntityProcessorSystem extends EntityProcessorSystem {
        @SkipWire
        public EntitySet processedEntities = new EntitySet();

        public TestEntityProcessorSystem() {
            super(Family.create());
        }

        @Override
        protected void process(int entity) {
            if (processedEntities.contains(entity))
                throw new AssertionError("entity already processed: " + entity);
            processedEntities.edit().addEntity(entity);
        }
    }

    @Test
    public void testEntityProcessorSystem() {
        TestEntityProcessorSystem system = new TestEntityProcessorSystem();
        Engine engine = new Engine(new EngineConfig().addSystem(system));
        Mask entities = new Mask();
        int a, b, c;
        entities.set(a = engine.createEntity());
        entities.set(b = engine.createEntity());
        entities.set(c = engine.createEntity());
        engine.update();
        assertEquals(system.processedEntities.getMask(), entities);
        system.processedEntities.edit().clear();
        engine.destroyEntity(b);
        entities.clear(b);
        engine.update();
        assertEquals(system.processedEntities.getMask(), entities);
        system.processedEntities.edit().clear();
        entities.set(b = engine.createEntity());
        engine.update();
        assertEquals(system.processedEntities.getMask(), entities);
        system.processedEntities.edit().clear();
        engine.destroyEntity(a);
        engine.destroyEntity(b);
        engine.destroyEntity(c);
        entities.clear(a);
        entities.clear(b);
        entities.clear(c);
    }
}
