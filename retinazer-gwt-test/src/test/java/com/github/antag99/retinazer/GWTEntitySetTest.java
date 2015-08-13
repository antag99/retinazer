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

import java.util.Arrays;

import com.google.gwt.junit.client.GWTTestCase;

public class GWTEntitySetTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "com.github.antag99.RetinazerTest";
    }

    public void testEntities() {
        Engine engine = EngineConfig.create()
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .finish();
        Entity entity0 = engine.createEntity();
        Entity entity1 = engine.createEntity();
        entity1.add(new FlagComponentA());
        Entity entity2 = engine.createEntity();
        Entity entity3 = engine.createEntity();
        Entity entity4 = engine.createEntity();
        engine.update();
        assertTrue(Arrays.equals(engine.getEntities().getEntities(),
                new Entity[] { entity0, entity1, entity2, entity3, entity4 }));
        assertTrue(Arrays.equals(engine.getEntitiesFor(
                Family.with(FlagComponentA.class)).getEntities(),
                new Entity[] { entity1 }));
    }

    public void testIndices() {
        Engine engine = EngineConfig.create()
                .withComponentType(FlagComponentA.class)
                .withComponentType(FlagComponentB.class)
                .withComponentType(FlagComponentC.class)
                .finish();
        Entity entity0 = engine.createEntity();
        Entity entity1 = engine.createEntity();
        entity1.add(new FlagComponentA());
        Entity entity2 = engine.createEntity();
        Entity entity3 = engine.createEntity();
        Entity entity4 = engine.createEntity();
        engine.update();
        assertTrue(Arrays.equals(engine.getEntities().getIndices(), new int[] {
                entity0.getIndex(), entity1.getIndex(), entity2.getIndex(),
                entity3.getIndex(), entity4.getIndex() }));
        assertTrue(Arrays.equals(engine.getEntitiesFor(
                Family.with(FlagComponentA.class)).getIndices(), new int[] {
                        entity1.getIndex() }));
    }
}
