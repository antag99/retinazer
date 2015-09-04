package com.github.antag99.retinazer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HandleTest {
    @Test
    public void testCreate() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createEntity();
        entity.create(FlagComponentA.class);
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
        engine.update();
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
    }

    @Test
    public void testAdd() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createEntity();
        entity.add(new FlagComponentA());
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
        engine.update();
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
    }

    @Test
    public void testRemove() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createEntity();
        entity.add(new FlagComponentA());
        entity.remove(FlagComponentA.class);
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
        engine.update();
        assertNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
        engine.update();
        assertNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
    }

    @Test
    public void testDuplicate() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createEntity();
        Handle reference = entity.cpy();
        assertEquals(entity.getEngine(), reference.getEngine());
        assertNotNull(reference.getEngine());
        assertEquals(0, entity.idx());
        assertEquals(0, reference.idx());
    }

    @Test
    public void testDestroy() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createEntity().cpy();
        entity.destroy();
        engine.update();
        assertFalse(engine.getEntities().contains(entity.idx()));
    }

    @Test
    public void testComponentsDestroyed() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createEntity().cpy();
        entity.create(FlagComponentA.class);
        entity.destroy();
        engine.update();
        engine.createEntity();
        assertNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
    }
}
