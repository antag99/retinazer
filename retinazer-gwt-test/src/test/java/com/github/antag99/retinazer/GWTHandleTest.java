package com.github.antag99.retinazer;

public class GWTHandleTest extends RetinazerTestCase {
    public void testCreate() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createHandle(engine.createEntity());
        entity.create(FlagComponentA.class);
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
        engine.update();
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
    }

    public void testAdd() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createHandle(engine.createEntity());
        entity.add(new FlagComponentA());
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
        engine.update();
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
    }

    public void testRemove() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createHandle(engine.createEntity());
        entity.add(new FlagComponentA());
        entity.remove(FlagComponentA.class);
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
        engine.update();
        assertNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
        engine.update();
        assertNull(engine.getMapper(FlagComponentA.class).get(entity.idx()));
    }

    public void testDuplicate() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createHandle(engine.createEntity());
        Handle reference = entity.cpy();
        assertEquals(entity.getEngine(), reference.getEngine());
        assertNotNull(reference.getEngine());
        assertEquals(0, entity.idx());
        assertEquals(0, reference.idx());
    }

    public void testDestroy() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createHandle(engine.createEntity());
        entity.destroy();
        engine.update();
        assertFalse(engine.getEntities().contains(entity.idx()));
    }
}
