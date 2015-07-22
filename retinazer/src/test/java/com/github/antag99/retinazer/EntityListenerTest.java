package com.github.antag99.retinazer;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.mockito.InOrder;

public class EntityListenerTest {
    @Test
    public void testEntityListener() {
        EntityListener listener = mock(EntityListener.class);
        Engine engine = EngineConfig.create().finish();
        engine.addEntityListener(listener);
        Entity entity = engine.createEntity();
        verifyNoMoreInteractions(listener);
        engine.update();
        verify(listener).entityAdd(entity);
        verifyNoMoreInteractions(listener);
        entity.destroy();
        verifyNoMoreInteractions(listener);
        engine.update();
        verify(listener).entityRemove(entity);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testFamilyListener() {
        EntityListener listenerA = mock(EntityListener.class);
        EntityListener listenerB = mock(EntityListener.class);
        InOrder order = inOrder(listenerA, listenerB);
        Engine engine = EngineConfig.create()
            .withComponentType(FlagComponentA.class)
            .withComponentType(FlagComponentB.class)
            .withComponentType(FlagComponentC.class)
            .finish();
        engine.addEntityListener(Family.with(FlagComponentA.class), listenerA);
        engine.addEntityListener(Family.with(FlagComponentB.class), listenerB);
        Entity entity = engine.createEntity();
        engine.update();
        order.verifyNoMoreInteractions();
        entity.add(new FlagComponentA());
        order.verifyNoMoreInteractions();
        engine.update();
        order.verify(listenerA).entityAdd(entity);
        order.verifyNoMoreInteractions();
        entity.remove(FlagComponentA.class);
        engine.update();
        order.verify(listenerA).entityRemove(entity);
        order.verifyNoMoreInteractions();
        entity.add(new FlagComponentB());
        engine.update();
        order.verify(listenerB).entityAdd(entity);
        order.verifyNoMoreInteractions();
        entity.destroy();
        engine.update();
        order.verify(listenerB).entityRemove(entity);
        order.verifyNoMoreInteractions();
    }
}
