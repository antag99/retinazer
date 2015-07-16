package com.github.antag99.benchmarks.ashley;

import org.openjdk.jmh.infra.Blackhole;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.antag99.benchmarks.ashley.components.ComponentA;

public class IterationSystemA extends IteratingSystem {
    private Blackhole voidness = new Blackhole();

    public IterationSystemA() {
        super(Family.all(ComponentA.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        voidness.consume(entity);
    }
}
