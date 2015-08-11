package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;

public class PooledIterationBenchmark extends PooledArtemisBenchmark {
    private World world;

    @Setup
    public void setup() {
        // @off
        world = new World(new WorldConfiguration()
                .setSystem(new PooledIterationSystemA())
                .setSystem(new PooledIterationSystemB())
                .setSystem(new PooledIterationSystemC())
                .setSystem(new PooledIterationSystemD())
                .setSystem(new PooledIterationSystemE())
                .setSystem(new PooledIterationSystemF())
                .setSystem(new PooledIterationSystemG())
                .setSystem(new PooledIterationSystemH())
        );
        // @on

        Class<? extends Component>[] componentTypes = getComponentTypes();
        for (int i = 0, n = getEntityCount(); i < n; ++i) {
            Entity entity = world.createEntity();
            // equivalent to mask = i % (2 ^ componentTypes.length)
            int mask = i & ((1 << componentTypes.length) - 1);
            for (int ii = 0, nn = componentTypes.length; ii < nn; ++ii) {
                if (((mask >> ii) & 1) == 1) {
                    entity.edit().create(componentTypes[ii]);
                }
            }
        }

        world.process();
    }

    @Benchmark
    public void benchmarkIteration() {
        world.process();
    }
}
