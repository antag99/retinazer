package com.github.antag99.retinazer;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.antag99.retinazer.util.Mask;

public class EntityProcessorSystemTest {
    public static final class TestEntityProcessorSystem extends EntityProcessorSystem {
        @SkipWire
        public EntitySet processedEntities = new EntitySet();

        public TestEntityProcessorSystem() {
            super(Family.create());
        }

        @Override
        protected void process(int entity) {
            if (processedEntities.contains(entity))
                throw new AssertionError("entity already processed: " + entity);
            processedEntities.addEntity(entity);
        }
    }

    @Test
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
