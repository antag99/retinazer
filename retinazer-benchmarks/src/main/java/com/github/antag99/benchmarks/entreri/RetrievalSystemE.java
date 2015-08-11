package com.github.antag99.benchmarks.entreri;

import org.openjdk.jmh.infra.Blackhole;

import com.lhkbob.entreri.ComponentIterator;
import com.lhkbob.entreri.EntitySystem;

public final class RetrievalSystemE implements EntreriSystem {
    private Blackhole voidness = new Blackhole();
    private ComponentIterator iterator;
    private ComponentE component;

    public RetrievalSystemE(EntitySystem entitySystem) {
        this.iterator = entitySystem.fastIterator();
        this.component = iterator.addRequired(ComponentE.class);
    }

    @Override
    public void process() {
        ComponentE component = this.component;
        ComponentIterator iterator = this.iterator;
        iterator.reset();
        while (iterator.next()) {
            voidness.consume(component.getA());
            voidness.consume(component.getB());
            voidness.consume(component.getC());
            voidness.consume(component.getD());
            voidness.consume(component.getE());
            voidness.consume(component.getF());
            voidness.consume(component.getG());
            voidness.consume(component.getH());
        }
    }
}
