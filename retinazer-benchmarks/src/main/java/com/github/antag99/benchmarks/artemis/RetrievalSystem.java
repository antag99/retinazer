package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.infra.Blackhole;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

public abstract class RetrievalSystem extends EntityProcessingSystem {
    private Blackhole voidness = new Blackhole();
    private Class<? extends Component> componentType;
    private ComponentMapper<? extends Component> componentMapper;

    @SuppressWarnings("unchecked")
    public RetrievalSystem(Class<? extends Component> componentType) {
        super(Aspect.all(componentType));

        this.componentType = componentType;
    }

    @Override
    public void initialize() {
        componentMapper = world.getMapper(componentType);

        super.initialize();
    }

    @Override
    public final void process(Entity entity) {
        voidness.consume(entity);
        voidness.consume(componentMapper.get(entity));
    }
}
