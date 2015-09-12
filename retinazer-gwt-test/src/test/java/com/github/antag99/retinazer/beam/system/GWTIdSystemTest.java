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

import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.RetinazerTestCase;
import com.github.antag99.retinazer.beam.component.Id;

public final class GWTIdSystemTest extends RetinazerTestCase {
    private Engine engine;
    private IdSystem idSystem;

    @Override
    protected void gwtSetUp() throws Exception {
        engine = new Engine(new EngineConfig()
                .addSystem(idSystem = new IdSystem()));
    }

    public void testId() {
        Handle entity = engine.createEntity().cpy();
        entity.create(Id.class).id("player");
        engine.update();
        assertEquals(entity.idx(), idSystem.getEntity("player"));
        entity.destroy();
        engine.update();
        assertEquals(-1, idSystem.getEntity("player", true));
    }

    public void testMissingEntity0() {
        try {
            idSystem.getEntity("missing");
        } catch (IllegalArgumentException ex) {
            if (ex.getClass() == IllegalArgumentException.class)
                return;
        }
        fail("IllegalArgumentException expected");
    }

    public void testMissingEntity1() {
        try {
            idSystem.getEntity("missing", false);
        } catch (IllegalArgumentException ex) {
            if (ex.getClass() == IllegalArgumentException.class)
                return;
        }
        fail("IllegalArgumentException expected");
    }

    public void testMissingEntity2() {
        assertEquals(-1, idSystem.getEntity("missing", true));
    }

    public void testDuplicateEntity() {
        try {
            engine.createEntity().create(Id.class).id("duplicate");
            engine.createEntity().create(Id.class).id("duplicate");
            engine.update();
        } catch (IllegalStateException ex) {
            if (ex.getClass() == IllegalStateException.class)
                return;
        }
        fail("IllegalStateException expected");
    }

    public void testNullId() {
        try {
            engine.createEntity().create(Id.class).id = null;
            engine.update();
        } catch (IllegalStateException ex) {
            if (ex.getClass() == IllegalStateException.class)
                return;
        }
        fail("IllegalStateException expected");
    }

    public void testChangingId() {
        try {
            Handle entity = engine.createEntity().cpy();
            Id id = entity.create(Id.class);
            id.id = "good";
            engine.update();
            id.id = "bad";
            entity.destroy();
            engine.update();
        } catch (IllegalStateException ex) {
            if (ex.getClass() == IllegalStateException.class)
                return;
        }
        fail("IllegalStateException expected");
    }
}
