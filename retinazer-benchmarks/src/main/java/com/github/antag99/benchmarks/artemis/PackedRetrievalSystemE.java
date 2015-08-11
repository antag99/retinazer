package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.infra.Blackhole;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;

@Wire
public final class PackedRetrievalSystemE extends EntityProcessingSystem {
    private Blackhole voidness = new Blackhole();
    private ComponentMapper<PackedComponentE> mapper;

    @SuppressWarnings("unchecked")
    public PackedRetrievalSystemE() {
        super(Aspect.all(PackedComponentE.class));
    }

    @Override
    public final void process(Entity entity) {
        PackedComponentE component = mapper.get(entity);
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
