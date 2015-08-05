package com.github.antag99.benchmarks.entreri;

import org.openjdk.jmh.infra.Blackhole;

import com.lhkbob.entreri.Component;
import com.lhkbob.entreri.ComponentIterator;
import com.lhkbob.entreri.EntitySystem;

public abstract class RetrievalSystem implements Runnable {
    private Blackhole voidness = new Blackhole();
    private ComponentIterator iterator;
    private Component component;

    public RetrievalSystem(EntitySystem entitySystem, Class<? extends Component> componentType) {
        this.iterator = entitySystem.fastIterator();
        this.component = iterator.addRequired(componentType);
    }

    @Override
    public void run() {
        Component component = this.component;
        ComponentIterator iterator = this.iterator;
        iterator.reset();
        while (iterator.next()) {
            voidness.consume(iterator);
            voidness.consume(component);
        }
    }
}
