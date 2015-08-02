package com.github.antag99.retinazer;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class EntitySetTest {
    @Test
    public void testEntities() {
        Engine engine = EngineConfig.create()
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .finish();
        Entity entity0 = engine.createEntity();
        Entity entity1 = engine.createEntity();
        entity1.add(new FlagComponentA());
        Entity entity2 = engine.createEntity();
        Entity entity3 = engine.createEntity();
        Entity entity4 = engine.createEntity();
        engine.update();
        assertTrue(Arrays.equals(engine.getEntities().getEntities(),
                new Entity[] { entity0, entity1, entity2, entity3, entity4 }));
        assertTrue(Arrays.equals(engine.getEntitiesFor(
                Family.with(FlagComponentA.class)).getEntities(),
                new Entity[] { entity1 }));
    }

    @Test
    public void testIndices() {
        Engine engine = EngineConfig.create()
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .finish();
        Entity entity0 = engine.createEntity();
        Entity entity1 = engine.createEntity();
        entity1.add(new FlagComponentA());
        Entity entity2 = engine.createEntity();
        Entity entity3 = engine.createEntity();
        Entity entity4 = engine.createEntity();
        engine.update();
        assertTrue(Arrays.equals(engine.getEntities().getIndices(), new int[] {
                entity0.getIndex(), entity1.getIndex(), entity2.getIndex(),
                entity3.getIndex(), entity4.getIndex() }));
        assertTrue(Arrays.equals(engine.getEntitiesFor(
                Family.with(FlagComponentA.class)).getIndices(), new int[] {
                        entity1.getIndex() }));
    }
}
