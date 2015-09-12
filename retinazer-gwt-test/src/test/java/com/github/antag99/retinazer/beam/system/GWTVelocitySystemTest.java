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
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.RetinazerTestCase;
import com.github.antag99.retinazer.beam.component.Position;
import com.github.antag99.retinazer.beam.component.Velocity;

public final class GWTVelocitySystemTest extends RetinazerTestCase {
    public void testVelocity() {
        DeltaSystem deltaSystem = new DeltaSystem(Float.MAX_VALUE);
        VelocitySystem velocitySystem = new VelocitySystem();

        Engine engine = new Engine(new EngineConfig()
                .addSystem(deltaSystem)
                .addSystem(velocitySystem));

        Handle entity = engine.createEntity();
        Position position = entity.create(Position.class);
        Velocity velocity = entity.create(Velocity.class);
        velocity.xy(5f, 0f);

        deltaSystem.setDeltaTime(1f);
        engine.update();

        assertEquals(5f, position.x, 0f);
        assertEquals(0f, position.y, 0f);

        deltaSystem.setDeltaTime(0.5f);
        engine.update();

        assertEquals(7.5f, position.x, 0f);
        assertEquals(0f, position.y, 0f);
    }
}
