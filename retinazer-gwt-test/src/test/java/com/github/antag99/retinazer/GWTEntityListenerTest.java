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

public class GWTEntityListenerTest extends RetinazerTestCase {
    private static class EntityListenerMock implements EntityListener {
        private EntitySet insertedEntities = new EntitySet();
        private EntitySet removedEntities = new EntitySet();

        @Override
        public void inserted(EntitySet entities) {
            if (insertedEntities.size() != 0)
                throw new AssertionError();
            this.insertedEntities = new EntitySet(entities);
        }

        @Override
        public void removed(EntitySet entities) {
            if (removedEntities.size() != 0)
                throw new AssertionError();
            this.removedEntities = new EntitySet(entities);
        }

        public void verifyInserted(int... entities) {
            EntitySet set = new EntitySet();
            for (int e : entities)
                set.edit().addEntity(e);
            assertEquals(set, insertedEntities);
            insertedEntities = new EntitySet();
        }

        public void verifyRemoved(int... entities) {
            EntitySet set = new EntitySet();
            for (int e : entities)
                set.edit().addEntity(e);
            assertEquals(set, removedEntities);
            removedEntities = new EntitySet();
        }
    }

    public void testEntityListener() {
        EntityListenerMock listener = new EntityListenerMock();
        Engine engine = new Engine(new EngineConfig());
        engine.addEntityListener(listener);
        int entity = engine.createEntity();
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
        EntityListenerMock listenerB = new EntityListenerMock();
        EntityListenerMock listenerC = new EntityListenerMock();
        Engine engine = new Engine(new EngineConfig());
        engine.getFamily(Family.with(FlagComponentB.class)).addListener(listenerB);
        engine.getFamily(Family.with(FlagComponentC.class)).addListener(listenerC);
        Mapper<FlagComponentB> mFlagB = engine.getMapper(FlagComponentB.class);
        Mapper<FlagComponentC> mFlagC = engine.getMapper(FlagComponentC.class);
        int entity = engine.createEntity();
        engine.update();
        listenerB.verifyInserted(new int[0]);
        listenerB.verifyRemoved(new int[0]);
        listenerC.verifyInserted(new int[0]);
        listenerC.verifyRemoved(new int[0]);
        mFlagB.create(entity);
        listenerB.verifyInserted(new int[0]);
        listenerB.verifyRemoved(new int[0]);
        listenerC.verifyInserted(new int[0]);
        listenerC.verifyRemoved(new int[0]);
        engine.update();
        listenerB.verifyInserted(entity);
        listenerB.verifyRemoved(new int[0]);
        listenerC.verifyInserted(new int[0]);
        listenerC.verifyRemoved(new int[0]);
        mFlagB.remove(entity);
        engine.update();
        listenerB.verifyInserted(new int[0]);
        listenerB.verifyRemoved(entity);
        listenerC.verifyInserted(new int[0]);
        listenerC.verifyRemoved(new int[0]);
        mFlagC.create(entity);
        engine.update();
        listenerB.verifyInserted(new int[0]);
        listenerB.verifyRemoved(new int[0]);
        listenerC.verifyInserted(entity);
        listenerC.verifyRemoved(new int[0]);
        engine.destroyEntity(entity);
        engine.update();
        listenerB.verifyInserted(new int[0]);
        listenerB.verifyRemoved(new int[0]);
        listenerC.verifyInserted(new int[0]);
        listenerC.verifyRemoved(entity);
    }
}
