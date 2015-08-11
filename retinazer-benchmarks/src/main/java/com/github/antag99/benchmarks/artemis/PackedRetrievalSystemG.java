package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.infra.Blackhole;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;

@Wire
public final class PackedRetrievalSystemG extends EntityProcessingSystem {
    private Blackhole voidness = new Blackhole();
    private ComponentMapper<PackedComponentG> mapper;

    @SuppressWarnings("unchecked")
    public PackedRetrievalSystemG() {
        super(Aspect.all(PackedComponentG.class));
    }

    @Override
    public final void process(Entity entity) {
        PackedComponentG component = mapper.get(entity);
        voidness.consume(entity);
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
