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
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;
import com.github.antag99.retinazer.beam.command.Command;
import com.github.antag99.retinazer.beam.component.Script;

@Wire
public final class ScriptSystem extends EntityProcessorSystem {
    private Engine engine;
    private DeltaSystem deltaSystem;
    private Mapper<Script> mScript;

    @SkipWire
    private Handle handle;

    public ScriptSystem() {
        super(Family.with(Script.class));
    }

    @Override
    protected void initialize() {
        this.handle = engine.createHandle();
    }

    @Override
    protected void process(int entity) {
        final float deltaTime = deltaSystem.getDeltaTime();
        final Script script = mScript.get(entity);
        final Object[] items = script.commands.begin();
        for (int i = 0, n = script.commands.size; i < n; i++) {
            Command command = (Command) items[i];

            if (command.apply(handle.idx(entity), deltaTime) > 0f) {
                script.commands.removeValue(command, true);
            }
        }
        script.commands.end();
    }
}
