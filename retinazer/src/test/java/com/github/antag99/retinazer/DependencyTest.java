package com.github.antag99.retinazer;

import static org.junit.Assert.*;

import org.junit.Test;

public class DependencyTest {

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

    @Test
    public void testExactDependency() {
        Dummy theDummy = new Dummy();
        Dummy2 theDummy2 = new Dummy2();
        Dummy3 theDummy3 = new Dummy3();
        Dummy4 theDummy4 = new Dummy4();

        TestConsumerSystem testConsumer = new TestConsumerSystem();
        Engine engine = EngineConfig.create()
                .withDependency(theDummy4)
                .withDependency(theDummy3)
                .withDependency(theDummy2)
                .withDependency(theDummy)
                .withSystem(testConsumer).finish();
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

    @Test
    public void testInexactDependency() {
        Dummy theDummy = new Dummy();
        Dummy3 theDummy3 = new Dummy3();
        Dummy4 theDummy4 = new Dummy4();

        TestConsumerSystem testConsumer = new TestConsumerSystem();
        Engine engine = EngineConfig.create()
                .withDependency(Dummy4.class, theDummy4)
                .withDependency(Dummy2.class, theDummy3)
                .withDependency(Dummy.class, theDummy)
                .withSystem(testConsumer).finish();
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
    @Test(expected = ClassCastException.class)
    public void testMismatchDependency() {
        EngineConfig.create().withDependency((Class<Dummy3>) (Class<?>) Dummy4.class, new Dummy3());
    }
}
