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
package com.github.antag99.retinazer.beam.util;

import com.github.antag99.retinazer.beam.component.Keyboard;
import com.github.antag99.retinazer.beam.system.KeyboardSystem;

/**
 * Controls are flags that determine how an entity will act. Some examples
 * could be <em>move left</em>, <em>move right</em> and <em>jump</em>. These
 * controls would be defined as follows:
 *
 * <pre>
 * <code>
 * public final class Controls {
 *     private Controls() {
 *     }
 *
 *     public static final Control MOVE_LEFT = new Control("Controls.MOVE_LEFT");
 *     public static final Control MOVE_RIGHT = new Control("Controls.MOVE_RIGHT");
 *     public static final Control JUMP = new Control("Controls.JUMP");
 * }
 * </code>
 * </pre>
 *
 * These controls could then be bound to keys using a {@link Keyboard} component
 * along with {@link KeyboardSystem}. Note that this allows replacing controllers
 * at any time without major refactoring.
 */
public final class Control extends Ordinal<Control> {
    public static final OrdinalType<Control> TYPE = new OrdinalType<>();

    public Control(String name) {
        super(TYPE, name);
    }

    public static final Control forIndex(int index) {
        return TYPE.forIndex(index);
    }

    public static final Control forName(String name) {
        return TYPE.forName(name);
    }
}
