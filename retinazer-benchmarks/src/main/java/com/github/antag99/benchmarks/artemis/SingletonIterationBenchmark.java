package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;

public class SingletonIterationBenchmark extends ArtemisBenchmark {
    private World world;

    @Setup
    public void setup() {
        world = new World(new WorldConfiguration()
                .setSystem(SingletonIterationSystem.class));
        for (int i = 0, n = getEntityCount(); i < n; i++) {
            Entity entity = world.createEntity();
            if ((i % 8) == 0)
                entity.edit().add(SingletonComponent.INSTANCE);
        }
    }

    @Benchmark
    public void benchmarkSingletonIteration() {
        world.process();
    }
}
