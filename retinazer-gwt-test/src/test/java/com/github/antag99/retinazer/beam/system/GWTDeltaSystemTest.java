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
package com.github.antag99.retinazer.beam.system;

import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;
import com.github.antag99.retinazer.RetinazerTestCase;

public final class GWTDeltaSystemTest extends RetinazerTestCase {
    public void testDefault() {
        assertEquals(1f / 20f, new DeltaSystem().getMaxDeltaTime(), 0f);
    }

    public void testDelta() {
        DeltaSystem deltaSystem = new DeltaSystem(2.5f);

        Engine engine = new Engine(new EngineConfig()
                .addSystem(deltaSystem));

        engine.update();

        deltaSystem.setDeltaTime(1.5f);
        assertEquals(1.5f, deltaSystem.getDeltaTime(), 0f);
        assertEquals(2.5f, deltaSystem.getMaxDeltaTime(), 0f);

        deltaSystem.setDeltaTime(3f);
        assertEquals(2.5f, deltaSystem.getDeltaTime(), 0f);
        assertEquals(2.5f, deltaSystem.getMaxDeltaTime(), 0f);
    }
}
