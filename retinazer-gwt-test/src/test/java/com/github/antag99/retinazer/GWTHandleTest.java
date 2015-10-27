package com.github.antag99.retinazer;

public class GWTHandleTest extends RetinazerTestCase {
    public void testCreate() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createHandle(engine.createEntity());
        entity.create(FlagComponentA.class);
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.get()));
        engine.update();
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.get()));
    }

    public void testAdd() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createHandle(engine.createEntity());
        entity.add(new FlagComponentA());
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.get()));
        engine.update();
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.get()));
    }

    public void testRemove() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createHandle(engine.createEntity());
        entity.add(new FlagComponentA());
        entity.remove(FlagComponentA.class);
        assertNotNull(engine.getMapper(FlagComponentA.class).get(entity.get()));
        engine.update();
        assertNull(engine.getMapper(FlagComponentA.class).get(entity.get()));
        engine.update();
        assertNull(engine.getMapper(FlagComponentA.class).get(entity.get()));
    }

    public void testDuplicate() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createHandle(engine.createEntity());
        Handle reference = entity.cpy();
        assertEquals(entity.getEngine(), reference.getEngine());
        assertNotNull(reference.getEngine());
        assertEquals(0, entity.get());
        assertEquals(0, reference.get());
    }

    public void testDestroy() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createHandle(engine.createEntity());
        entity.destroy();
        engine.update();
        assertFalse(engine.getEntities().contains(entity.get()));
    }
}
