package com.github.antag99.benchmarks.ashley;

import org.openjdk.jmh.infra.Blackhole;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public final class IterationSystemC extends IteratingSystem {
    private Blackhole voidness = new Blackhole();

    public IterationSystemC() {
        super(Family.all(ComponentC.class).get());
    }

    @Override
    public final void processEntity(Entity entity, float deltaTime) {
        voidness.consume(entity);
        voidness.consume(deltaTime);
    }
}
