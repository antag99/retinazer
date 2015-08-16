package com.github.antag99.benchmarks.retinazer;

import org.openjdk.jmh.infra.Blackhole;

import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;

public final class IterationSystemD extends EntityProcessorSystem {
    private Blackhole voidness = new Blackhole();

    public IterationSystemD() {
        super(Family.with(ComponentD.class));
    }

    @Override
    public final void process(int entity) {
        voidness.consume(entity);
    }
}
