package com.github.antag99.retinazer;

import java.util.Arrays;

import com.github.antag99.retinazer.util.Mask;

public class GWTEntitySetTest extends RetinazerTestCase {
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

    public void testIndices() {
        Engine engine = new Engine(new EngineConfig());
        int entity0 = engine.createEntity();
        int entity1 = engine.createHandle(engine.createEntity())
                .add(new FlagComponentA()).get();
        int entity2 = engine.createEntity();
        int entity3 = engine.createEntity();
        int entity4 = engine.createEntity();
        engine.update();
        assertTrue(Arrays.equals(engine.getEntities().getIndices().toArray(),
                new int[] { entity0, entity1, entity2, entity3, entity4 }));
        assertTrue(Arrays.equals(engine.getFamily(
                Family.with(FlagComponentA.class)).getEntities().getIndices().toArray(),
                new int[] { entity1 }));
    }

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

    public void testUnmodifiable() {
        try {
            new EntitySet().view().edit();
        } catch (RetinazerException ex) {
            return;
        }
        fail("RetinazerException expected");
    }
}
