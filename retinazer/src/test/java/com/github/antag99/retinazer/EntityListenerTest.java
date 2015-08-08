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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.mockito.InOrder;

import com.github.antag99.retinazer.Event.WithFamily;

public class EntityListenerTest {
    private static class EntitySystemA extends EntitySystem {
        @EventHandler
        public void entityAdd(EntityCreateEvent event) {
        }

        @EventHandler
        public void entityRemove(EntityDestroyEvent event) {
        }
    }

    private static class EntitySystemB extends EntitySystem {
        @EventHandler
        @WithFamily(with = FlagComponentB.class)
        public void entityAdd(EntityMatchEvent event) {
        }

        @EventHandler
        @WithFamily(with = FlagComponentB.class)
        public void entityRemove(EntityUnmatchEvent event) {
        }
    }

    private static class EntitySystemC extends EntitySystem {
        @EventHandler
        @WithFamily(with = FlagComponentC.class)
        public void entityAdd(EntityMatchEvent event) {
        }

        @EventHandler
        @WithFamily(with = FlagComponentC.class)
        public void entityRemove(EntityUnmatchEvent event) {
        }
    }

    @Test
    public void testEntityListener() {
        EntitySystemA listener = spy(new EntitySystemA());
        Engine engine = EngineConfig.create().withSystem(listener).finish();
        Entity entity = engine.createEntity();
        verifyNoMoreInteractions(listener);
        engine.flush();
        verify(listener).entityAdd(any(EntityCreateEvent.class));
        verifyNoMoreInteractions(listener);
        entity.destroy();
        verifyNoMoreInteractions(listener);
        engine.update();
        verify(listener).entityRemove(any(EntityDestroyEvent.class));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testFamilyListener() {
        EntitySystemB listenerB = spy(new EntitySystemB());
        EntitySystemC listenerC = spy(new EntitySystemC());
        InOrder order = inOrder(listenerB, listenerC);
        Engine engine = EngineConfig.create()
                .withSystem(listenerB)
                .withSystem(listenerC)
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .finish();
        Entity entity = engine.createEntity();
        engine.update();
        order.verifyNoMoreInteractions();
        entity.add(new FlagComponentB());
        order.verifyNoMoreInteractions();
        engine.flush();
        order.verify(listenerB).entityAdd(any(EntityMatchEvent.class));
        order.verifyNoMoreInteractions();
        entity.remove(FlagComponentB.class);
        engine.update();
        order.verify(listenerB).entityRemove(any(EntityUnmatchEvent.class));
        order.verifyNoMoreInteractions();
        entity.add(new FlagComponentC());
        engine.update();
        order.verify(listenerC).entityAdd(any(EntityMatchEvent.class));
        order.verifyNoMoreInteractions();
        entity.destroy();
        engine.update();
        order.verify(listenerC).entityRemove(any(EntityUnmatchEvent.class));
        order.verifyNoMoreInteractions();
    }
}
