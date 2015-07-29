package com.github.antag99.benchmarks.retinazer;

import org.openjdk.jmh.infra.Blackhole;

import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.ComponentMapper;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.Entity;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.EventHandler;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.utils.DestroyEvent;
import com.github.antag99.retinazer.utils.InitializeEvent;
import com.github.antag99.retinazer.utils.Inject;

public abstract class RetrievalSystem extends EntityProcessorSystem {
    private @Inject Engine engine;
    private Blackhole voidness = new Blackhole();
    private Class<? extends Component> componentType;
    private ComponentMapper<? extends Component> componentMapper;

    public RetrievalSystem(Class<? extends Component> componentType) {
        super(Family.with(componentType));

        this.componentType = componentType;
    }

    @EventHandler
    private void initialize(InitializeEvent event) {
        componentMapper = engine.getMapper(componentType);
    }

    @EventHandler
    private void destroy(DestroyEvent event) {
        componentMapper = null;
    }

    @Override
    public final void process(Entity entity) {
        voidness.consume(entity);
        voidness.consume(componentMapper.get(entity));
    }
}
