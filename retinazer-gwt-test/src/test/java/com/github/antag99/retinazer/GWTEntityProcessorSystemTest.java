package com.github.antag99.retinazer;

import com.github.antag99.retinazer.utils.Mask;

public class GWTEntityProcessorSystemTest extends RetinazerTestCase {
    public static final class TestEntityProcessorSystem extends EntityProcessorSystem {
        public EntitySet processedEntities = new EntitySet();

        public TestEntityProcessorSystem() {
            super(Family.create());
        }

        @Override
        protected void process(int entity) {
            if (processedEntities.contains(entity))
                throw new IllegalStateException("entity already processed: " + entity);
            processedEntities.addEntity(entity);
        }
    }

    public void testEntityProcessorSystem() {
        TestEntityProcessorSystem system = new TestEntityProcessorSystem();
        Engine engine = new Engine(new EngineConfig().addSystem(system));
        Mask entities = new Mask();
        int a, b, c;
        entities.set(a = engine.createEntity().idx());
        entities.set(b = engine.createEntity().idx());
        entities.set(c = engine.createEntity().idx());
        engine.update();
        assertEquals(system.processedEntities.getMask(), entities);
        system.processedEntities.clear();
        engine.destroyEntity(b);
        entities.clear(b);
        engine.update();
        assertEquals(system.processedEntities.getMask(), entities);
        system.processedEntities.clear();
        entities.set(b = engine.createEntity().idx());
        engine.update();
        assertEquals(system.processedEntities.getMask(), entities);
        system.processedEntities.clear();
        engine.destroyEntity(a);
        engine.destroyEntity(b);
        engine.destroyEntity(c);
        entities.clear(a);
        entities.clear(b);
        entities.clear(c);
    }
}
