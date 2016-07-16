/*******************************************************************************
 * Retinazer, an entity-component-system framework for Java
 *
 * Copyright (C) 2015-2016 Anton Gustafsson
 *
 * This file is part of Retinazer.
 *
 * Retinazer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Retinazer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Retinazer.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.antag99.retinazer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.github.antag99.retinazer.component.TagA;
import com.github.antag99.retinazer.component.TagB;
import com.github.antag99.retinazer.component.TagC;
import com.github.antag99.retinazer.system.TagSystemA;
import com.github.antag99.retinazer.system.TagSystemB;
import com.github.antag99.retinazer.system.TagSystemC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class EngineTest {
    @Test
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

    @Test
    public void testEntitySystemPriority() {
        final List<EntitySystem> initializedSystems = new ArrayList<>();
        final List<EntitySystem> updatedSystems = new ArrayList<>();

        abstract class OrderSystem extends EntitySystem {
            @Override
            protected final void initialize() {
                initializedSystems.add(this);
            }

            @Override
            protected final void update() {
                updatedSystems.add(this);
            }
        }

        EntitySystem entitySystemA = new OrderSystem() {
            @Override
            public String toString() {
                return "A";
            }
        };

        EntitySystem entitySystemB = new OrderSystem() {
            @Override
            public String toString() {
                return "B";
            }
        };

        EntitySystem entitySystemC = new OrderSystem() {
            @Override
            public String toString() {
                return "C";
            }
        };

        EntitySystem entitySystemD = new OrderSystem() {
            @Override
            public String toString() {
                return "D";
            }
        };

        EntitySystem entitySystemE = new OrderSystem() {
            @Override
            public String toString() {
                return "E";
            }
        };

        Engine engine = new Engine(new EngineConfig()
                .addSystem(entitySystemA, Priority.DEFAULT)
                .addSystem(entitySystemB, Priority.DEFAULT)
                .addSystem(entitySystemC, Priority.HIGH)
                .addSystem(entitySystemD, Priority.LOWEST)
                .addSystem(entitySystemE, Priority.DEFAULT));
        assertEquals(Arrays.asList(entitySystemC,
                entitySystemA,
                entitySystemB,
                entitySystemE,
                entitySystemD), initializedSystems);
        initializedSystems.clear();
        engine.update();
        assertEquals(Arrays.asList(entitySystemC,
                entitySystemA,
                entitySystemB,
                entitySystemE,
                entitySystemD), updatedSystems);
        updatedSystems.clear();
    }

    public static Set<Integer> asSet(int... entities) {
        HashSet<Integer> set = new HashSet<>();
        for (int entity : entities)
            set.add(entity);
        return set;
    }

    public static Set<Integer> asSet(EntitySet entities) {
        HashSet<Integer> set = new HashSet<>();
        int[] buffer = entities.getIndices().buffer;
        for (int i = 0, n = entities.size(); i < n; i++)
            set.add(buffer[i]);
        return set;
    }

    @Test
    public void testEntityRetrieval() {
        Engine engine = new Engine(new EngineConfig());
        Mapper<TagA> mFlagA = engine.getMapper(TagA.class);
        Mapper<TagB> mFlagB = engine.getMapper(TagB.class);
        Mapper<TagC> mFlagC = engine.getMapper(TagC.class);

        int entity0 = engine.createEntity();

        int entity1 = engine.createEntity();
        mFlagA.create(entity1);

        int entity2 = engine.createEntity();
        mFlagB.create(entity2);

        int entity3 = engine.createEntity();
        mFlagC.create(entity3);

        int entity4 = engine.createEntity();
        mFlagA.create(entity4);
        mFlagB.create(entity4);

        int entity5 = engine.createEntity();
        mFlagB.create(entity5);
        mFlagC.create(entity5);

        int entity6 = engine.createEntity();
        mFlagA.create(entity6);
        mFlagC.create(entity6);

        int entity7 = engine.createEntity();
        mFlagA.create(entity7);
        mFlagB.create(entity7);
        mFlagC.create(entity7);

        engine.update();

        assertEquals(
                asSet(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                asSet(engine.getEntities()));

        assertEquals(
                asSet(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                asSet(engine.getFamily(Family.with()).getEntities()));

        assertEquals(
                asSet(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                asSet(engine.getFamily(Family.exclude()).getEntities()));

        assertEquals(
                asSet(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                asSet(engine.getFamily(Family.with().exclude()).getEntities()));

        assertEquals(
                asSet(entity1, entity4, entity6, entity7),
                asSet(engine.getFamily(Family.with(TagA.class)).getEntities()));

        assertEquals(
                asSet(entity2, entity4, entity5, entity7),
                asSet(engine.getFamily(Family.with(TagB.class)).getEntities()));

        assertEquals(
                asSet(entity3, entity5, entity6, entity7),
                asSet(engine.getFamily(Family.with(TagC.class)).getEntities()));

        assertEquals(
                asSet(entity4, entity7),
                asSet(engine.getFamily(Family.with(TagA.class, TagB.class)).getEntities()));

        assertEquals(
                asSet(entity6, entity7),
                asSet(engine.getFamily(Family.with(TagA.class, TagC.class)).getEntities()));

        assertEquals(
                asSet(entity5, entity7),
                asSet(engine.getFamily(Family.with(TagB.class, TagC.class)).getEntities()));

        assertEquals(
                asSet(entity7),
                asSet(engine.getFamily(Family.with(TagA.class, TagB.class, TagC.class)).getEntities()));

        assertEquals(
                asSet(entity0, entity2, entity3, entity5),
                asSet(engine.getFamily(Family.exclude(TagA.class)).getEntities()));

        assertEquals(
                asSet(entity0, entity1, entity3, entity6),
                asSet(engine.getFamily(Family.exclude(TagB.class)).getEntities()));

        assertEquals(
                asSet(entity0, entity1, entity2, entity4),
                asSet(engine.getFamily(Family.exclude(TagC.class)).getEntities()));

        assertEquals(
                asSet(entity0, entity3),
                asSet(engine.getFamily(Family.exclude(TagA.class, TagB.class)).getEntities()));

        assertEquals(
                asSet(entity0, entity2),
                asSet(engine.getFamily(Family.exclude(TagA.class, TagC.class)).getEntities()));

        assertEquals(
                asSet(entity0, entity1),
                asSet(engine.getFamily(Family.exclude(TagB.class, TagC.class)).getEntities()));

        assertEquals(
                asSet(entity0),
                asSet(engine.getFamily(
                        Family.exclude(TagA.class, TagB.class, TagC.class)).getEntities()));

        assertEquals(
                asSet(entity1, entity6),
                asSet(engine.getFamily(Family.with(TagA.class).exclude(TagB.class)).getEntities()));

        assertEquals(
                asSet(entity1, entity4),
                asSet(engine.getFamily(Family.with(TagA.class).exclude(TagC.class)).getEntities()));

        assertEquals(
                asSet(entity2, entity5),
                asSet(engine.getFamily(Family.with(TagB.class).exclude(TagA.class)).getEntities()));

        assertEquals(
                asSet(entity2, entity4),
                asSet(engine.getFamily(Family.with(TagB.class).exclude(TagC.class)).getEntities()));

        assertEquals(
                asSet(entity3, entity5),
                asSet(engine.getFamily(Family.with(TagC.class).exclude(TagA.class)).getEntities()));

        assertEquals(
                asSet(entity3, entity6),
                asSet(engine.getFamily(Family.with(TagC.class).exclude(TagB.class)).getEntities()));

        assertEquals(
                asSet(entity1),
                asSet(engine.getFamily(
                        Family.with(TagA.class).exclude(TagB.class, TagC.class)).getEntities()));

        assertEquals(
                asSet(entity2),
                asSet(engine.getFamily(
                        Family.with(TagB.class).exclude(TagA.class, TagC.class)).getEntities()));

        assertEquals(
                asSet(entity3),
                asSet(engine.getFamily(
                        Family.with(TagC.class).exclude(TagA.class, TagB.class)).getEntities()));
    }

    public static class MissingService {
    }

    public static class MissingServiceConsumer {
        private @Wire MissingService service;
    }

    @Test(expected = RetinazerException.class)
    public void testMissingDependencyInjection() {
        MissingServiceConsumer consumer = new MissingServiceConsumer();
        new Engine(new EngineConfig()).wire(consumer);
    }

    @Wire
    public static class ExampleSystem extends EntitySystem {
        public Engine engine;
        public TagSystemA flagSystemA;
        public TagSystemB flagSystemB;
        public TagSystemC flagSystemC;
        public Mapper<TagA> mFlagA;
        public Mapper<TagB> mFlagB;
        public Mapper<TagC> mFlagC;
        // public Mapper<? extends Component> mBad;
        // public Mapper<? extends Component> mWorse;
    }

    @Test
    public void testEngineDependencyInjection() {
        ExampleSystem system = new ExampleSystem();
        TagSystemA flagSystemA = new TagSystemA();
        TagSystemB flagSystemB = new TagSystemB();
        TagSystemC flagSystemC = new TagSystemC();
        Engine engine = new Engine(new EngineConfig()
                .addSystem(system)
                .addSystem(flagSystemA)
                .addSystem(flagSystemB)
                .addSystem(flagSystemC));
        assertSame(engine, system.engine);
        assertSame(flagSystemA, system.flagSystemA);
        assertSame(flagSystemB, system.flagSystemB);
        assertSame(flagSystemC, system.flagSystemC);
        assertSame(engine.getMapper(TagA.class), system.mFlagA);
        assertSame(engine.getMapper(TagB.class), system.mFlagB);
        assertSame(engine.getMapper(TagC.class), system.mFlagC);
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
