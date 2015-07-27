package com.github.antag99.benchmarks.ashley;

import org.openjdk.jmh.infra.Blackhole;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public abstract class RetrievalSystem extends IteratingSystem {
    private Blackhole voidness = new Blackhole();
    private ComponentMapper<? extends Component> componentMapper;

    public RetrievalSystem(Class<? extends Component> componentType) {
        super(Family.all(componentType).get());

        componentMapper = ComponentMapper.getFor(componentType);
    }

    @Override
    public final void processEntity(Entity entity, float deltaTime) {
        voidness.consume(entity);
        voidness.consume(deltaTime);
        voidness.consume(componentMapper.get(entity));
    }
}
