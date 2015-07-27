package com.github.antag99.benchmarks.artemis;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;

public class RetrievalBenchmark extends ArtemisBenchmark {
    private World world;

    @Setup
    public void setup() {
        // @off
        world = new World(new WorldConfiguration()
                .setSystem(new RetrievalSystem(ComponentA.class) {})
                .setSystem(new RetrievalSystem(ComponentB.class) {})
                .setSystem(new RetrievalSystem(ComponentC.class) {})
                .setSystem(new RetrievalSystem(ComponentD.class) {})
                .setSystem(new RetrievalSystem(ComponentE.class) {})
                .setSystem(new RetrievalSystem(ComponentF.class) {})
                .setSystem(new RetrievalSystem(ComponentG.class) {})
                .setSystem(new RetrievalSystem(ComponentH.class) {})
        );
        // @on

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
    public void benchmarkRetrieval() {
        world.process();
    }
}
