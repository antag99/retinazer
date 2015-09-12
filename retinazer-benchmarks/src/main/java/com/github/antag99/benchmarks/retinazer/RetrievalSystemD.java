package com.github.antag99.benchmarks.retinazer;

import org.openjdk.jmh.infra.Blackhole;

import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;

public final class RetrievalSystemD extends EntityProcessorSystem {
    @SkipWire
    private Blackhole voidness = new Blackhole();
    private Mapper<ComponentD> mapper;

    public RetrievalSystemD() {
        super(Family.with(ComponentD.class));
    }

    @Override
    public final void process(int entity) {
        ComponentD component = mapper.get(entity);
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
