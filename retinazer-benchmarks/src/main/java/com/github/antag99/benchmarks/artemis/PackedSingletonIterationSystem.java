package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.infra.Blackhole;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

public final class PackedSingletonIterationSystem extends EntityProcessingSystem {
    private Blackhole voidness = new Blackhole();

    @SuppressWarnings("unchecked")
    public PackedSingletonIterationSystem() {
        super(Aspect.all(PackedSingletonComponent.class));
    }

    @Override
    protected void process(Entity entity) {
        voidness.consume(entity);
    }
}
