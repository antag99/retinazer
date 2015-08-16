package com.github.antag99.benchmarks.retinazer;

import org.openjdk.jmh.infra.Blackhole;

import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;

public final class IterationSystemF extends EntityProcessorSystem {
    private Blackhole voidness = new Blackhole();

    public IterationSystemF() {
        super(Family.with(ComponentF.class));
    }

    @Override
    public final void process(int entity) {
        voidness.consume(entity);
    }
}
