package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.infra.Blackhole;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.github.antag99.benchmarks.artemis.components.ComponentE;

public class IterationSystemE extends EntityProcessingSystem {
    private Blackhole voidness = new Blackhole();

    @SuppressWarnings("unchecked")
    public IterationSystemE() {
        super(Aspect.getAspectForAll(ComponentE.class));
    }

    @Override
    public void process(Entity entity) {
        voidness.consume(entity);
    }
}
