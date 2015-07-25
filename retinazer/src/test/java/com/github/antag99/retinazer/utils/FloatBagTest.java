package com.github.antag99.retinazer.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class FloatBagTest {

    /**
     * Ensures that the elements of a bag are actually stored
     */
    @Test
    public void testStorage() {
        FloatBag bag = new FloatBag();

        bag.set(0, 0f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(0f, bag.get(1), 0f);
        assertEquals(0f, bag.get(2), 0f);
        assertEquals(0f, bag.get(3), 0f);
        assertEquals(0f, bag.get(4), 0f);
        assertEquals(0f, bag.get(5), 0f);
        assertEquals(0f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);

        bag.set(1, 1f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(1f, bag.get(1), 0f);
        assertEquals(0f, bag.get(2), 0f);
        assertEquals(0f, bag.get(3), 0f);
        assertEquals(0f, bag.get(4), 0f);
        assertEquals(0f, bag.get(5), 0f);
        assertEquals(0f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);

        bag.set(2, 2f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(1f, bag.get(1), 0f);
        assertEquals(2f, bag.get(2), 0f);
        assertEquals(0f, bag.get(3), 0f);
        assertEquals(0f, bag.get(4), 0f);
        assertEquals(0f, bag.get(5), 0f);
        assertEquals(0f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);

        bag.set(3, 3f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(1f, bag.get(1), 0f);
        assertEquals(2f, bag.get(2), 0f);
        assertEquals(3f, bag.get(3), 0f);
        assertEquals(0f, bag.get(4), 0f);
        assertEquals(0f, bag.get(5), 0f);
        assertEquals(0f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);

        bag.set(4, 4f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(1f, bag.get(1), 0f);
        assertEquals(2f, bag.get(2), 0f);
        assertEquals(3f, bag.get(3), 0f);
        assertEquals(4f, bag.get(4), 0f);
        assertEquals(0f, bag.get(5), 0f);
        assertEquals(0f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);

        bag.set(5, 5f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(1f, bag.get(1), 0f);
        assertEquals(2f, bag.get(2), 0f);
        assertEquals(3f, bag.get(3), 0f);
        assertEquals(4f, bag.get(4), 0f);
        assertEquals(5f, bag.get(5), 0f);
        assertEquals(0f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);

        bag.set(6, 6f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(1f, bag.get(1), 0f);
        assertEquals(2f, bag.get(2), 0f);
        assertEquals(3f, bag.get(3), 0f);
        assertEquals(4f, bag.get(4), 0f);
        assertEquals(5f, bag.get(5), 0f);
        assertEquals(6f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);

        bag.set(7, 7f);
        assertEquals(0f, bag.get(0), 0f);
        assertEquals(1f, bag.get(1), 0f);
        assertEquals(2f, bag.get(2), 0f);
        assertEquals(3f, bag.get(3), 0f);
        assertEquals(4f, bag.get(4), 0f);
        assertEquals(5f, bag.get(5), 0f);
        assertEquals(6f, bag.get(6), 0f);
        assertEquals(7f, bag.get(7), 0f);

        bag.clear();

        assertEquals(0f, bag.get(0), 0f);
        assertEquals(0f, bag.get(1), 0f);
        assertEquals(0f, bag.get(2), 0f);
        assertEquals(0f, bag.get(3), 0f);
        assertEquals(0f, bag.get(4), 0f);
        assertEquals(0f, bag.get(5), 0f);
        assertEquals(0f, bag.get(6), 0f);
        assertEquals(0f, bag.get(7), 0f);
    }

    /**
     * Ensures that the bag contains the default value by default
     */
    @Test
    public void testDefault() {
        FloatBag bag = new FloatBag();
        assertEquals(0f, bag.get(0), 0f);
        bag.set(0, 1f);
        assertEquals(0f, bag.get(1), 0f);
        assertEquals(0f, bag.get(2), 0f);
        assertEquals(0f, bag.get(3), 0f);
    }

    /**
     * Ensures that the bag resizes correctly when out of capacity, that it
     * does not resize when queried for non-existing elements, and that it does
     * not resize when the default value is set.
     */
    @Test
    public void testCapacity() {
        FloatBag bag;

        bag = new FloatBag();
        assertEquals(0, bag.getCapacity());
        bag.set(0, 1f);
        assertEquals(1, bag.getCapacity());
        bag.set(1, 2f);
        assertEquals(2, bag.getCapacity());
        bag.set(2, 3f);
        assertEquals(4, bag.getCapacity());
        bag.set(3, 4f);
        assertEquals(4, bag.getCapacity());
        bag.set(4, 5f);
        assertEquals(8, bag.getCapacity());
        bag.set(8, 6f);
        assertEquals(16, bag.getCapacity());
        bag.set(35, 7f);
        assertEquals(64, bag.getCapacity());

        bag = new FloatBag();
        for (int i = 0; i < 32; i++) {
            bag.get((1 << i) - 1);
            assertEquals(0, bag.getCapacity());
        }
        bag.get(Integer.MAX_VALUE);
        assertEquals(0, bag.getCapacity());

        bag = new FloatBag();
        for (int i = 0; i < 31; i++) {
            bag.set((1 << i) - 1, 0f);
            assertEquals(0, bag.getCapacity());
        }
        bag.set(Integer.MAX_VALUE, 0f);
        assertEquals(0, bag.getCapacity());
    }

    /**
     * When a negative index is used, an {@link ArrayIndexOutOfBoundsException} should be thrown.
     */
    @Test
    public void testArrayIndexOutOfBoundsException() {
        FloatBag bag = new FloatBag();
        for (int i = 0; i < 32; i++) {
            try {
                bag.set(-(1 << i), 0f);
            } catch (ArrayIndexOutOfBoundsException ex) {
                continue;
            }

            fail("ArrayIndexOutOfBoundsException expected for index " + (-(1 << i)));
        }

        for (int i = 0; i < 32; i++) {
            try {
                bag.get(-(1 << i));
            } catch (ArrayIndexOutOfBoundsException ex) {
                continue;
            }

            fail("ArrayIndexOutOfBoundsException expected for index " + (-(1 << i)));
        }
    }
}
