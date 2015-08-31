package com.github.antag99.retinazer;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.badlogic.gdx.utils.IntArray;
import com.github.antag99.retinazer.utils.Mask;

public class EntitySetTest {
    public static final class TestEntitySetListener implements EntitySetListener {
        public Mask inserted = new Mask();
        public Mask removed = new Mask();
        public int insertedInvocations = 0;
        public int removedInvocations = 0;

        @Override
        public void inserted(IntArray entities) {
            insertedInvocations++;
            for (int i = 0, n = entities.size; i < n; i++)
                inserted.set(entities.get(i));
        }

        @Override
        public void removed(IntArray entities) {
            removedInvocations++;
            for (int i = 0, n = entities.size; i < n; i++)
                removed.set(entities.get(i));
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
        int entity0 = engine.createEntity().getEntity();
        int entity1 = engine.createEntity().add(new FlagComponentA()).getEntity();
        int entity2 = engine.createEntity().getEntity();
        int entity3 = engine.createEntity().getEntity();
        int entity4 = engine.createEntity().getEntity();
        engine.update();
        assertTrue(Arrays.equals(engine.getEntities().getIndices().toArray(),
                new int[] { entity0, entity1, entity2, entity3, entity4 }));
        assertTrue(Arrays.equals(engine.getEntitiesFor(
                Family.with(FlagComponentA.class)).getIndices().toArray(),
                new int[] { entity1 }));
    }

    @Test
    public void testAddListener() {
        TestEntitySetListener testListener = new TestEntitySetListener();
        EntitySet entitySet = new EntitySet();
        entitySet.addListener(testListener);

        int entity = 0;

        entitySet.addEntity(entity);

        testListener.verifyInserted(entity);
        testListener.verifyRemoved(new int[0]);
        testListener.verifyInsertedInvocations(1);
        testListener.verifyRemovedInvocations(0);

        entitySet.removeEntity(0);

        testListener.verifyInserted(new int[0]);
        testListener.verifyRemoved(entity);
        testListener.verifyInsertedInvocations(0);
        testListener.verifyRemovedInvocations(1);

        // Re-inserts the listener at index 0
        entitySet.addListener(testListener);
        entitySet.addEntity(0);

        testListener.verifyInserted(entity);
        testListener.verifyRemoved(new int[0]);
        testListener.verifyInsertedInvocations(1);
        testListener.verifyRemovedInvocations(0);

        entitySet.removeEntity(0);

        testListener.verifyInserted(new int[0]);
        testListener.verifyRemoved(entity);
        testListener.verifyInsertedInvocations(0);
        testListener.verifyRemovedInvocations(1);
    }

    @Test
    public void testRemoveListener() {
        TestEntitySetListener testListener = new TestEntitySetListener();
        TestEntitySetListener testListener2 = new TestEntitySetListener();
        EntitySet entitySet = new EntitySet();
        // Removing the listener when it does not exist has no effect
        entitySet.removeListener(testListener);
        entitySet.addListener(testListener);
        entitySet.addListener(testListener);
        entitySet.addListener(testListener);
        entitySet.addListener(testListener2);
        entitySet.removeListener(testListener);
        // One listener; duplicate listeners are ignored

        // Paranoia!
        testListener.verifyInserted(new int[0]);
        testListener.verifyRemoved(new int[0]);
        testListener.verifyInsertedInvocations(0);
        testListener.verifyRemovedInvocations(0);

        int entity = 0;

        entitySet.addEntity(entity);

        // Shouldn't be invoked
        testListener.verifyInserted(new int[0]);
        testListener.verifyRemoved(new int[0]);
        testListener.verifyInsertedInvocations(0);
        testListener.verifyRemovedInvocations(0);

        testListener2.verifyInserted(entity);
        testListener2.verifyRemoved(new int[0]);
        testListener2.verifyInsertedInvocations(1);
        testListener2.verifyRemovedInvocations(0);
    }

    @Test
    public void testZeroEntities() {
        // No listeners should be invoked with zero entities
        TestEntitySetListener testListener = new TestEntitySetListener();
        EntitySet entitySet = new EntitySet();
        entitySet.addListener(testListener);
        entitySet.addEntities(new IntArray());
        entitySet.addEntities(new Mask());
        entitySet.removeEntities(new IntArray());
        entitySet.removeEntities(new Mask());
        testListener.verifyInserted(new int[0]);
        testListener.verifyRemoved(new int[0]);
        testListener.verifyInsertedInvocations(0);
        testListener.verifyRemovedInvocations(0);
    }

    @Test
    public void testEquals() {
        EntitySet a = new EntitySet();
        EntitySet b = new EntitySet();
        assertEquals(a, b);
        a.addListener(new TestEntitySetListener());
        assertEquals(a, b);
        a.addEntity(0);
        assertNotEquals(a, b);
        a.addEntity(54);
        assertNotEquals(a, b);
        assertNotEquals(a, new Object());
    }

    @Test
    public void testHashcode() {
        EntitySet a = new EntitySet();
        EntitySet b = new EntitySet();
        assertEquals(a.hashCode(), b.hashCode());
        a.addListener(new TestEntitySetListener());
        assertEquals(a.hashCode(), b.hashCode());
        a.addEntity(0);
        assertNotEquals(a.hashCode(), b.hashCode());
        a.addEntity(54);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testToString() {
        EntitySet set = new EntitySet();
        assertEquals("[]", set.toString());
        set.addEntity(0);
        assertEquals("[0]", set.toString());
        set.addEntity(4);
        assertEquals("[0, 4]", set.toString());
        set.addEntity(2);
        assertEquals("[0, 2, 4]", set.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnmodifiable0() {
        new EntitySet().unmodifiable().addEntity(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnmodifiable1() {
        new EntitySet().unmodifiable().addEntities(new IntArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnmodifiable2() {
        new EntitySet().unmodifiable().addEntities(new Mask());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnmodifiable3() {
        new EntitySet().unmodifiable().removeEntity(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnmodifiable4() {
        new EntitySet().unmodifiable().removeEntities(new IntArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnmodifiable5() {
        new EntitySet().unmodifiable().removeEntities(new Mask());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnmodifiable6() {
        new EntitySet().unmodifiable().clear();
    }
}
