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

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.utils.IntArray;
import com.google.gwt.junit.client.GWTTestCase;

import junit.framework.Assert;

public class GWTEngineTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "com.github.antag99.RetinazerTest";
    }

    public void testEngine() {
        Engine engine = new Engine(new EngineConfig());
        engine.update();
        engine.update();
        engine.update();
        engine.update();
        engine.update();
        engine.update();
        engine.update();
        engine.update();
    }

    private Set<Integer> asSet(int... entities) {
        HashSet<Integer> set = new HashSet<>();
        for (int entity : entities)
            set.add(entity);
        return set;
    }

    private Set<Integer> asSet(IntArray entities) {
        return asSet(entities.toArray());
    }

    public void testEntityRetrieval() {
        Engine engine = new Engine(new EngineConfig());

        int entity0 = engine.createEntity().getEntity();

        int entity1 = engine.createEntity()
                .add(new FlagComponentA())
                .getEntity();

        int entity2 = engine.createEntity()
                .add(new FlagComponentB())
                .getEntity();

        int entity3 = engine.createEntity()
                .add(new FlagComponentC())
                .getEntity();

        int entity4 = engine.createEntity()
                .add(new FlagComponentA())
                .add(new FlagComponentB())
                .getEntity();

        int entity5 = engine.createEntity()
                .add(new FlagComponentB())
                .add(new FlagComponentC())
                .getEntity();

        int entity6 = engine.createEntity()
                .add(new FlagComponentA())
                .add(new FlagComponentC())
                .getEntity();

        int entity7 = engine.createEntity()
                .add(new FlagComponentA())
                .add(new FlagComponentB())
                .add(new FlagComponentC())
                .getEntity();

        engine.update();

        assertEquals(
                asSet(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                asSet(engine.getEntities().getIndices()));

        assertEquals(
                asSet(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                asSet(engine.getEntitiesFor(Family.with()).getIndices()));

        assertEquals(
                asSet(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                asSet(engine.getEntitiesFor(Family.exclude()).getIndices()));

        assertEquals(
                asSet(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                asSet(engine.getEntitiesFor(Family.with().exclude()).getIndices()));

        assertEquals(
                asSet(entity1, entity4, entity6, entity7),
                asSet(engine.getEntitiesFor(Family.with(FlagComponentA.class)).getIndices()));

        assertEquals(
                asSet(entity2, entity4, entity5, entity7),
                asSet(engine.getEntitiesFor(Family.with(FlagComponentB.class)).getIndices()));

        assertEquals(
                asSet(entity3, entity5, entity6, entity7),
                asSet(engine.getEntitiesFor(Family.with(FlagComponentC.class)).getIndices()));

        assertEquals(
                asSet(entity4, entity7),
                asSet(engine.getEntitiesFor(Family.with(FlagComponentA.class, FlagComponentB.class)).getIndices()));

        assertEquals(
                asSet(entity6, entity7),
                asSet(engine.getEntitiesFor(Family.with(FlagComponentA.class, FlagComponentC.class)).getIndices()));

        assertEquals(
                asSet(entity5, entity7),
                asSet(engine.getEntitiesFor(Family.with(FlagComponentB.class, FlagComponentC.class)).getIndices()));

        assertEquals(
                asSet(entity7),
                asSet(engine.getEntitiesFor(Family.with(FlagComponentA.class, FlagComponentB.class, FlagComponentC.class)).getIndices()));

        assertEquals(
                asSet(entity0, entity2, entity3, entity5),
                asSet(engine.getEntitiesFor(Family.exclude(FlagComponentA.class)).getIndices()));

        assertEquals(
                asSet(entity0, entity1, entity3, entity6),
                asSet(engine.getEntitiesFor(Family.exclude(FlagComponentB.class)).getIndices()));

        assertEquals(
                asSet(entity0, entity1, entity2, entity4),
                asSet(engine.getEntitiesFor(Family.exclude(FlagComponentC.class)).getIndices()));

        assertEquals(
                asSet(entity0, entity3),
                asSet(engine.getEntitiesFor(Family.exclude(FlagComponentA.class, FlagComponentB.class)).getIndices()));

        assertEquals(
                asSet(entity0, entity2),
                asSet(engine.getEntitiesFor(Family.exclude(FlagComponentA.class, FlagComponentC.class)).getIndices()));

        assertEquals(
                asSet(entity0, entity1),
                asSet(engine.getEntitiesFor(Family.exclude(FlagComponentB.class, FlagComponentC.class)).getIndices()));

        assertEquals(
                asSet(entity0),
                asSet(engine.getEntitiesFor(
                        Family.exclude(FlagComponentA.class, FlagComponentB.class, FlagComponentC.class)).getIndices()));

        assertEquals(
                asSet(entity1, entity6),
                asSet(engine.getEntitiesFor(Family.with(FlagComponentA.class).exclude(FlagComponentB.class)).getIndices()));

        assertEquals(
                asSet(entity1, entity4),
                asSet(engine.getEntitiesFor(Family.with(FlagComponentA.class).exclude(FlagComponentC.class)).getIndices()));

        assertEquals(
                asSet(entity2, entity5),
                asSet(engine.getEntitiesFor(Family.with(FlagComponentB.class).exclude(FlagComponentA.class)).getIndices()));

        assertEquals(
                asSet(entity2, entity4),
                asSet(engine.getEntitiesFor(Family.with(FlagComponentB.class).exclude(FlagComponentC.class)).getIndices()));

        assertEquals(
                asSet(entity3, entity5),
                asSet(engine.getEntitiesFor(Family.with(FlagComponentC.class).exclude(FlagComponentA.class)).getIndices()));

        assertEquals(
                asSet(entity3, entity6),
                asSet(engine.getEntitiesFor(Family.with(FlagComponentC.class).exclude(FlagComponentB.class)).getIndices()));

        assertEquals(
                asSet(entity1),
                asSet(engine.getEntitiesFor(
                        Family.with(FlagComponentA.class).exclude(FlagComponentB.class, FlagComponentC.class)).getIndices()));

        assertEquals(
                asSet(entity2),
                asSet(engine.getEntitiesFor(
                        Family.with(FlagComponentB.class).exclude(FlagComponentA.class, FlagComponentC.class)).getIndices()));

        assertEquals(
                asSet(entity3),
                asSet(engine.getEntitiesFor(
                        Family.with(FlagComponentC.class).exclude(FlagComponentA.class, FlagComponentB.class)).getIndices()));
    }

    public static class MissingService {
    }

    public static class MissingServiceConsumer {
        private @Wire MissingService service;
    }

    public void testMissingDependencyInjection() {
        MissingServiceConsumer consumer = new MissingServiceConsumer();
        Engine engine = new Engine(new EngineConfig());
        try {
            engine.wire(consumer);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
        }
    }

    public static class ExampleSystem extends EntitySystem {
        public @Wire Engine engine;
        public @Wire FlagSystemA flagSystemA;
        public @Wire FlagSystemB flagSystemB;
        public @Wire FlagSystemC flagSystemC;
    }

    public void testEngineDependencyInjection() {
        ExampleSystem system = new ExampleSystem();
        FlagSystemA flagSystemA = new FlagSystemA();
        FlagSystemB flagSystemB = new FlagSystemB();
        FlagSystemC flagSystemC = new FlagSystemC();
        Engine engine = new Engine(new EngineConfig()
                .addSystem(system)
                .addSystem(flagSystemA)
                .addSystem(flagSystemB)
                .addSystem(flagSystemC));
        assertSame(engine, system.engine);
        assertSame(flagSystemA, system.flagSystemA);
        assertSame(flagSystemB, system.flagSystemB);
        assertSame(flagSystemC, system.flagSystemC);
        engine.unwire(system);
        engine.wire(system);
        assertSame(engine, system.engine);
        assertSame(flagSystemA, system.flagSystemA);
        assertSame(flagSystemB, system.flagSystemB);
        assertSame(flagSystemC, system.flagSystemC);
    }

    public static class MissingSystem extends EntitySystem {
    }

    public static class MissingSystemConsumer extends EntitySystem {
        public @Wire MissingSystem system;
    }

    public void testMissingEngineDependencyInjection() {
        try {
            new Engine(new EngineConfig().addSystem(new MissingSystemConsumer()));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
        }
    }

}
