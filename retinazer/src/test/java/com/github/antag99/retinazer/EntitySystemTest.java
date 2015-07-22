package com.github.antag99.retinazer;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.InOrder;

public class EntitySystemTest {
    @Test
    public void testEntitySystem() {
        EntitySystem system = mock(EntitySystem.class);
        Engine engine = EngineConfig.create().withSystem(system).finish();
        InOrder order = inOrder(system);
        order.verify(system).initialize();
        engine.update();
        order.verify(system).beforeUpdate();
        order.verify(system).update();
        order.verify(system).afterUpdate();
        order.verifyNoMoreInteractions();
        engine.reset();
        order.verify(system).destroy();
        order.verify(system).initialize();
    }
}
