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

import static com.github.antag99.retinazer.beam.command.Commands.delay;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.beam.command.DelayCommand;
import com.github.antag99.retinazer.beam.component.Script;

public final class ScriptSystemTest {
    Engine engine;
    ScriptSystem scriptSystem;
    DeltaSystem deltaSystem;

    @Before
    public void initialize() {
        engine = new Engine(new EngineConfig()
                .addSystem(scriptSystem = new ScriptSystem())
                .addSystem(deltaSystem = new DeltaSystem(Float.MAX_VALUE)));
    }

    @Test
    public void testScript() {
        Handle entity = engine.createEntity();
        Script script = entity.create(Script.class);
        DelayCommand delay = delay(1f);
        script.command(delay);
        deltaSystem.setDeltaTime(0.75f);
        engine.update();
        deltaSystem.setDeltaTime(0.75f);
        engine.update();
        assertEquals(1f, delay.apply(entity, 1f), 0f);
    }
}
