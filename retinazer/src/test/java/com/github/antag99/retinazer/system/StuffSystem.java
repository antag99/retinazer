package com.github.antag99.retinazer.system;

import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.PackedComponent;
import com.github.antag99.retinazer.PackedMapper;
import com.github.antag99.retinazer.PlainMapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.retinazer.component.Stuff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class StuffSystem extends EntityProcessorSystem {
    private Mapper<Stuff> mStuff;

    private @SkipWire boolean packed;

    public StuffSystem(boolean packed) {
        super(Family.with(Stuff.class));

        this.packed = packed;
    }

    @Override
    protected void initialize() {
        if (packed) {
            assertTrue(mStuff instanceof PackedMapper);
        } else {
            assertTrue(mStuff instanceof PlainMapper);
        }
    }

    public int createStuff() {
        int entity = engine.createEntity();
        Stuff stuff = mStuff.create(entity);
        assertEquals(stuff.myBoolean, true);
        assertEquals(stuff.myByte, 42);
        assertEquals(stuff.myShort, 13);
        assertEquals(stuff.myChar, 3);
        assertEquals(stuff.myInt, 4);
        assertEquals(stuff.myLong, 42424242);
        assertEquals(stuff.myFloat, 3.141592653f, 0f);
        assertEquals(stuff.myDouble, 0.333333333333d, 0f);
        if (packed) {
            assertTrue(PackedComponent.class.isInstance(stuff));
        } else {
            assertTrue(Component.class.isInstance(stuff));
        }
        return entity;
    }

    @Override
    protected void process(int entity) {
        Stuff stuff = mStuff.get(entity);
        stuff.shuffle();
        stuff.square();
    }
}
