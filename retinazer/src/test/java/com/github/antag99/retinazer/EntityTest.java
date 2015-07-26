package com.github.antag99.retinazer;

import static com.github.antag99.retinazer.TestUtils.assertEqualsUnordered;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

public class EntityTest {
    @Test
    public void testEntity() {
        Engine engine = EngineConfig.create()
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .finish();
        Entity entity = engine.createEntity();
        FlagComponentA flagComponentA = new FlagComponentA();
        FlagComponentB flagComponentB = new FlagComponentB();
        FlagComponentC flagComponentC = new FlagComponentC();
        entity.add(flagComponentA);
        entity.add(flagComponentB);
        entity.add(flagComponentC);
        assertEqualsUnordered(
                Arrays.asList(),
                entity.getComponents());
        engine.update();
        assertEqualsUnordered(
                Arrays.asList(flagComponentA, flagComponentB, flagComponentC),
                entity.getComponents());
        entity.remove(FlagComponentB.class);
        assertEqualsUnordered(
                Arrays.asList(flagComponentA, flagComponentB, flagComponentC),
                entity.getComponents());
        engine.update();
        assertEqualsUnordered(
                Arrays.asList(flagComponentA, flagComponentC),
                entity.getComponents());
        FlagComponentA newFlagComponentA = new FlagComponentA();
        entity.add(newFlagComponentA);
        assertEqualsUnordered(
                Arrays.asList(flagComponentA, flagComponentC),
                entity.getComponents());
        engine.update();
        assertEqualsUnordered(
                Arrays.asList(newFlagComponentA, flagComponentC),
                entity.getComponents());
        FlagComponentC newFlagComponentC = new FlagComponentC();
        entity.remove(FlagComponentC.class);
        entity.add(newFlagComponentC);
        engine.update();
        assertEqualsUnordered(
                Arrays.asList(newFlagComponentA, newFlagComponentC),
                entity.getComponents());
    }

    @Test
    public void testComponentIteratorRemove() {
        Engine engine = EngineConfig.create()
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .finish();
        Entity entity = engine.createEntity();
        FlagComponentA flagComponentA = new FlagComponentA();
        FlagComponentB flagComponentB = new FlagComponentB();
        FlagComponentC flagComponentC = new FlagComponentC();
        entity.add(flagComponentA);
        entity.add(flagComponentB);
        entity.add(flagComponentC);
        engine.update();
        assertEqualsUnordered(
                Arrays.asList(flagComponentA, flagComponentB, flagComponentC),
                entity.getComponents());
        Iterator<Component> iterator = entity.getComponents().iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == flagComponentB) {
                iterator.remove();
            }
        }
        assertEqualsUnordered(
                Arrays.asList(flagComponentA, flagComponentB, flagComponentC),
                entity.getComponents());
        engine.update();
        assertEqualsUnordered(
                Arrays.asList(flagComponentA, flagComponentC),
                entity.getComponents());
    }

    @Test(expected = IllegalStateException.class)
    public void testComponentIteratorIllegalStateException() {
        Engine engine = EngineConfig.create()
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .finish();
        Entity entity = engine.createEntity();
        FlagComponentA flagComponentA = new FlagComponentA();
        FlagComponentB flagComponentB = new FlagComponentB();
        FlagComponentC flagComponentC = new FlagComponentC();
        entity.add(flagComponentA);
        entity.add(flagComponentB);
        entity.add(flagComponentC);
        engine.update();
        entity.getComponents().iterator().remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void testComponentIteratorNoSuchElementException() {
        Engine engine = EngineConfig.create()
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .finish();
        Entity entity = engine.createEntity();
        FlagComponentA flagComponentA = new FlagComponentA();
        FlagComponentB flagComponentB = new FlagComponentB();
        FlagComponentC flagComponentC = new FlagComponentC();
        entity.add(flagComponentA);
        entity.add(flagComponentB);
        entity.add(flagComponentC);
        engine.update();
        Iterator<Component> iterator = entity.getComponents().iterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testEntityGuid() {
        Engine engine = EngineConfig.create().finish();
        Entity entity = engine.createEntity(5L);
        engine.update();
        assertEquals(5L, entity.getGuid());
        assertSame(entity, engine.getEntityForGuid(5L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEntityNoGuid() {
        Engine engine = EngineConfig.create().finish();
        Entity entity = engine.createEntity();
        engine.update();
        entity.getGuid();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEntityDelayGuid() {
        Engine engine = EngineConfig.create().finish();
        Entity entity = engine.createEntity(5L);
        entity.getGuid();
    }

    @Test
    public void testEntityIndex() {
        Engine engine = EngineConfig.create().finish();
        Entity entity = engine.createEntity();
        engine.update();
        assertEquals(0, entity.getIndex());
        assertSame(entity, engine.getEntityForIndex(0));
        Entity secondEntity = engine.createEntity();
        engine.update();
        assertEquals(1, secondEntity.getIndex());
        assertSame(secondEntity, engine.getEntityForIndex(1));
        entity.destroy();
        engine.update();
        Entity thirdEntity = engine.createEntity();
        engine.update();
        assertEquals(0, thirdEntity.getIndex());
    }
}
