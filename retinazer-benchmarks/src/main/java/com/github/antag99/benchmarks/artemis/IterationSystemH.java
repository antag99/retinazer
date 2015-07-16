package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.infra.Blackhole;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.github.antag99.benchmarks.artemis.components.ComponentH;

public class IterationSystemH extends EntityProcessingSystem {
    private Blackhole voidness = new Blackhole();

    @SuppressWarnings("unchecked")
    public IterationSystemH() {
        super(Aspect.getAspectForAll(ComponentH.class));
    }

    @Override
    public void process(Entity entity) {
        voidness.consume(entity);
    }
}
