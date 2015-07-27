package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.infra.Blackhole;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

public abstract class IterationSystem extends EntityProcessingSystem {
    private Blackhole voidness = new Blackhole();

    @SuppressWarnings("unchecked")
    public IterationSystem(Class<? extends Component> componentType) {
        super(Aspect.all(componentType));
    }

    @Override
    public final void process(Entity entity) {
        voidness.consume(entity);
    }
}
