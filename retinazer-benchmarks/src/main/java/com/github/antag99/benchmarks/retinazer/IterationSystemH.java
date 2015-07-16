package com.github.antag99.benchmarks.retinazer;

import org.openjdk.jmh.infra.Blackhole;

import com.github.antag99.benchmarks.retinazer.components.ComponentH;
import com.github.antag99.retinazer.Entity;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;

public class IterationSystemH extends EntityProcessorSystem {
    private Blackhole voidness = new Blackhole();

    public IterationSystemH() {
        super(Family.with(ComponentH.class));
    }

    @Override
    public void process(Entity entity) {
        voidness.consume(entity);
    }
}
