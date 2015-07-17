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

import static com.github.antag99.retinazer.TestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.InOrder;

import com.github.antag99.retinazer.utils.Inject;

public class RetinazerTest {
    @Test
    public void testEngine() {
        Engine engine = EngineConfig.create().finish();
        engine.update();
        engine.update();
        engine.update();
        engine.update();
        engine.reset();
        engine.update();
        engine.reset();
        engine.reset();
        engine.update();
        engine.update();
        engine.update();
    }

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

    @Test
    public void testEntityListener() {
        EntityListener listener = mock(EntityListener.class);
        Engine engine = EngineConfig.create().finish();
        engine.addEntityListener(listener);
        Entity entity = engine.createEntity();
        verifyNoMoreInteractions(listener);
        engine.update();
        verify(listener).entityAdded(entity);
        verifyNoMoreInteractions(listener);
        entity.destroy();
        verifyNoMoreInteractions(listener);
        engine.update();
        verify(listener).entityRemoved(entity);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testFamilyListener() {
        EntityListener listenerA = mock(EntityListener.class);
        EntityListener listenerB = mock(EntityListener.class);
        InOrder order = inOrder(listenerA, listenerB);
        Engine engine = EngineConfig.create()
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .finish();
        engine.addEntityListener(Family.with(FlagComponentA.class), listenerA);
        engine.addEntityListener(Family.with(FlagComponentB.class), listenerB);
        Entity entity = engine.createEntity();
        engine.update();
        order.verifyNoMoreInteractions();
        entity.add(new FlagComponentA());
        order.verifyNoMoreInteractions();
        engine.update();
        order.verify(listenerA).entityAdded(entity);
        order.verifyNoMoreInteractions();
        entity.remove(FlagComponentA.class);
        engine.update();
        order.verify(listenerA).entityRemoved(entity);
        order.verifyNoMoreInteractions();
        entity.add(new FlagComponentB());
        engine.update();
        order.verify(listenerB).entityAdded(entity);
        order.verifyNoMoreInteractions();
        entity.destroy();
        engine.update();
        order.verify(listenerB).entityRemoved(entity);
        order.verifyNoMoreInteractions();
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

        engine.update();

        assertEqualsUnordered(
                Arrays.asList(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                engine.getEntities());

        assertEqualsUnordered(
                Arrays.asList(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                engine.getEntitiesFor(Family.with()));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                engine.getEntitiesFor(Family.exclude()));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity1, entity2, entity3, entity4, entity5, entity6, entity7),
                engine.getEntitiesFor(Family.with().exclude()));

        assertEqualsUnordered(
                Arrays.asList(entity1, entity4, entity6, entity7),
                engine.getEntitiesFor(Family.with(FlagComponentA.class)));

        assertEqualsUnordered(
                engine.getEntitiesFor(Family.with(FlagComponentB.class)),
                Arrays.asList(entity2, entity4, entity5, entity7));

        assertEqualsUnordered(
                Arrays.asList(entity3, entity5, entity6, entity7),
                engine.getEntitiesFor(Family.with(FlagComponentC.class)));

        assertEqualsUnordered(
                Arrays.asList(entity4, entity7),
                engine.getEntitiesFor(Family.with(FlagComponentA.class, FlagComponentB.class)));

        assertEqualsUnordered(
                Arrays.asList(entity6, entity7),
                engine.getEntitiesFor(Family.with(FlagComponentA.class, FlagComponentC.class)));

        assertEqualsUnordered(
                Arrays.asList(entity5, entity7),
                engine.getEntitiesFor(Family.with(FlagComponentB.class, FlagComponentC.class)));

        assertEqualsUnordered(
                Arrays.asList(entity7),
                engine.getEntitiesFor(Family.with(FlagComponentA.class, FlagComponentB.class, FlagComponentC.class)));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity2, entity3, entity5),
                engine.getEntitiesFor(Family.exclude(FlagComponentA.class)));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity1, entity3, entity6),
                engine.getEntitiesFor(Family.exclude(FlagComponentB.class)));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity1, entity2, entity4),
                engine.getEntitiesFor(Family.exclude(FlagComponentC.class)));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity3),
                engine.getEntitiesFor(Family.exclude(FlagComponentA.class, FlagComponentB.class)));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity2),
                engine.getEntitiesFor(Family.exclude(FlagComponentA.class, FlagComponentC.class)));

        assertEqualsUnordered(
                Arrays.asList(entity0, entity1),
                engine.getEntitiesFor(Family.exclude(FlagComponentB.class, FlagComponentC.class)));

        assertEqualsUnordered(
                Arrays.asList(entity0),
                engine.getEntitiesFor(
                        Family.exclude(FlagComponentA.class, FlagComponentB.class, FlagComponentC.class)));

        assertEqualsUnordered(
                Arrays.asList(entity1, entity6),
                engine.getEntitiesFor(Family.with(FlagComponentA.class).exclude(FlagComponentB.class)));

        assertEqualsUnordered(
                Arrays.asList(entity1, entity4),
                engine.getEntitiesFor(Family.with(FlagComponentA.class).exclude(FlagComponentC.class)));

        assertEqualsUnordered(
                Arrays.asList(entity2, entity5),
                engine.getEntitiesFor(Family.with(FlagComponentB.class).exclude(FlagComponentA.class)));

        assertEqualsUnordered(
                Arrays.asList(entity2, entity4),
                engine.getEntitiesFor(Family.with(FlagComponentB.class).exclude(FlagComponentC.class)));

        assertEqualsUnordered(
                Arrays.asList(entity3, entity5),
                engine.getEntitiesFor(Family.with(FlagComponentC.class).exclude(FlagComponentA.class)));

        assertEqualsUnordered(
                Arrays.asList(entity3, entity6),
                engine.getEntitiesFor(Family.with(FlagComponentC.class).exclude(FlagComponentB.class)));

        assertEqualsUnordered(
                Arrays.asList(entity1),
                engine.getEntitiesFor(
                        Family.with(FlagComponentA.class).exclude(FlagComponentB.class, FlagComponentC.class)));

        assertEqualsUnordered(
                Arrays.asList(entity2),
                engine.getEntitiesFor(
                        Family.with(FlagComponentB.class).exclude(FlagComponentA.class, FlagComponentC.class)));

        assertEqualsUnordered(
                Arrays.asList(entity3),
                engine.getEntitiesFor(
                        Family.with(FlagComponentC.class).exclude(FlagComponentA.class, FlagComponentB.class)));
    }

    private static class SimpleService {
    }

    private static class SimpleServiceConsumer {
        public @Inject SimpleService service;
    }

    @Test
    public void testDependencyInjection() {
        SimpleService service = new SimpleService();
        SimpleServiceConsumer consumer = new SimpleServiceConsumer();
        Engine engine = EngineConfig.create().withDependency(service).finish();
        engine.injectDependencies(consumer);
        assertSame(service, consumer.service);
        engine.uninjectDependencies(consumer);
        assertSame(null, consumer.service);
        engine.uninjectDependencies(consumer);
        assertSame(null, consumer.service);
    }

    private static class AbstractService {
    }

    private static class AbstractServiceImpl extends AbstractService {
    }

    private static class AbstractServiceConsumer {
        private @Inject AbstractService service;
    }

    @Test
    public void testAbstractDependencyInjection() {
        AbstractService service = new AbstractServiceImpl();
        AbstractServiceConsumer consumer = new AbstractServiceConsumer();
        Engine engine = EngineConfig.create().withDependency(service).finish();
        engine.injectDependencies(consumer);
        assertSame(service, consumer.service);
        engine.uninjectDependencies(consumer);
        assertSame(null, consumer.service);
        engine.uninjectDependencies(consumer);
        assertSame(null, consumer.service);
    }

    private interface InterfaceService {
    }

    private static class InterfaceServiceImpl implements InterfaceService {
    }

    private static class InterfaceServiceConsumer {
        private @Inject InterfaceService service;
    }

    @Test
    public void testInterfaceDependencyInjection() {
        InterfaceService service = new InterfaceServiceImpl();
        InterfaceServiceConsumer consumer = new InterfaceServiceConsumer();
        Engine engine = EngineConfig.create().withDependency(service).finish();
        engine.injectDependencies(consumer);
        assertSame(service, consumer.service);
        engine.uninjectDependencies(consumer);
        assertSame(null, consumer.service);
        engine.uninjectDependencies(consumer);
        assertSame(null, consumer.service);
    }

    private static class SimpleServiceSubclassConsumer extends SimpleServiceConsumer {
    }

    @Test
    public void testSuperclassDependencyInjection() {
        SimpleService service = new SimpleService();
        SimpleServiceSubclassConsumer consumer = new SimpleServiceSubclassConsumer();
        Engine engine = EngineConfig.create().withDependency(service).finish();
        engine.injectDependencies(consumer);
        assertSame(service, consumer.service);
        engine.uninjectDependencies(consumer);
        assertSame(null, consumer.service);
        engine.uninjectDependencies(consumer);
        assertSame(null, consumer.service);
    }

    private static class MissingService {
    }

    private static class MissingServiceConsumer {
        private @Inject MissingService service;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingDependencyInjection() {
        MissingServiceConsumer consumer = new MissingServiceConsumer();
        EngineConfig.create().finish().injectDependencies(consumer);
    }

    private static class ExampleSystem extends EntitySystem {
        public @Inject Engine engine;
        public @Inject FlagSystemA flagSystemA;
        public @Inject FlagSystemB flagSystemB;
        public @Inject FlagSystemC flagSystemC;
        public @Inject ComponentMapper<FlagComponentA> flagMapperA;
        public @Inject ComponentMapper<FlagComponentB> flagMapperB;
        public @Inject ComponentMapper<FlagComponentC> flagMapperC;
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
        assertSame(engine.getMapper(FlagComponentA.class), system.flagMapperA);
        assertSame(engine.getMapper(FlagComponentB.class), system.flagMapperB);
        assertSame(engine.getMapper(FlagComponentC.class), system.flagMapperC);
        assertSame(flagSystemA, system.flagSystemA);
        assertSame(flagSystemB, system.flagSystemB);
        assertSame(flagSystemC, system.flagSystemC);
        engine.reset();
        assertSame(engine, system.engine);
        assertSame(engine.getMapper(FlagComponentA.class), system.flagMapperA);
        assertSame(engine.getMapper(FlagComponentB.class), system.flagMapperB);
        assertSame(engine.getMapper(FlagComponentC.class), system.flagMapperC);
        assertSame(flagSystemA, system.flagSystemA);
        assertSame(flagSystemB, system.flagSystemB);
        assertSame(flagSystemC, system.flagSystemC);
    }

    private static class MissingSystem extends EntitySystem {
    }

    private static class MissingSystemConsumer extends EntitySystem {
        public @Inject MissingSystem system;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingEngineDependencyInjection() {
        EngineConfig.create().withSystem(new MissingSystemConsumer()).finish();
    }

    @Test
    public void testEntitySystem() {
        EntitySystem system = mock(EntitySystem.class);
        Engine engine = EngineConfig.create().withSystem(system).finish();
        InOrder order = inOrder(system);
        order.verify(system).initialize();
        engine.update();
        order.verify(system).beforeUpdate();
        order.verify(system).update();
        order.verify(system).afterUpdate();
        order.verifyNoMoreInteractions();
        engine.reset();
        order.verify(system).destroy();
        order.verify(system).initialize();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testEventListener() {
        EventListener<Event> firstListener = mock(EventListener.class);
        EventListener<Event> secondListener = mock(EventListener.class);
        EventListener<Event> thirdListener = mock(EventListener.class);
        InOrder order = inOrder(firstListener, secondListener, thirdListener);
        Engine engine = EngineConfig.create()
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .withEventType(EventA.class)
                .withEventType(EventB.class)
                .withEventType(EventC.class)
                .finish();
        EventA event = new EventA();
        engine.addEventListener(EventA.class, Family.with(FlagComponentA.class), 0, firstListener);
        engine.addEventListener(EventA.class, Family.with(FlagComponentA.class), 3, secondListener);
        engine.addEventListener(EventA.class, Family.EMPTY, 7, thirdListener);
        Entity entity = engine.createEntity();
        engine.update();
        entity.dispatch(event);
        order.verify(thirdListener).handleEvent(event, entity);
        order.verifyNoMoreInteractions();
        entity.add(new FlagComponentA());
        engine.update();
        entity.dispatch(event);
        order.verify(firstListener).handleEvent(event, entity);
        order.verify(secondListener).handleEvent(event, entity);
        order.verify(thirdListener).handleEvent(event, entity);
        order.verifyNoMoreInteractions();
        engine.removeEventListener(secondListener);
        entity.dispatch(event);
        order.verify(firstListener).handleEvent(event, entity);
        order.verify(thirdListener).handleEvent(event, entity);
        order.verifyNoMoreInteractions();
        engine.removeEventListener(firstListener);
        entity.dispatch(event);
        order.verify(thirdListener).handleEvent(event, entity);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void testEventHandler() {
        EventABCSystem system = mock(EventABCSystem.class);
        InOrder order = inOrder(system);
        Engine engine = EngineConfig.create()
                .withEventType(EventA.class)
                .withEventType(EventB.class)
                .withEventType(EventC.class)
                .withSystem(system).finish();
        Entity entity = engine.createEntity();
        order.verify(system).initialize();
        order.verifyNoMoreInteractions();
        EventA eventA = new EventA();
        EventB eventB = new EventB();
        EventC eventC = new EventC();
        entity.dispatch(eventA);
        order.verify(system).beforeHandleEvent(eventA, entity);
        order.verify(system).handleEventA(eventA, entity);
        order.verify(system).handleEvent(eventA, entity);
        order.verify(system).afterHandleEventA(eventA, entity);
        order.verify(system).afterHandleEvent(eventA, entity);
        order.verifyNoMoreInteractions();
        entity.dispatch(eventB);
        entity.dispatch(eventC);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingEventHandler() {
        EntitySystem invalidSystem = new EntitySystem() {
            @EventHandler
            public void missingHandler(EventA event, Entity entity) {
            }
        };
        EngineConfig.create().withSystem(invalidSystem).finish();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEventHandler0() {
        EntitySystem invalidSystem = new EntitySystem() {
            @EventHandler
            public void invalidHandler() {
            }
        };
        EngineConfig.create().withSystem(invalidSystem).finish();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEventHandler1() {
        EntitySystem invalidSystem = new EntitySystem() {
            @EventHandler
            public void invalidHandler(Event event) {
            }
        };
        EngineConfig.create().withSystem(invalidSystem).finish();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEventHandler2() {
        EntitySystem invalidSystem = new EntitySystem() {
            @EventHandler
            public void invalidHandler(Entity entity) {
            }
        };
        EngineConfig.create().withSystem(invalidSystem).finish();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEventHandler3() {
        EntitySystem invalidSystem = new EntitySystem() {
            @EventHandler
            public void invalidHandler(Object object) {
            }
        };
        EngineConfig.create().withSystem(invalidSystem).finish();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEventHandler4() {
        EntitySystem invalidSystem = new EntitySystem() {
            @EventHandler
            public void invalidHandler(Object object, Entity entity) {
            }
        };
        EngineConfig.create().withSystem(invalidSystem).finish();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEventHandler5() {
        EntitySystem invalidSystem = new EntitySystem() {
            @EventHandler
            public void invalidHandler(Event event, Object entity) {
            }
        };
        EngineConfig.create().withSystem(invalidSystem).finish();
    }
}
