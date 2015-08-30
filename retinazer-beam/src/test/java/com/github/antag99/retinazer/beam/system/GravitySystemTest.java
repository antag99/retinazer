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

import static com.badlogic.gdx.math.MathUtils.FLOAT_ROUNDING_ERROR;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.beam.component.Velocity;
import com.github.antag99.retinazer.beam.component.Weight;

@RunWith(Parameterized.class)
public final class GravitySystemTest {

    @Parameters
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][] {
                { 0f, -50f, 0f, -100f },
                { -50f, 0f, -100f, 0f },
                { 0f, 50f, 0f, 100f },
                { 50f, 0f, 100f, 0f },
                { 0f, -50f, 0f, -101f },
                { -50f, 0f, -101f, 0f },
                { 0f, 50f, 0f, 101f },
                { 50f, 0f, 101f, 0f },
        });
    }

    @Parameter(value = 0)
    public float gravityX;
    @Parameter(value = 1)
    public float gravityY;
    @Parameter(value = 2)
    public float maxGravityX;
    @Parameter(value = 3)
    public float maxGravityY;

    Engine engine;
    DeltaSystem deltaSystem;
    GravitySystem gravitySystem;

    @Before
    public void initialize() {
        engine = new Engine(new EngineConfig()
                .addSystem(deltaSystem = new DeltaSystem(Float.MAX_VALUE))
                .addSystem(gravitySystem = new GravitySystem(gravityX, gravityY,
                        maxGravityX, maxGravityY)));
    }

    @Test
    public void testGetters() {
        assertEquals(gravityX, gravitySystem.getGravityX(), 0f);
        assertEquals(gravityY, gravitySystem.getGravityY(), 0f);
        assertEquals(maxGravityX, gravitySystem.getMaxGravityX(), 0f);
        assertEquals(maxGravityY, gravitySystem.getMaxGravityY(), 0f);
    }

    @Test
    public void testGravity() {
        Handle entity = engine.createEntity();
        Velocity velocity = entity.create(Velocity.class);
        Weight weight = entity.create(Weight.class);
        weight.gravityScaleX = 1f;
        weight.gravityScaleY = 1f;

        deltaSystem.setDeltaTime(1f);

        engine.update();
        assertEquals(gravityX, velocity.x, FLOAT_ROUNDING_ERROR);
        assertEquals(gravityY, velocity.y, FLOAT_ROUNDING_ERROR);

        engine.update();
        assertEquals(gravityX + gravityX, velocity.x, FLOAT_ROUNDING_ERROR);
        assertEquals(gravityY + gravityY, velocity.y, FLOAT_ROUNDING_ERROR);

        engine.update();
        assertEquals(maxGravityX, velocity.x, FLOAT_ROUNDING_ERROR);
        assertEquals(maxGravityY, velocity.y, FLOAT_ROUNDING_ERROR);

        velocity.x = maxGravityX * 2f;
        velocity.y = maxGravityY * 2f;

        engine.update();
        assertEquals(maxGravityX * 2f, velocity.x, FLOAT_ROUNDING_ERROR);
        assertEquals(maxGravityY * 2f, velocity.y, FLOAT_ROUNDING_ERROR);
    }
}
