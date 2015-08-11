package com.github.antag99.benchmarks.ashley;

import org.openjdk.jmh.infra.Blackhole;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public final class RetrievalSystemA extends IteratingSystem {
    private static final ComponentMapper<ComponentA> mapper =
            ComponentMapper.getFor(ComponentA.class);
    private Blackhole voidness = new Blackhole();

    public RetrievalSystemA() {
        super(Family.all(ComponentA.class).get());
    }

    @Override
    public final void processEntity(Entity entity, float deltaTime) {
        ComponentA component = mapper.get(entity);
        voidness.consume(entity);
        voidness.consume(deltaTime);
        voidness.consume(component);
        voidness.consume(component.a);
        voidness.consume(component.b);
        voidness.consume(component.c);
        voidness.consume(component.d);
        voidness.consume(component.e);
        voidness.consume(component.f);
        voidness.consume(component.g);
        voidness.consume(component.h);
    }
}
