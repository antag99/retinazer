package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.infra.Blackhole;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

public final class IterationSystemD extends EntityProcessingSystem {
    private Blackhole voidness = new Blackhole();

    @SuppressWarnings("unchecked")
    public IterationSystemD() {
        super(Aspect.all(ComponentD.class));
    }

    @Override
    protected void process(Entity e) {
        voidness.consume(e);
    }
}
