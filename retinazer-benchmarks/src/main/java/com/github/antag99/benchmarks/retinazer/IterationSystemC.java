package com.github.antag99.benchmarks.retinazer;

import org.openjdk.jmh.infra.Blackhole;

import com.github.antag99.retinazer.Entity;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;

public final class IterationSystemC extends EntityProcessorSystem {
    private Blackhole voidness = new Blackhole();

    public IterationSystemC() {
        super(Family.with(ComponentC.class));
    }

    @Override
    public final void process(Entity entity) {
        voidness.consume(entity);
    }
}
