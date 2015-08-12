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

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InOrder;

public class EntityListenerTest {
    @Test
    public void testEntityListener() {
        EntitySetListener listener = mock(EntitySetListener.class);
        Engine engine = EngineConfig.create().finish();
        engine.getEntities().addListener(listener);
        Entity entity = engine.createEntity();
        verifyNoMoreInteractions(listener);
        engine.flush();
        verify(listener).inserted(entity);
        verifyNoMoreInteractions(listener);
        entity.destroy();
        verifyNoMoreInteractions(listener);
        engine.update();
        verify(listener).removed(entity);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testFamilyListener() {
        EntitySetListener listenerB = mock(EntitySetListener.class);
        EntitySetListener listenerC = mock(EntitySetListener.class);
        InOrder order = inOrder(listenerB, listenerC);
        Engine engine = EngineConfig.create()
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .finish();
        engine.getEntitiesFor(Family.with(FlagComponentB.class)).addListener(listenerB);
        engine.getEntitiesFor(Family.with(FlagComponentC.class)).addListener(listenerC);
        Entity entity = engine.createEntity();
        engine.update();
        order.verifyNoMoreInteractions();
        entity.add(new FlagComponentB());
        order.verifyNoMoreInteractions();
        engine.flush();
        order.verify(listenerB).inserted(entity);
        order.verifyNoMoreInteractions();
        entity.remove(FlagComponentB.class);
        engine.update();
        order.verify(listenerB).removed(entity);
        order.verifyNoMoreInteractions();
        entity.add(new FlagComponentC());
        engine.update();
        order.verify(listenerC).inserted(entity);
        order.verifyNoMoreInteractions();
        entity.destroy();
        engine.update();
        order.verify(listenerC).removed(entity);
        order.verifyNoMoreInteractions();
    }
}
