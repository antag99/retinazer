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

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.InOrder;

import com.github.antag99.retinazer.Event.WithEntity;

public class EventTest {

    /**
     * Test event that tests no-entity event dispatch
     */
    private static class TestEvent implements Event {
    }

    /**
     * Test sub-interface that flags events as special
     */
    private interface TestEventFlag extends Event {
    }

    private static class TestEventWithFlag extends TestEvent implements TestEventFlag {
    }

    /**
     * Test event that tests entity-based event filtering
     */
    private static class TestEventWithEntity implements Event {
        private Entity theEntity;

        public TestEventWithEntity(Entity theEntity) {
            this.theEntity = theEntity;
        }

        @SuppressWarnings("unused")
        public Entity getEntity() {
            return theEntity;
        }
    }

    private static class TestEventWithEntityAndFlag extends TestEventWithEntity implements TestEventFlag {
        public TestEventWithEntityAndFlag(Entity theEntity) {
            super(theEntity);
        }
    }

    private class TestEventListener implements EventListener {
        @EventHandler(priority = 1)
        public void handleTestEvent(TestEvent event) {
        }

        @EventHandler(priority = 0)
        public void handleTestEventFlag(TestEventFlag event) {
        }

        @EventHandler(priority = 2)
        public void handleTestEventWithEntity(TestEventWithEntity event) {
        }

        @EventHandler(value = {
            @WithEntity(name = "entity", with = {
                FlagComponentA.class
            })
        }, priority = 3)
        public void handleTestEventWithEntityA(TestEventWithEntity event) {
        }

        @EventHandler(value = {
            @WithEntity(name = "entity", with = {
                FlagComponentB.class
            })
        }, priority = 4)
        public void handleTestEventWithEntityB(TestEventWithEntity event) {
        }

        @EventHandler(priority = 5)
        public void handleTestEventWithEntityAndFlag(TestEventWithEntityAndFlag event) {
        }
    }

    @Test
    public void testEventHandler() {
        Engine engine = EngineConfig.create()
            .withComponentType(FlagComponentA.class)
            .withComponentType(FlagComponentB.class)
            .withEventType(TestEvent.class)
            .withEventType(TestEventFlag.class)
            .withEventType(TestEventWithFlag.class)
            .withEventType(TestEventWithEntity.class)
            .withEventType(TestEventWithEntityAndFlag.class)
            .finish();
        TestEventListener testEventListener = mock(TestEventListener.class);
        InOrder order = inOrder(testEventListener);
        engine.addEventListener(testEventListener);
        Entity theEntity = engine.createEntity();
        Entity theEntityA = engine.createEntity();
        theEntityA.add(new FlagComponentA());
        Entity theEntityB = engine.createEntity();
        theEntityB.add(new FlagComponentB());
        Entity theEntityAB = engine.createEntity();
        theEntityAB.add(new FlagComponentA());
        theEntityAB.add(new FlagComponentB());
        engine.update();
        TestEvent testEvent = new TestEvent();
        TestEventWithFlag testEventWithFlag = new TestEventWithFlag();
        TestEventWithEntity testEventWithEntity = new TestEventWithEntity(theEntity);
        TestEventWithEntity testEventWithEntityA = new TestEventWithEntity(theEntityA);
        TestEventWithEntity testEventWithEntityB = new TestEventWithEntity(theEntityB);
        TestEventWithEntity testEventWithEntityAB = new TestEventWithEntity(theEntityAB);
        TestEventWithEntityAndFlag testEventWithEntityAndFlag = new TestEventWithEntityAndFlag(theEntity);
        engine.dispatchEvent(testEvent);
        order.verify(testEventListener).handleTestEvent(testEvent);
        order.verifyNoMoreInteractions();
        engine.dispatchEvent(testEventWithFlag);
        order.verify(testEventListener).handleTestEventFlag(testEventWithFlag);
        order.verify(testEventListener).handleTestEvent(testEventWithFlag);
        order.verifyNoMoreInteractions();
        engine.dispatchEvent(testEventWithEntity);
        order.verify(testEventListener).handleTestEventWithEntity(testEventWithEntity);
        order.verifyNoMoreInteractions();
        engine.dispatchEvent(testEventWithEntityA);
        order.verify(testEventListener).handleTestEventWithEntity(testEventWithEntityA);
        order.verify(testEventListener).handleTestEventWithEntityA(testEventWithEntityA);
        order.verifyNoMoreInteractions();
        engine.dispatchEvent(testEventWithEntityB);
        order.verify(testEventListener).handleTestEventWithEntity(testEventWithEntityB);
        order.verify(testEventListener).handleTestEventWithEntityB(testEventWithEntityB);
        order.verifyNoMoreInteractions();
        engine.dispatchEvent(testEventWithEntityAB);
        order.verify(testEventListener).handleTestEventWithEntity(testEventWithEntityAB);
        order.verify(testEventListener).handleTestEventWithEntityA(testEventWithEntityAB);
        order.verify(testEventListener).handleTestEventWithEntityB(testEventWithEntityAB);
        order.verifyNoMoreInteractions();
        engine.dispatchEvent(testEventWithEntityAndFlag);
        order.verify(testEventListener).handleTestEventFlag(testEventWithEntityAndFlag);
        order.verify(testEventListener).handleTestEventWithEntityAndFlag(testEventWithEntityAndFlag);
        order.verifyNoMoreInteractions();
        engine.removeEventListener(testEventListener);
    }
}
