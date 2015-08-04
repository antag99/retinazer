package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.infra.Blackhole;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;

@Wire
public final class RetrievalSystemF extends EntityProcessingSystem {
    private Blackhole voidness = new Blackhole();
    private ComponentMapper<ComponentF> mapper;

    @SuppressWarnings("unchecked")
    public RetrievalSystemF() {
        super(Aspect.all(ComponentF.class));
    }

    @Override
    public final void process(Entity entity) {
        voidness.consume(entity);
        voidness.consume(mapper.get(entity));
    }
}
