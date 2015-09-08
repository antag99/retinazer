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

import com.badlogic.gdx.InputProcessor;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;
import com.github.antag99.retinazer.Wire.Exclude;
import com.github.antag99.retinazer.beam.component.Input;
import com.github.antag99.retinazer.beam.component.Keyboard;
import com.github.antag99.retinazer.beam.util.Control;
import com.github.antag99.retinazer.util.Mask;

@Wire
public final class KeyboardSystem extends EntityProcessorSystem implements InputProcessor {
    private Mapper<Keyboard> mKeyboard;
    private Mapper<Input> mInput;

    @Exclude
    private Mask activeKeys = new Mask();

    @Exclude
    private Mask justPressedKeys = new Mask();

    @Exclude
    private Mask justReleasedKeys = new Mask();

    public KeyboardSystem() {
        super(Family.with(Keyboard.class, Input.class));
    }

    @Override
    protected void processEntities() {
        super.processEntities();

        justPressedKeys.clear();
        justReleasedKeys.clear();
    }

    @Override
    protected void process(int entity) {
        final Keyboard keyboard = mKeyboard.get(entity);
        final Input input = mInput.get(entity);

        for (int i = justPressedKeys.nextSetBit(0); i != -1; i = justPressedKeys.nextSetBit(i + 1)) {
            Control control = keyboard.controls.get(i);
            if (control != null)
                input.controls.add(control);
        }

        for (int i = justReleasedKeys.nextSetBit(0); i != -1; i = justReleasedKeys.nextSetBit(i + 1)) {
            Control control = keyboard.controls.get(i);
            if (control != null)
                input.controls.remove(control);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        justPressedKeys.set(keycode);
        activeKeys.set(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        justReleasedKeys.set(keycode);
        activeKeys.clear(keycode);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
