package com.github.antag99.benchmarks.ashley;

import org.openjdk.jmh.infra.Blackhole;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.antag99.benchmarks.ashley.components.ComponentH;

public class IterationSystemH extends IteratingSystem {
    private Blackhole voidness = new Blackhole();

    public IterationSystemH() {
        super(Family.all(ComponentH.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        voidness.consume(entity);
    }
}
