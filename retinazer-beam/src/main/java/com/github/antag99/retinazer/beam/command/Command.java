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

import com.badlogic.gdx.utils.Pool.Poolable;
import com.github.antag99.retinazer.Handle;

/**
 * Commands are used for scripting entities. This can be used for reducing
 * specialized system implementations and data-driven logic.
 */
public abstract class Command implements Poolable {

    /**
     * Applies this command to the given entity.
     *
     * @param entity
     *            the entity bound to this command.
     * @param time
     *            time given for this command to execute.
     * @return time this command didn't use; will be given to the next
     *         command in a queue; anything {@code > 0} will denote that the
     *         command has finished executing.
     */
    public abstract float apply(Handle entity, float time);

    /**
     * Restarts this command; resets state, but <b>not</b> configuration. E.g.
     * the command can be executed once again.
     */
    public abstract void restart();

    /**
     * Resets this command, after which the instance can be reused.
     */
    @Override
    public void reset() {
        restart();
    }
}
