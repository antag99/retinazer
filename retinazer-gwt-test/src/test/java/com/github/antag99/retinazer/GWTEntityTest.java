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

import static com.github.antag99.retinazer.GWTTestUtils.assertEqualsUnordered;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.gwt.junit.client.GWTTestCase;

public class GWTEntityTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "com.github.antag99.RetinazerTest";
    }

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
        try {
            entity.getComponents().iterator().remove();
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
        }
    }

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
        try {
            iterator.next();
            fail("NoSuchElementException Expected");
        } catch (NoSuchElementException expected) {
        }
    }

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
