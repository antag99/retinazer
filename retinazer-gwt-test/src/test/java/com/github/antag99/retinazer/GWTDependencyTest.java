package com.github.antag99.retinazer;

public class GWTDependencyTest extends RetinazerTestCase {
    public static class Dummy {
    }

    public static class Dummy2 extends Dummy {
    }

    public static class Dummy3 extends Dummy2 {
    }

    public static class Dummy4 extends Dummy {
    }

    @Wire
    public static final class TestConsumerSystem extends EntitySystem {
        public Dummy dummy;
        public Dummy2 dummy2;
        public Dummy3 dummy3;
        public Dummy4 dummy4;
    }

    public void testExactDependency() {
        Dummy theDummy = new Dummy();
        Dummy2 theDummy2 = new Dummy2();
        Dummy3 theDummy3 = new Dummy3();
        Dummy4 theDummy4 = new Dummy4();

        TestConsumerSystem testConsumer = new TestConsumerSystem();
        Engine engine = new Engine(new EngineConfig().addWireResolver(
                new DependencyResolver(new DependencyConfig()
                        .addDependency(theDummy4)
                        .addDependency(theDummy3)
                        .addDependency(theDummy2)
                        .addDependency(theDummy)))
                .addSystem(testConsumer));
        assertSame(theDummy, testConsumer.dummy);
        assertSame(theDummy2, testConsumer.dummy2);
        assertSame(theDummy3, testConsumer.dummy3);
        assertSame(theDummy4, testConsumer.dummy4);
        engine.unwire(testConsumer);
        assertSame(null, testConsumer.dummy);
        assertSame(null, testConsumer.dummy2);
        assertSame(null, testConsumer.dummy3);
        assertSame(null, testConsumer.dummy4);
        // Paranoia!
        engine.wire(testConsumer);
        assertSame(theDummy, testConsumer.dummy);
        assertSame(theDummy2, testConsumer.dummy2);
        assertSame(theDummy3, testConsumer.dummy3);
        assertSame(theDummy4, testConsumer.dummy4);
        engine.unwire(testConsumer);
        assertSame(null, testConsumer.dummy);
        assertSame(null, testConsumer.dummy2);
        assertSame(null, testConsumer.dummy3);
        assertSame(null, testConsumer.dummy4);
    }

    public void testInexactDependency() {
        Dummy theDummy = new Dummy();
        Dummy3 theDummy3 = new Dummy3();
        Dummy4 theDummy4 = new Dummy4();

        TestConsumerSystem testConsumer = new TestConsumerSystem();
        Engine engine = new Engine(new EngineConfig()
                .addWireResolver(new DependencyResolver(new DependencyConfig()
                        .addDependency(Dummy4.class, theDummy4)
                        .addDependency(Dummy2.class, theDummy3)
                        .addDependency(Dummy.class, theDummy)))
                .addSystem(testConsumer));
        assertSame(theDummy, testConsumer.dummy);
        assertSame(theDummy3, testConsumer.dummy2);
        assertSame(null, testConsumer.dummy3);
        assertSame(theDummy4, testConsumer.dummy4);
        engine.unwire(testConsumer);
        assertSame(null, testConsumer.dummy);
        assertSame(null, testConsumer.dummy2);
        assertSame(null, testConsumer.dummy3);
        assertSame(null, testConsumer.dummy4);
        // Paranoia!
        engine.wire(testConsumer);
        assertSame(theDummy, testConsumer.dummy);
        assertSame(theDummy3, testConsumer.dummy2);
        assertSame(null, testConsumer.dummy3);
        assertSame(theDummy4, testConsumer.dummy4);
        engine.unwire(testConsumer);
        assertSame(null, testConsumer.dummy);
        assertSame(null, testConsumer.dummy2);
        assertSame(null, testConsumer.dummy3);
        assertSame(null, testConsumer.dummy4);
    }

    @SuppressWarnings("unchecked")
    public void testMismatchDependency() {
        try {
            new DependencyConfig().addDependency((Class<Dummy3>) (Class<?>) Dummy4.class, new Dummy3());
        } catch (ClassCastException ex) {
            return;
        }
        fail("expected ClassCastException");
    }
}
