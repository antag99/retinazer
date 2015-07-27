package com.github.antag99.benchmarks.retinazer;

import org.openjdk.jmh.infra.Blackhole;

import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.Entity;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;

public abstract class IterationSystem extends EntityProcessorSystem {
    private Blackhole voidness = new Blackhole();

    public IterationSystem(Class<? extends Component> componentType) {
        super(Family.with(componentType));
    }

    @Override
    public final void process(Entity entity) {
        voidness.consume(entity);
    }
}
