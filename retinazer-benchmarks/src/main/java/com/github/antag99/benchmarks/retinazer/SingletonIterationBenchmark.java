package com.github.antag99.benchmarks.retinazer;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;
import com.github.antag99.retinazer.Mapper;

public class SingletonIterationBenchmark extends RetinazerBenchmark {
    private Engine engine;

    @Setup
    public void setup() {
        engine = new Engine(new EngineConfig()
                .addSystem(new SingletonIterationSystem()));
        Mapper<SingletonComponent> mSingleton = engine.getMapper(SingletonComponent.class);
        for (int i = 0, n = getEntityCount(); i < n; i++) {
            int entity = engine.createEntity();
            if ((i % 8) == 0)
                mSingleton.add(entity, SingletonComponent.INSTANCE);
        }
    }

    @Benchmark
    public void benchmarkSingletonIteration() {
        engine.update();
    }
}
