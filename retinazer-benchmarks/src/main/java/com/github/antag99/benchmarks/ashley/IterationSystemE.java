package com.github.antag99.benchmarks.ashley;

import org.openjdk.jmh.infra.Blackhole;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.antag99.benchmarks.ashley.components.ComponentE;

public class IterationSystemE extends IteratingSystem {
    private Blackhole voidness = new Blackhole();

    public IterationSystemE() {
        super(Family.all(ComponentE.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        voidness.consume(entity);
    }
}
