package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;

public class PackedSingletonIterationBenchmark extends ArtemisBenchmark {
    private World world;

    @Setup
    public void setup() {
        world = new World(new WorldConfiguration()
                .setSystem(PackedSingletonIterationSystem.class));
        for (int i = 0, n = getEntityCount(); i < n; i++) {
            Entity entity = world.createEntity();
            if ((i % 8) == 0)
                entity.edit().create(PackedSingletonComponent.class);
        }
    }

    @Benchmark
    public void benchmarkSingletonIteration() {
        world.process();
    }
}
