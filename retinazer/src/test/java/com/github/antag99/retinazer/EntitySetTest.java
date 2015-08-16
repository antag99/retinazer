package com.github.antag99.retinazer;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class EntitySetTest {
    @Test
    public void testIndices() {
        Engine engine = EngineConfig.create().finish();
        int entity0 = engine.createEntity().getEntity();
        int entity1 = engine.createEntity().add(new FlagComponentA()).getEntity();
        int entity2 = engine.createEntity().getEntity();
        int entity3 = engine.createEntity().getEntity();
        int entity4 = engine.createEntity().getEntity();
        engine.update();
        assertTrue(Arrays.equals(engine.getEntities().getIndices().toArray(),
                new int[] { entity0, entity1, entity2, entity3, entity4 }));
        assertTrue(Arrays.equals(engine.getEntitiesFor(
                Family.with(FlagComponentA.class)).getIndices().toArray(),
                new int[] { entity1 }));
    }
}
