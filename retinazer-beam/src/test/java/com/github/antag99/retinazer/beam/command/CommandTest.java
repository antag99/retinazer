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

import static com.github.antag99.retinazer.beam.command.Commands.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.utils.Pools;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;
import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.beam.FlagA;
import com.github.antag99.retinazer.beam.FlagB;
import com.github.antag99.retinazer.beam.command.AddCommand;
import com.github.antag99.retinazer.beam.command.Command;
import com.github.antag99.retinazer.beam.command.DelayCommand;
import com.github.antag99.retinazer.beam.command.DestroyCommand;
import com.github.antag99.retinazer.beam.command.LoopCommand;
import com.github.antag99.retinazer.beam.command.RemoveCommand;
import com.github.antag99.retinazer.beam.command.RepeatCommand;
import com.github.antag99.retinazer.beam.command.SequenceCommand;

public class CommandTest {
    @Test
    public void testAdd() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createEntity();
        FlagA flag = new FlagA();
        AddCommand add = add(flag);
        add.apply(entity, 0f);
        assertEquals(flag, engine.getMapper(FlagA.class).get(entity.getEntity()));
        Pools.free(add);
        add = add(new FlagA());
        add.apply(entity, 0f);
        // should *not* change added component
        assertEquals(flag, engine.getMapper(FlagA.class).get(entity.getEntity()));
    }

    @Test
    public void testRemove() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createEntity();
        entity.create(FlagA.class);
        RemoveCommand remove = remove(FlagA.class);
        remove.apply(entity, 0f);
        engine.update();
        assertFalse(engine.getMapper(FlagA.class).has(entity.getEntity()));
        Pools.free(remove);
        remove = remove(FlagB.class);
        remove.apply(entity, 0f);
        engine.update();
        assertFalse(engine.getMapper(FlagB.class).has(entity.getEntity()));
    }

    @Test
    public void testDestroy() {
        Engine engine = new Engine(new EngineConfig());
        Handle entity = engine.createEntity();
        DestroyCommand destroy = destroy();
        destroy.apply(entity, 0f);
        engine.update();
        assertEquals(new EntitySet(), engine.getEntities());
        Pools.free(destroy);
        destroy = destroy();
        entity = engine.createEntity();
        engine.update();
        destroy.apply(entity, 0f);
        engine.update();
        assertEquals(new EntitySet(), engine.getEntities());
    }

    @Test
    public void testDelay() {
        Engine engine = new Engine(new EngineConfig());
        Handle dummy = engine.createEntity();
        DelayCommand delay = delay(5f);
        assertEquals(0f, delay.apply(dummy, 4f), 0f);
        assertEquals(1f, delay.apply(dummy, 2f), 0f);
        delay.restart();
        assertEquals(0f, delay.apply(dummy, 4f), 0f);
        assertEquals(1f, delay.apply(dummy, 2f), 0f);
        Pools.free(delay);
        delay = delay(5f);
        assertEquals(0f, delay.apply(dummy, 4f), 0f);
        assertEquals(1f, delay.apply(dummy, 2f), 0f);
        delay.restart();
        assertEquals(0f, delay.apply(dummy, 4f), 0f);
        assertEquals(1f, delay.apply(dummy, 2f), 0f);
    }

    @Test
    public void testSequence() {
        Engine engine = new Engine(new EngineConfig());
        Handle dummy = engine.createEntity();
        DelayCommand delay0 = delay(0f);
        DelayCommand delay1 = delay(1f);
        DelayCommand delay2 = delay(2f);
        DelayCommand delay3 = delay(3f);
        DelayCommand delay4 = delay(4f);
        DelayCommand delay5 = delay(5f);
        SequenceCommand sequence = seq(delay0, delay1, delay2, delay3, delay4, delay5);
        sequence.apply(dummy, 0f);
        assertEquals(1f, delay0.apply(dummy, 1f), 0f);
        sequence.apply(dummy, 1f);
        assertEquals(1f, delay1.apply(dummy, 1f), 0f);
        sequence.apply(dummy, 2f);
        assertEquals(1f, delay2.apply(dummy, 1f), 0f);
        sequence.apply(dummy, 7f);
        assertEquals(1f, delay3.apply(dummy, 1f), 0f);
        assertEquals(1f, delay4.apply(dummy, 1f), 0f);
        assertEquals(0f, delay5.apply(dummy, 4f), 0f);
        sequence.apply(dummy, 1f);
        assertEquals(1f, delay5.apply(dummy, 1f), 0f);
        assertEquals(1f, sequence.apply(dummy, 1f), 0f);
        Pools.free(sequence);

        delay0 = delay(0f);
        delay1 = delay(1f);
        delay2 = delay(2f);
        delay3 = delay(3f);
        delay4 = delay(4f);
        delay5 = delay(5f);
        sequence = seq(delay0, delay1, delay2, delay3, delay4, delay5);
        sequence.apply(dummy, 0f);
        assertEquals(1f, delay0.apply(dummy, 1f), 0f);
        sequence.apply(dummy, 1f);
        assertEquals(1f, delay1.apply(dummy, 1f), 0f);
        sequence.apply(dummy, 2f);
        assertEquals(1f, delay2.apply(dummy, 1f), 0f);
        sequence.apply(dummy, 7f);
        assertEquals(1f, delay3.apply(dummy, 1f), 0f);
        assertEquals(1f, delay4.apply(dummy, 1f), 0f);
        assertEquals(0f, delay5.apply(dummy, 4f), 0f);
        sequence.apply(dummy, 1f);
        assertEquals(1f, delay5.apply(dummy, 1f), 0f);
        assertEquals(1f, sequence.apply(dummy, 1f), 0f);
    }

    @Test
    public void testSequenceOverloads() {
        DelayCommand delay0 = delay(0f);
        DelayCommand delay1 = delay(1f);
        DelayCommand delay2 = delay(2f);
        DelayCommand delay3 = delay(3f);
        DelayCommand delay4 = delay(4f);
        DelayCommand delay5 = delay(5f);

        assertEquals(seq(new Command[] { delay0, delay1, delay2, delay3, delay4, delay5 }).commands,
                seq(delay0, delay1, delay2, delay3, delay4, delay5).commands);

        assertEquals(seq(new Command[] { delay0, delay1, delay2, delay3, delay4 }).commands,
                seq(delay0, delay1, delay2, delay3, delay4).commands);

        assertEquals(seq(new Command[] { delay0, delay1, delay2, delay3 }).commands,
                seq(delay0, delay1, delay2, delay3).commands);

        assertEquals(seq(new Command[] { delay0, delay1, delay2 }).commands,
                seq(delay0, delay1, delay2).commands);

        assertEquals(seq(new Command[] { delay0, delay1 }).commands,
                seq(delay0, delay1).commands);

        assertEquals(seq(new Command[] { delay0 }).commands,
                seq(delay0).commands);

        assertEquals(seq(new Command[] {}).commands,
                seq().commands);
    }

    @Test
    public void testLoop() {
        Engine engine = new Engine(new EngineConfig());
        Handle dummy = engine.createEntity();
        DelayCommand delay = delay(1f);
        LoopCommand loop = loop(delay);
        assertEquals(0f, loop.apply(dummy, 0.5f), 0f);
        assertEquals(0.5f, delay.apply(dummy, 1f), 0f);
        assertEquals(1f, delay.apply(dummy, 1f), 0f);
        assertEquals(0f, loop.apply(dummy, 5f), 0f);
        assertEquals(1f, delay.apply(dummy, 1f), 0f);
        Pools.free(delay);
        Pools.free(loop);
        delay = delay(1f);
        loop = loop(delay);
        assertEquals(0f, loop.apply(dummy, 0.5f), 0f);
        assertEquals(0.5f, delay.apply(dummy, 1f), 0f);
        assertEquals(1f, delay.apply(dummy, 1f), 0f);
        assertEquals(0f, loop.apply(dummy, 5f), 0f);
        assertEquals(1f, delay.apply(dummy, 1f), 0f);
    }

    @Test
    public void testRepeat() {
        Engine engine = new Engine(new EngineConfig());
        Handle dummy = engine.createEntity();
        DelayCommand delay = delay(1f);
        RepeatCommand repeat = repeat(delay, 5);
        assertEquals(0f, repeat.apply(dummy, 0.5f), 0f);
        assertEquals(0.5f, delay.apply(dummy, 1f), 0f);
        assertEquals(0f, repeat.apply(dummy, 0.5f), 0f);
        assertEquals(0.5f, delay.apply(dummy, 1f), 0f);
        assertEquals(0f, repeat.apply(dummy, 0.5f), 0f);
        assertEquals(0.5f, delay.apply(dummy, 1f), 0f);
        assertEquals(0f, repeat.apply(dummy, 0.5f), 0f);
        assertEquals(0.5f, delay.apply(dummy, 1f), 0f);
        assertEquals(0f, repeat.apply(dummy, 0.5f), 0f);
        assertEquals(0.5f, delay.apply(dummy, 1f), 0f);
        assertEquals(0.5f, repeat.apply(dummy, 0.5f), 0f);
        assertEquals(0.5f, repeat.apply(dummy, 0.5f), 0f);
        Pools.free(repeat);
        delay = delay(1f);
        repeat = repeat(delay, 5);
        assertEquals(0f, repeat.apply(dummy, 0.5f), 0f);
        assertEquals(0.5f, delay.apply(dummy, 1f), 0f);
        assertEquals(0f, repeat.apply(dummy, 0.5f), 0f);
        assertEquals(0.5f, delay.apply(dummy, 1f), 0f);
        assertEquals(0f, repeat.apply(dummy, 0.5f), 0f);
        assertEquals(0.5f, delay.apply(dummy, 1f), 0f);
        assertEquals(0f, repeat.apply(dummy, 0.5f), 0f);
        assertEquals(0.5f, delay.apply(dummy, 1f), 0f);
        assertEquals(0f, repeat.apply(dummy, 0.5f), 0f);
        assertEquals(0.5f, delay.apply(dummy, 1f), 0f);
        assertEquals(0.5f, repeat.apply(dummy, 0.5f), 0f);
        assertEquals(0.5f, repeat.apply(dummy, 0.5f), 0f);
    }
}
