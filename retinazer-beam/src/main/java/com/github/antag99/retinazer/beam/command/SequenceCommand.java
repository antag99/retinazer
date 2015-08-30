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
package com.github.antag99.retinazer.beam.command;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.github.antag99.retinazer.Handle;

public final class SequenceCommand extends Command {
    public Array<Command> commands = new Array<Command>();
    private int index = 0;

    @Override
    public float apply(Handle entity, float time) {
        while (index < commands.size && time > 0f) {
            time = commands.get(index).apply(entity, time);
            if (time > 0f) {
                index++;
            }
        }
        return time;
    }

    @Override
    public void restart() {
        for (int i = 0; i < commands.size; i++)
            commands.get(i).restart();
        index = 0;
    }

    @Override
    public void reset() {
        super.reset();
        Pools.freeAll(commands);
        commands.clear();
    }
}
