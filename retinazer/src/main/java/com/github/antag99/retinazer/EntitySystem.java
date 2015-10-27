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
package com.github.antag99.retinazer;

/**
 * Base class for system implementations.
 */
@Wire
public abstract class EntitySystem {

    /**
     * Engine instance this entity system is added to, for convenience.
     */
    protected Engine engine;

    /**
     * Temporary handle for entities in the engine, for convenience.
     */
    @SkipWire
    protected Handle handle;

    /**
     * Framework-side initialization method. End users should not override
     * this method. Always call {@code super.setup()} when overriding this.
     */
    protected void setup() {
        handle = engine.createHandle();
    }

    /**
     * Initializes this system. If you override this method, mark it {@code final}.
     */
    protected void initialize() {
    }

    /**
     * Updates this system. If you override this method, mark it {@code final}.
     */
    protected void update() {
    }
}
