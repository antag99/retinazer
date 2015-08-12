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

import static com.github.antag99.retinazer.TestUtils.assertEqualsUnordered;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.junit.Test;

public class EngineTest {
    @Test
    public void testEngine() {
        Engine engine = EngineConfig.create().finish();
        engine.update();
        engine.update();
        engine.update();
        engine.update();
        engine.update();
        engine.update();
        engine.update();
        engine.update();
    }

    @Test
    public void testEntityRetrieval() {
        Engine engine = EngineConfig.create()
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .finish();

        Entity entity0 = engine.createEntity();

        Entity entity1 = engine.createEntity();
        entity1.add(new FlagComponentA());

        Entity entity2 = engine.createEntity();
        entity2.add(new FlagComponentB());

        Entity entity3 = engine.createEntity();
        entity3.add(new FlagComponentC());

        Entity entity4 = engine.createEntity();
        entity4.add(new FlagComponentA());
        entity4.add(new FlagComponentB());

        Entity entity5 = engine.createEntity();
        entity5.add(new FlagComponentB());
        entity5.add(new FlagComponentC());

        Entity entity6 = engine.createEntity();
        entity6.add(new FlagComponentA());
        entity6.add(new FlagComponentC());

        Entity entity7 = engine.createEntity();
        entity7.add(new FlagComponentA());
        entity7.add(new FlagComponentB());
        entity7.add(new FlagComponentC());

        engine.flush();

        assertEqualsUnordered(
                Arrays.asList(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                Arrays.asList(engine.getEntities().getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                Arrays.asList(engine.getEntitiesFor(Family.with()).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                Arrays.asList(engine.getEntitiesFor(Family.exclude()).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                Arrays.asList(engine.getEntitiesFor(Family.with().exclude()).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity1, entity4, entity6, entity7),
                Arrays.asList(engine.getEntitiesFor(Family.with(FlagComponentA.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity2, entity4, entity5, entity7),
                Arrays.asList(engine.getEntitiesFor(Family.with(FlagComponentB.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity3, entity5, entity6, entity7),
                Arrays.asList(engine.getEntitiesFor(Family.with(FlagComponentC.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity4, entity7),
                Arrays.asList(engine.getEntitiesFor(Family.with(FlagComponentA.class, FlagComponentB.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity6, entity7),
                Arrays.asList(engine.getEntitiesFor(Family.with(FlagComponentA.class, FlagComponentC.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity5, entity7),
                Arrays.asList(engine.getEntitiesFor(Family.with(FlagComponentB.class, FlagComponentC.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity7),
                Arrays.asList(engine.getEntitiesFor(Family.with(FlagComponentA.class, FlagComponentB.class, FlagComponentC.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity2, entity3, entity5),
                Arrays.asList(engine.getEntitiesFor(Family.exclude(FlagComponentA.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity1, entity3, entity6),
                Arrays.asList(engine.getEntitiesFor(Family.exclude(FlagComponentB.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity1, entity2, entity4),
                Arrays.asList(engine.getEntitiesFor(Family.exclude(FlagComponentC.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity3),
                Arrays.asList(engine.getEntitiesFor(Family.exclude(FlagComponentA.class, FlagComponentB.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity2),
                Arrays.asList(engine.getEntitiesFor(Family.exclude(FlagComponentA.class, FlagComponentC.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity1),
                Arrays.asList(engine.getEntitiesFor(Family.exclude(FlagComponentB.class, FlagComponentC.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity0),
                Arrays.asList(engine.getEntitiesFor(
                        Family.exclude(FlagComponentA.class, FlagComponentB.class, FlagComponentC.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity1, entity6),
                Arrays.asList(engine.getEntitiesFor(Family.with(FlagComponentA.class).exclude(FlagComponentB.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity1, entity4),
                Arrays.asList(engine.getEntitiesFor(Family.with(FlagComponentA.class).exclude(FlagComponentC.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity2, entity5),
                Arrays.asList(engine.getEntitiesFor(Family.with(FlagComponentB.class).exclude(FlagComponentA.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity2, entity4),
                Arrays.asList(engine.getEntitiesFor(Family.with(FlagComponentB.class).exclude(FlagComponentC.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity3, entity5),
                Arrays.asList(engine.getEntitiesFor(Family.with(FlagComponentC.class).exclude(FlagComponentA.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity3, entity6),
                Arrays.asList(engine.getEntitiesFor(Family.with(FlagComponentC.class).exclude(FlagComponentB.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity1),
                Arrays.asList(engine.getEntitiesFor(
                        Family.with(FlagComponentA.class).exclude(FlagComponentB.class, FlagComponentC.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity2),
                Arrays.asList(engine.getEntitiesFor(
                        Family.with(FlagComponentB.class).exclude(FlagComponentA.class, FlagComponentC.class)).getEntities()));

        assertEqualsUnordered(
                Arrays.asList(entity3),
                Arrays.asList(engine.getEntitiesFor(
                        Family.with(FlagComponentC.class).exclude(FlagComponentA.class, FlagComponentB.class)).getEntities()));
    }

    private static class MissingService {
    }

    private static class MissingServiceConsumer {
        private @Wire MissingService service;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingDependencyInjection() {
        MissingServiceConsumer consumer = new MissingServiceConsumer();
        EngineConfig.create().finish().wire(consumer);
    }

    private static class ExampleSystem extends EntitySystem {
        public @Wire Engine engine;
        public @Wire FlagSystemA flagSystemA;
        public @Wire FlagSystemB flagSystemB;
        public @Wire FlagSystemC flagSystemC;
    }

    @Test
    public void testEngineDependencyInjection() {
        ExampleSystem system = new ExampleSystem();
        FlagSystemA flagSystemA = new FlagSystemA();
        FlagSystemB flagSystemB = new FlagSystemB();
        FlagSystemC flagSystemC = new FlagSystemC();
        Engine engine = EngineConfig.create()
                .withSystem(system)
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .withSystem(flagSystemA)
                .withSystem(flagSystemB)
                .withSystem(flagSystemC).finish();
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

    private static class MissingSystem extends EntitySystem {
    }

    private static class MissingSystemConsumer extends EntitySystem {
        public @Wire MissingSystem system;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingEngineDependencyInjection() {
        EngineConfig.create().withSystem(new MissingSystemConsumer()).finish();
    }
}
