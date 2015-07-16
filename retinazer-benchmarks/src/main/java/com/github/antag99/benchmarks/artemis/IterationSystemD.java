package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.infra.Blackhole;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.github.antag99.benchmarks.artemis.components.ComponentD;

public class IterationSystemD extends EntityProcessingSystem {
    private Blackhole voidness = new Blackhole();

    @SuppressWarnings("unchecked")
    public IterationSystemD() {
        super(Aspect.getAspectForAll(ComponentD.class));
    }

    @Override
    public void process(Entity entity) {
        voidness.consume(entity);
    }
}
