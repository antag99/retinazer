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
