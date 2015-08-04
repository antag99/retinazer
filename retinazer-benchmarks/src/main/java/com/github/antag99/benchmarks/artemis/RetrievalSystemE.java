package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.infra.Blackhole;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;

@Wire
public final class RetrievalSystemE extends EntityProcessingSystem {
    private Blackhole voidness = new Blackhole();
    private ComponentMapper<ComponentE> mapper;

    @SuppressWarnings("unchecked")
    public RetrievalSystemE() {
        super(Aspect.all(ComponentE.class));
    }

    @Override
    public final void process(Entity entity) {
        voidness.consume(entity);
        voidness.consume(mapper.get(entity));
    }
}
