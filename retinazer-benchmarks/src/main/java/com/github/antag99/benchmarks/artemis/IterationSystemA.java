package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.infra.Blackhole;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.github.antag99.benchmarks.artemis.components.ComponentA;

public class IterationSystemA extends EntityProcessingSystem {
    private Blackhole voidness = new Blackhole();

    @SuppressWarnings("unchecked")
    public IterationSystemA() {
        super(Aspect.getAspectForAll(ComponentA.class));
    }

    @Override
    public void process(Entity entity) {
        voidness.consume(entity);
    }
}
