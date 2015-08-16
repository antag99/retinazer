package com.github.antag99.benchmarks.retinazer;

import org.openjdk.jmh.infra.Blackhole;

import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;

public final class SingletonIterationSystem extends EntityProcessorSystem {
    private Blackhole voidness = new Blackhole();

    public SingletonIterationSystem() {
        super(Family.with(SingletonComponent.class));
    }

    @Override
    protected void process(int entity) {
        voidness.consume(entity);
    }
}
