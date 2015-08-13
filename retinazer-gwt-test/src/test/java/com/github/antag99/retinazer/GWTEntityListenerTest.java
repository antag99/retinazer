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

import com.google.gwt.junit.client.GWTTestCase;

public class GWTEntityListenerTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "com.github.antag99.RetinazerTest";
    }

    private class EntitySetListenerMock implements EntitySetListener {
        private Entity insertedEntity = null;
        private Entity removedEntity = null;

        @Override
        public void inserted(Entity entity) {
            if (insertedEntity != null) {
                fail();
            }
            insertedEntity = entity;
        }

        @Override
        public void removed(Entity entity) {
            if (removedEntity != null) {
                fail();
            }
            removedEntity = entity;
        }

        public void verifyInserted(Entity entity) {
            assertSame(entity, insertedEntity);
            insertedEntity = null;
        }

        public void verifyRemoved(Entity entity) {
            assertSame(entity, removedEntity);
            removedEntity = null;
        }
    }

    public void testEntityListener() {
        EntitySetListenerMock listener = new EntitySetListenerMock();
        Engine engine = EngineConfig.create().finish();
        engine.getEntities().addListener(listener);
        Entity entity = engine.createEntity();
        listener.verifyInserted(null);
        listener.verifyRemoved(null);
        engine.flush();
        listener.verifyInserted(entity);
        listener.verifyRemoved(null);
        entity.destroy();
        listener.verifyInserted(null);
        listener.verifyRemoved(null);
        engine.update();
        listener.verifyInserted(null);
        listener.verifyRemoved(entity);
    }

    public void testFamilyListener() {
        EntitySetListenerMock listenerB = new EntitySetListenerMock();
        EntitySetListenerMock listenerC = new EntitySetListenerMock();
        Engine engine = EngineConfig.create()
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .finish();
        engine.getEntitiesFor(Family.with(FlagComponentB.class)).addListener(listenerB);
        engine.getEntitiesFor(Family.with(FlagComponentC.class)).addListener(listenerC);
        Entity entity = engine.createEntity();
        engine.update();
        listenerB.verifyInserted(null);
        listenerB.verifyRemoved(null);
        listenerC.verifyInserted(null);
        listenerC.verifyRemoved(null);
        entity.add(new FlagComponentB());
        listenerB.verifyInserted(null);
        listenerB.verifyRemoved(null);
        listenerC.verifyInserted(null);
        listenerC.verifyRemoved(null);
        engine.flush();
        listenerB.verifyInserted(entity);
        listenerB.verifyRemoved(null);
        listenerC.verifyInserted(null);
        listenerC.verifyRemoved(null);
        entity.remove(FlagComponentB.class);
        engine.update();
        listenerB.verifyInserted(null);
        listenerB.verifyRemoved(entity);
        listenerC.verifyInserted(null);
        listenerC.verifyRemoved(null);
        entity.add(new FlagComponentC());
        engine.update();
        listenerB.verifyInserted(null);
        listenerB.verifyRemoved(null);
        listenerC.verifyInserted(entity);
        listenerC.verifyRemoved(null);
        entity.destroy();
        engine.update();
        listenerB.verifyInserted(null);
        listenerB.verifyRemoved(null);
        listenerC.verifyInserted(null);
        listenerC.verifyRemoved(entity);
    }
}
