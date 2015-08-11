package com.github.antag99.benchmarks.ashley;

import org.openjdk.jmh.infra.Blackhole;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public final class IterationSystemD extends IteratingSystem {
    private Blackhole voidness = new Blackhole();

    public IterationSystemD() {
        super(Family.all(ComponentD.class).get());
    }

    @Override
    public final void processEntity(Entity entity, float deltaTime) {
        voidness.consume(entity);
        voidness.consume(deltaTime);
    }
}
