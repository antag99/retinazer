package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;

public class IterationBenchmark extends ArtemisBenchmark {
    private World world;

    @Setup
    public void setup() {
        world = new World();
        // @off
        world.setSystem(new IterationSystem(ComponentA.class) {});
        world.setSystem(new IterationSystem(ComponentB.class) {});
        world.setSystem(new IterationSystem(ComponentC.class) {});
        world.setSystem(new IterationSystem(ComponentD.class) {});
        world.setSystem(new IterationSystem(ComponentE.class) {});
        world.setSystem(new IterationSystem(ComponentF.class) {});
        world.setSystem(new IterationSystem(ComponentG.class) {});
        world.setSystem(new IterationSystem(ComponentH.class) {});
        // @on
        world.initialize();

        Class<? extends Component>[] componentTypes = getComponentTypes();
        for (int i = 0, n = getEntityCount(); i < n; ++i) {
            Entity entity = world.createEntity();
            // equivalent to mask = i % (2 ^ componentTypes.length)
            int mask = i & ((1 << componentTypes.length) - 1);
            for (int ii = 0, nn = componentTypes.length; ii < nn; ++ii) {
                if (((mask >> ii) & 1) == 1) {
                    entity.edit().add(newInstance(componentTypes[ii]));
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
