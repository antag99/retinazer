package com.github.antag99.retinazer;

import static org.junit.Assert.*;

import org.junit.Test;

public class FamilyTest {
    @Test
    public void testHashCode() {
        Engine engine = new Engine(new EngineConfig());
        assertEquals(0, engine.getFamily(Family.create()).hashCode());
        assertEquals(1, engine.getFamily(Family.with(FlagComponentA.class)).hashCode());
        assertEquals(2, engine.getFamily(Family.exclude(FlagComponentA.class)).hashCode());
    }

    @Test
    public void testEquals() {
        Engine engine = new Engine(new EngineConfig());
        assertEquals(engine.getFamily(Family.create()), engine.getFamily(Family.create()));
        assertEquals(engine.getFamily(Family.with(FlagComponentA.class)),
                engine.getFamily(Family.with(FlagComponentA.class)));
        assertEquals(engine.getFamily(Family.exclude(FlagComponentA.class)),
                engine.getFamily(Family.exclude(FlagComponentA.class)));
        assertNotEquals(engine.getFamily(Family.with(FlagComponentA.class)),
                engine.getFamily(Family.exclude(FlagComponentA.class)));
    }

    @Test
    public void testSame() {
        Engine engine = new Engine(new EngineConfig());
        assertSame(engine.getFamily(Family.create()), engine.getFamily(Family.create()));
        assertSame(engine.getFamily(Family.with(FlagComponentA.class)),
                engine.getFamily(Family.with(FlagComponentA.class)));
        assertSame(engine.getFamily(Family.exclude(FlagComponentA.class)),
                engine.getFamily(Family.exclude(FlagComponentA.class)));
        assertNotSame(engine.getFamily(Family.with(FlagComponentA.class)),
                engine.getFamily(Family.exclude(FlagComponentA.class)));
    }
}
