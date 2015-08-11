package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.infra.Blackhole;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;

@Wire
public final class PackedRetrievalSystemC extends EntityProcessingSystem {
    private Blackhole voidness = new Blackhole();
    private ComponentMapper<PackedComponentC> mapper;

    @SuppressWarnings("unchecked")
    public PackedRetrievalSystemC() {
        super(Aspect.all(PackedComponentC.class));
    }

    @Override
    public final void process(Entity entity) {
        PackedComponentC component = mapper.get(entity);
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
