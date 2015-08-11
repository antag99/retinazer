package com.github.antag99.benchmarks.ashley;

import org.openjdk.jmh.infra.Blackhole;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public final class RetrievalSystemB extends IteratingSystem {
    private static final ComponentMapper<ComponentB> mapper =
            ComponentMapper.getFor(ComponentB.class);
    private Blackhole voidness = new Blackhole();

    public RetrievalSystemB() {
        super(Family.all(ComponentB.class).get());
    }

    @Override
    public final void processEntity(Entity entity, float deltaTime) {
        ComponentB component = mapper.get(entity);
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
