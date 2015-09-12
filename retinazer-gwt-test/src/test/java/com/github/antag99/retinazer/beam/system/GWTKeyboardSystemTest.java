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

import com.badlogic.gdx.Input.Keys;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.RetinazerTestCase;
import com.github.antag99.retinazer.beam.Controls;
import com.github.antag99.retinazer.beam.component.Input;
import com.github.antag99.retinazer.beam.component.Keyboard;

public class GWTKeyboardSystemTest extends RetinazerTestCase {
    public void testKeyboard() {
        KeyboardSystem keyboardSystem = new KeyboardSystem();

        Engine engine = new Engine(new EngineConfig()
                .addSystem(keyboardSystem));
        Handle entity = engine.createEntity();

        Keyboard keyboard = entity.create(Keyboard.class);
        keyboard.controls.put(Keys.A, Controls.MOVE_LEFT);
        keyboard.controls.put(Keys.D, Controls.MOVE_RIGHT);
        keyboard.controls.put(Keys.SPACE, Controls.JUMP);

        Input input = entity.create(Input.class);
        assertFalse(input.controls.contains(Controls.MOVE_LEFT));
        assertFalse(input.controls.contains(Controls.MOVE_RIGHT));
        assertFalse(input.controls.contains(Controls.JUMP));

        engine.update();
        assertFalse(input.controls.contains(Controls.MOVE_LEFT));
        assertFalse(input.controls.contains(Controls.MOVE_RIGHT));
        assertFalse(input.controls.contains(Controls.JUMP));

        keyboardSystem.keyDown(Keys.SPACE);
        keyboardSystem.keyDown(Keys.W);
        keyboardSystem.keyDown(Keys.S);

        engine.update();
        assertFalse(input.controls.contains(Controls.MOVE_LEFT));
        assertFalse(input.controls.contains(Controls.MOVE_RIGHT));
        assertTrue(input.controls.contains(Controls.JUMP));

        keyboardSystem.keyUp(Keys.SPACE);
        keyboardSystem.keyDown(Keys.A);
        keyboardSystem.keyDown(Keys.D);
        keyboardSystem.keyUp(Keys.W);
        keyboardSystem.keyUp(Keys.S);

        engine.update();
        assertTrue(input.controls.contains(Controls.MOVE_LEFT));
        assertTrue(input.controls.contains(Controls.MOVE_RIGHT));
        assertFalse(input.controls.contains(Controls.JUMP));

        keyboardSystem.keyUp(Keys.A);
        keyboardSystem.keyUp(Keys.D);
        engine.update();

        assertFalse(input.controls.contains(Controls.MOVE_LEFT));
        assertFalse(input.controls.contains(Controls.MOVE_RIGHT));
        assertFalse(input.controls.contains(Controls.JUMP));
    }
}
