package com.github.antag99.retinazer;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.github.antag99.retinazer.util.Mask;

public class EntitySetTest {
    public static final class TestEntitySetListener implements EntityListener {
        public Mask inserted = new Mask();
        public Mask removed = new Mask();
        public int insertedInvocations = 0;
        public int removedInvocations = 0;

        @Override
        public void inserted(EntitySet entities) {
            insertedInvocations++;
            inserted.or(entities.getMask());
        }

        @Override
        public void removed(EntitySet entities) {
            removedInvocations++;
            removed.or(entities.getMask());
        }

        public void verifyInserted(int... entities) {
            Mask mask = new Mask();
            for (int e : entities)
                mask.set(e);
            assertEquals(mask, inserted);
            inserted.clear();
        }

        public void verifyRemoved(int... entities) {
            Mask mask = new Mask();
            for (int e : entities)
                mask.set(e);
            assertEquals(mask, removed);
            removed.clear();
        }

        public void verifyInsertedInvocations(int count) {
            assertEquals(count, insertedInvocations);
            insertedInvocations = 0;
        }

        public void verifyRemovedInvocations(int count) {
            assertEquals(count, removedInvocations);
            removedInvocations = 0;
        }
    }

    @Test
    public void testIndices() {
        Engine engine = new Engine(new EngineConfig());
        int entity0 = engine.createEntity().idx();
        int entity1 = engine.createEntity().add(new FlagComponentA()).idx();
        int entity2 = engine.createEntity().idx();
        int entity3 = engine.createEntity().idx();
        int entity4 = engine.createEntity().idx();
        engine.update();
        assertTrue(Arrays.equals(engine.getEntities().getIndices().toArray(),
                new int[] { entity0, entity1, entity2, entity3, entity4 }));
        assertTrue(Arrays.equals(engine.getFamily(
                Family.with(FlagComponentA.class)).getEntities().getIndices().toArray(),
                new int[] { entity1 }));
    }

    @Test
    public void testEquals() {
        EntitySet a = new EntitySet();
        EntitySet b = new EntitySet();
        assertEquals(a, b);
        assertEquals(a, b);
        a.edit().addEntity(0);
        assertNotEquals(a, b);
        a.edit().addEntity(54);
        assertNotEquals(a, b);
        assertNotEquals(a, new Object());
    }

    @Test
    public void testHashcode() {
        EntitySet a = new EntitySet();
        EntitySet b = new EntitySet();
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(a.hashCode(), b.hashCode());
        a.edit().addEntity(0);
        assertNotEquals(a.hashCode(), b.hashCode());
        a.edit().addEntity(54);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testToString() {
        EntitySet set = new EntitySet();
        assertEquals("[]", set.toString());
        set.edit().addEntity(0);
        assertEquals("[0]", set.toString());
        set.edit().addEntity(4);
        assertEquals("[0, 4]", set.toString());
        set.edit().addEntity(2);
        assertEquals("[0, 2, 4]", set.toString());
    }

    @Test(expected = RetinazerException.class)
    public void testUnmodifiable() {
        new EntitySet().view().edit().addEntity(0);
    }
}
