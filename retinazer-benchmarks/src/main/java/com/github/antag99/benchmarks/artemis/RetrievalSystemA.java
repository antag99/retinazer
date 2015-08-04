package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.infra.Blackhole;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;

@Wire
public final class RetrievalSystemA extends EntityProcessingSystem {
    private Blackhole voidness = new Blackhole();
    private ComponentMapper<ComponentA> mapper;

    @SuppressWarnings("unchecked")
    public RetrievalSystemA() {
        super(Aspect.all(ComponentA.class));
    }

    @Override
    public final void process(Entity entity) {
        voidness.consume(entity);
        voidness.consume(mapper.get(entity));
    }
}
