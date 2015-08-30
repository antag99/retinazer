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
package com.github.antag99.retinazer.beam.system;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.beam.component.Id;

public final class IdSystemTest extends EntitySystem {
    private Engine engine;
    private IdSystem idSystem;

    @Before
    public void initialize() {
        engine = new Engine(new EngineConfig()
                .addSystem(idSystem = new IdSystem()));
    }

    @Test
    public void testId() {
        Handle entity = engine.createEntity().duplicate();
        entity.create(Id.class).id("player");
        engine.update();
        assertEquals(entity.getEntity(), idSystem.getEntity("player"));
        entity.destroy();
        engine.update();
        assertEquals(-1, idSystem.getEntity("player", true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingEntity0() {
        idSystem.getEntity("missing");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingEntity1() {
        idSystem.getEntity("missing", false);
    }

    @Test
    public void testMissingEntity2() {
        assertEquals(-1, idSystem.getEntity("missing", true));
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicateEntity() {
        engine.createEntity().create(Id.class).id("duplicate");
        engine.createEntity().create(Id.class).id("duplicate");
        engine.update();
    }

    @Test(expected = IllegalStateException.class)
    public void testNullId() {
        engine.createEntity().create(Id.class).id = null;
        engine.update();
    }

    @Test(expected = IllegalStateException.class)
    public void testChangingId() {
        Handle entity = engine.createEntity().duplicate();
        Id id = entity.create(Id.class);
        id.id = "good";
        engine.update();
        id.id = "bad";
        entity.destroy();
        engine.update();
    }
}
