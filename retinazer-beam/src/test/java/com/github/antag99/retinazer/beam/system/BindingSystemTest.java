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
import static com.github.antag99.retinazer.beam.command.Commands.delay;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.beam.Controls;
import com.github.antag99.retinazer.beam.command.DelayCommand;
import com.github.antag99.retinazer.beam.component.Binding;
import com.github.antag99.retinazer.beam.component.Input;

public final class BindingSystemTest {
    Engine engine;
    DeltaSystem deltaSystem;
    BindingSystem bindingSystem;

    @Before
    public void initialize() {
        engine = new Engine(new EngineConfig()
                .addSystem(deltaSystem = new DeltaSystem(Float.MAX_VALUE))
                .addSystem(bindingSystem = new BindingSystem()));
    }

    @Test
    public void testBinding() {
        Handle entity = engine.createEntity();
        Input input = entity.create(Input.class);
        Binding binding = entity.create(Binding.class);
        DelayCommand moveLeftCommand = delay(1f);
        DelayCommand moveRightCommand = delay(1f);
        DelayCommand jumpCommand = delay(1f);
        binding.commands.put(Controls.MOVE_LEFT, moveLeftCommand);
        binding.commands.put(Controls.MOVE_RIGHT, moveRightCommand);
        binding.commands.put(Controls.JUMP, jumpCommand);
        input.controls.add(Controls.MOVE_LEFT);
        deltaSystem.setDeltaTime(0.2f);
        engine.update();
        engine.update();
        engine.update();
        engine.update();
        assertEquals(0.3f, moveLeftCommand.apply(entity, 0.5f), FLOAT_ROUNDING_ERROR);
        engine.update();
        engine.update();
        engine.update();
        engine.update();
        assertEquals(0.1f, moveLeftCommand.apply(entity, 0.5f), FLOAT_ROUNDING_ERROR);
        input.controls.remove(Controls.MOVE_LEFT);
        input.controls.add(Controls.MOVE_RIGHT);
    }
}
