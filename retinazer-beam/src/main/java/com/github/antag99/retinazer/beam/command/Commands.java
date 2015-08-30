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

import com.badlogic.gdx.utils.Pools;
import com.github.antag99.retinazer.Component;

public final class Commands {
    private Commands() {
    }

    public static <T extends Command> T command(Class<T> type) {
        return Pools.obtain(type);
    }

    // seq(...) overloads are to avoid GC resulting from varargs arrays

    public static SequenceCommand seq(Command c0, Command c1) {
        SequenceCommand sequence = command(SequenceCommand.class);
        sequence.commands.add(c0);
        sequence.commands.add(c1);
        return sequence;
    }

    public static SequenceCommand seq(Command c0, Command c1, Command c2) {
        SequenceCommand sequence = command(SequenceCommand.class);
        sequence.commands.add(c0);
        sequence.commands.add(c1);
        sequence.commands.add(c2);
        return sequence;
    }

    public static SequenceCommand seq(Command c0, Command c1, Command c2, Command c3) {
        SequenceCommand sequence = command(SequenceCommand.class);
        sequence.commands.add(c0);
        sequence.commands.add(c1);
        sequence.commands.add(c2);
        sequence.commands.add(c3);
        return sequence;
    }

    public static SequenceCommand seq(Command c0, Command c1, Command c2, Command c3, Command c4) {
        SequenceCommand sequence = command(SequenceCommand.class);
        sequence.commands.add(c0);
        sequence.commands.add(c1);
        sequence.commands.add(c2);
        sequence.commands.add(c3);
        sequence.commands.add(c4);
        return sequence;
    }

    public static SequenceCommand seq(Command... commands) {
        SequenceCommand sequence = command(SequenceCommand.class);
        sequence.commands.addAll(commands);
        return sequence;
    }

    public static LoopCommand loop(Command command) {
        LoopCommand loop = command(LoopCommand.class);
        loop.command = command;
        return loop;
    }

    public static RepeatCommand repeat(Command command, int times) {
        RepeatCommand repeat = command(RepeatCommand.class);
        repeat.command = command;
        repeat.times = times;
        return repeat;
    }

    public static AddCommand add(Component component) {
        AddCommand add = command(AddCommand.class);
        add.component = component;
        return add;
    }

    public static RemoveCommand remove(Class<? extends Component> componentType) {
        RemoveCommand remove = command(RemoveCommand.class);
        remove.componentType = componentType;
        return remove;
    }

    public static DestroyCommand destroy() {
        return command(DestroyCommand.class);
    }

    public static DelayCommand delay(float time) {
        DelayCommand delay = command(DelayCommand.class);
        delay.delay = time;
        return delay;
    }
}
