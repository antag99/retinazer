package com.github.antag99.benchmarks.ashley;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

public class SingletonIterationBenchmark extends AshleyBenchmark {
    private Engine engine;

    @Setup
    public void setup() {
        engine = new Engine();
        engine.addSystem(new SingletonIterationSystem());
        for (int i = 0, n = getEntityCount(); i < n; i++) {
            Entity entity = new Entity();
            if ((i % 8) == 0)
                entity.add(SingletonComponent.INSTANCE);
            engine.addEntity(entity);
        }
    }

    @Benchmark
    public void benchmarkSingletonIteration() {
        engine.update(0f);
    }
}
