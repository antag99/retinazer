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
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.RetinazerException;
import com.github.antag99.retinazer.beam.component.Uuid;

public final class UuidSystemTest {
    Engine engine;
    UuidSystem uuidSystem;

    @Before
    public void setup() {
        engine = new Engine(new EngineConfig()
                .addSystem(uuidSystem = new UuidSystem()));
    }

    @Test
    public void testUuid() {
        int entity = uuidSystem.createEntity(42L).idx();
        assertEquals(-1, uuidSystem.getEntity(42L, true));
        engine.update();
        assertEquals(entity, uuidSystem.getEntity(42L, false));
        engine.destroyEntity(entity);
        assertEquals(entity, uuidSystem.getEntity(42L));
        engine.update();
        assertEquals(-1, uuidSystem.getEntity(42L, true));
    }

    @Test
    public void testDuplicateUuid() {
        uuidSystem.createEntity(42L);
        uuidSystem.createEntity(42L);
        try {
            engine.update();
        } catch (RetinazerException ex) {
            return;
        }
        fail("RetinazerException expected");
    }

    @Test
    public void testNoEntity0() {
        try {
            uuidSystem.getEntity(5L);
        } catch (IllegalArgumentException ex) {
            if (ex.getClass() == IllegalArgumentException.class)
                return;
        }
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testNoEntity1() {
        try {
            uuidSystem.getEntity(5L, false);
        } catch (IllegalArgumentException ex) {
            if (ex.getClass() == IllegalArgumentException.class)
                return;
        }
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testNoEntity2() {
        assertEquals(-1, uuidSystem.getEntity(5L, true));
    }

    @Test
    public void testSneakChange() {
        Handle entity = uuidSystem.createEntity(5L);
        engine.update();
        entity.get(Uuid.class).uuid = 0L;
        entity.destroy();
        try {
            engine.update();
        } catch (RetinazerException ex) {
            return;
        }
        fail("RetinazerException expected");
    }
}
