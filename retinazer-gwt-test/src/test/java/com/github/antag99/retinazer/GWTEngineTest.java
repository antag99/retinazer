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

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;

public class GWTEngineTest extends RetinazerTestCase {
    private Array<EntitySystem> initializedSystems = new Array<>();
    private Array<EntitySystem> updatedSystems = new Array<>();

    public abstract class OrderSystem extends EntitySystem {
        @Override
        protected final void initialize() {
            initializedSystems.add(this);
        }

        @Override
        protected final void update() {
            updatedSystems.add(this);
        }
    }

    public final class OrderSystemA extends OrderSystem {
    }

    public final class OrderSystemB extends OrderSystem {
    }

    public final class OrderSystemC extends OrderSystem {
    }

    public final class OrderSystemD extends OrderSystem {
    }

    public final class OrderSystemE extends OrderSystem {
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

    public void testEntitySystemPriority() {
        EntitySystem entitySystemA = new OrderSystemA();
        EntitySystem entitySystemB = new OrderSystemB();
        EntitySystem entitySystemC = new OrderSystemC();
        EntitySystem entitySystemD = new OrderSystemD();
        EntitySystem entitySystemE = new OrderSystemE();

        Engine engine = new Engine(new EngineConfig()
                .addSystem(entitySystemA, Priority.DEFAULT)
                .addSystem(entitySystemB, Priority.DEFAULT)
                .addSystem(entitySystemC, Priority.HIGH)
                .addSystem(entitySystemD, Priority.LOWEST)
                .addSystem(entitySystemE, Priority.DEFAULT));
        assertEquals(Array.with(entitySystemC,
                entitySystemA,
                entitySystemB,
                entitySystemE,
                entitySystemD), initializedSystems);
        initializedSystems.clear();
        engine.update();
        assertEquals(Array.with(entitySystemC,
                entitySystemA,
                entitySystemB,
                entitySystemE,
                entitySystemD), updatedSystems);
        updatedSystems.clear();
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

        int entity0 = engine.createEntity().idx();

        int entity1 = engine.createEntity()
                .add(new FlagComponentA())
                .idx();

        int entity2 = engine.createEntity()
                .add(new FlagComponentB())
                .idx();

        int entity3 = engine.createEntity()
                .add(new FlagComponentC())
                .idx();

        int entity4 = engine.createEntity()
                .add(new FlagComponentA())
                .add(new FlagComponentB())
                .idx();

        int entity5 = engine.createEntity()
                .add(new FlagComponentB())
                .add(new FlagComponentC())
                .idx();

        int entity6 = engine.createEntity()
                .add(new FlagComponentA())
                .add(new FlagComponentC())
                .idx();

        int entity7 = engine.createEntity()
                .add(new FlagComponentA())
                .add(new FlagComponentB())
                .add(new FlagComponentC())
                .idx();

        engine.update();

        assertEquals(
                asSet(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                asSet(engine.getEntities().getIndices()));

        assertEquals(
                asSet(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                asSet(engine.getFamily(Family.with()).getEntities().getIndices()));

        assertEquals(
                asSet(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                asSet(engine.getFamily(Family.exclude()).getEntities().getIndices()));

        assertEquals(
                asSet(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                asSet(engine.getFamily(Family.with().exclude()).getEntities().getIndices()));

        assertEquals(
                asSet(entity1, entity4, entity6, entity7),
                asSet(engine.getFamily(Family.with(FlagComponentA.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity2, entity4, entity5, entity7),
                asSet(engine.getFamily(Family.with(FlagComponentB.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity3, entity5, entity6, entity7),
                asSet(engine.getFamily(Family.with(FlagComponentC.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity4, entity7),
                asSet(engine.getFamily(Family.with(FlagComponentA.class, FlagComponentB.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity6, entity7),
                asSet(engine.getFamily(Family.with(FlagComponentA.class, FlagComponentC.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity5, entity7),
                asSet(engine.getFamily(Family.with(FlagComponentB.class, FlagComponentC.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity7),
                asSet(engine.getFamily(Family.with(FlagComponentA.class, FlagComponentB.class, FlagComponentC.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity0, entity2, entity3, entity5),
                asSet(engine.getFamily(Family.exclude(FlagComponentA.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity0, entity1, entity3, entity6),
                asSet(engine.getFamily(Family.exclude(FlagComponentB.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity0, entity1, entity2, entity4),
                asSet(engine.getFamily(Family.exclude(FlagComponentC.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity0, entity3),
                asSet(engine.getFamily(Family.exclude(FlagComponentA.class, FlagComponentB.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity0, entity2),
                asSet(engine.getFamily(Family.exclude(FlagComponentA.class, FlagComponentC.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity0, entity1),
                asSet(engine.getFamily(Family.exclude(FlagComponentB.class, FlagComponentC.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity0),
                asSet(engine.getFamily(
                        Family.exclude(FlagComponentA.class, FlagComponentB.class, FlagComponentC.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity1, entity6),
                asSet(engine.getFamily(Family.with(FlagComponentA.class).exclude(FlagComponentB.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity1, entity4),
                asSet(engine.getFamily(Family.with(FlagComponentA.class).exclude(FlagComponentC.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity2, entity5),
                asSet(engine.getFamily(Family.with(FlagComponentB.class).exclude(FlagComponentA.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity2, entity4),
                asSet(engine.getFamily(Family.with(FlagComponentB.class).exclude(FlagComponentC.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity3, entity5),
                asSet(engine.getFamily(Family.with(FlagComponentC.class).exclude(FlagComponentA.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity3, entity6),
                asSet(engine.getFamily(Family.with(FlagComponentC.class).exclude(FlagComponentB.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity1),
                asSet(engine.getFamily(
                        Family.with(FlagComponentA.class).exclude(FlagComponentB.class, FlagComponentC.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity2),
                asSet(engine.getFamily(
                        Family.with(FlagComponentB.class).exclude(FlagComponentA.class, FlagComponentC.class)).getEntities().getIndices()));

        assertEquals(
                asSet(entity3),
                asSet(engine.getFamily(
                        Family.with(FlagComponentC.class).exclude(FlagComponentA.class, FlagComponentB.class)).getEntities().getIndices()));
    }

    public static class MissingService {
    }

    public static class MissingServiceConsumer {
        private @Wire MissingService service;
    }

    public void testMissingDependencyInjection() {
        MissingServiceConsumer consumer = new MissingServiceConsumer();
        try {
            new Engine(new EngineConfig()).wire(consumer);
        } catch (RetinazerException ex) {
            return;
        }
        fail("expected RetinazerException");
    }

    @Wire
    public static class ExampleSystem extends EntitySystem {
        public Engine engine;
        public FlagSystemA flagSystemA;
        public FlagSystemB flagSystemB;
        public FlagSystemC flagSystemC;
        public Mapper<FlagComponentA> mFlagA;
        public Mapper<FlagComponentB> mFlagB;
        public Mapper<FlagComponentC> mFlagC;
        // public Mapper<? extends Component> mBad;
        // public Mapper<? extends Component> mWorse;
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
        assertSame(engine.getMapper(FlagComponentA.class), system.mFlagA);
        assertSame(engine.getMapper(FlagComponentB.class), system.mFlagB);
        assertSame(engine.getMapper(FlagComponentC.class), system.mFlagC);
        // assertSame(null, system.mBad);
        // assertSame(null, system.mWorse);
        engine.unwire(system);
        assertSame(null, system.flagSystemA);
        assertSame(null, system.flagSystemB);
        assertSame(null, system.flagSystemC);
        assertSame(null, system.mFlagA);
        assertSame(null, system.mFlagB);
        assertSame(null, system.mFlagC);
        // assertSame(null, system.mBad);
        // assertSame(null, system.mWorse);
        engine.wire(system);
        assertSame(engine, system.engine);
        assertSame(flagSystemA, system.flagSystemA);
        assertSame(flagSystemB, system.flagSystemB);
        assertSame(flagSystemC, system.flagSystemC);
        // assertSame(null, system.mBad);
        // assertSame(null, system.mWorse);
    }
}
