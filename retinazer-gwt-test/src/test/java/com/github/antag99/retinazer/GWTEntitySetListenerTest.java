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

import com.badlogic.gdx.utils.IntArray;

public class GWTEntitySetListenerTest extends RetinazerTestCase {
    private static class EntitySetListenerMock implements EntitySetListener {
        private IntArray insertedEntities = new IntArray();
        private IntArray removedEntities = new IntArray();

        @Override
        public void inserted(IntArray entities) {
            if (insertedEntities.size != 0)
                throw new AssertionError();
            this.insertedEntities = new IntArray(entities);
        }

        @Override
        public void removed(IntArray entities) {
            if (removedEntities.size != 0)
                throw new AssertionError();
            this.removedEntities = new IntArray(entities);
        }

        public void verifyInserted(int... entities) {
            IntArray array = IntArray.with(entities);
            assertEquals(array, insertedEntities);
            insertedEntities.clear();
        }

        public void verifyRemoved(int... entities) {
            IntArray array = IntArray.with(entities);
            assertEquals(array, removedEntities);
            removedEntities.clear();
        }
    }

    public void testEntityListener() {
        EntitySetListenerMock listener = new EntitySetListenerMock();
        Engine engine = new Engine(new EngineConfig());
        engine.getEntities().addListener(listener);
        int entity = engine.createEntity().idx();
        listener.verifyInserted(new int[0]);
        listener.verifyRemoved(new int[0]);
        engine.update();
        listener.verifyInserted(entity);
        listener.verifyRemoved(new int[0]);
        engine.destroyEntity(entity);
        listener.verifyInserted(new int[0]);
        listener.verifyRemoved(new int[0]);
        engine.update();
        listener.verifyInserted(new int[0]);
        listener.verifyRemoved(entity);
    }

    public void testFamilyListener() {
        EntitySetListenerMock listenerB = new EntitySetListenerMock();
        EntitySetListenerMock listenerC = new EntitySetListenerMock();
        Engine engine = new Engine(new EngineConfig());
        engine.getFamily(Family.with(FlagComponentB.class)).getEntities().addListener(listenerB);
        engine.getFamily(Family.with(FlagComponentC.class)).getEntities().addListener(listenerC);
        Handle entity = engine.createEntity().cpy();
        engine.update();
        listenerB.verifyInserted(new int[0]);
        listenerB.verifyRemoved(new int[0]);
        listenerC.verifyInserted(new int[0]);
        listenerC.verifyRemoved(new int[0]);
        entity.add(new FlagComponentB());
        listenerB.verifyInserted(new int[0]);
        listenerB.verifyRemoved(new int[0]);
        listenerC.verifyInserted(new int[0]);
        listenerC.verifyRemoved(new int[0]);
        engine.update();
        listenerB.verifyInserted(entity.idx());
        listenerB.verifyRemoved(new int[0]);
        listenerC.verifyInserted(new int[0]);
        listenerC.verifyRemoved(new int[0]);
        entity.remove(FlagComponentB.class);
        engine.update();
        listenerB.verifyInserted(new int[0]);
        listenerB.verifyRemoved(entity.idx());
        listenerC.verifyInserted(new int[0]);
        listenerC.verifyRemoved(new int[0]);
        entity.add(new FlagComponentC());
        engine.update();
        listenerB.verifyInserted(new int[0]);
        listenerB.verifyRemoved(new int[0]);
        listenerC.verifyInserted(entity.idx());
        listenerC.verifyRemoved(new int[0]);
        entity.destroy();
        engine.update();
        listenerB.verifyInserted(new int[0]);
        listenerB.verifyRemoved(new int[0]);
        listenerC.verifyInserted(new int[0]);
        listenerC.verifyRemoved(entity.idx());
    }
}
