package com.github.antag99.benchmarks.retinazer;

import org.openjdk.jmh.infra.Blackhole;

import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.SkipWire;

public final class IterationSystemH extends EntityProcessorSystem {
    @SkipWire
    private Blackhole voidness = new Blackhole();

    public IterationSystemH() {
        super(Family.with(ComponentH.class));
    }

    @Override
    public final void process(int entity) {
        voidness.consume(entity);
    }
}
