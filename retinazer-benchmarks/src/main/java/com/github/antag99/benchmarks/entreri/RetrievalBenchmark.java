package com.github.antag99.benchmarks.entreri;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.lhkbob.entreri.Component;
import com.lhkbob.entreri.Entity;
import com.lhkbob.entreri.EntitySystem;

public class RetrievalBenchmark extends EntreriBenchmark {
    private EntitySystem entitySystem;

    /*
     * The documentation provides no hints nor guidelines about how to
     * structure a game using Entreri; let's use an interface here. Iteration
     * benchmarks are pretty useless in Entreri, as they're basically the
     * same as retrieval.
     */
    private EntreriSystem[] systems;

    @Setup
    public void setup() {
        entitySystem = EntitySystem.Factory.create();

        Class<? extends Component>[] componentTypes = getComponentTypes();
        for (int i = 0, n = getEntityCount(); i < n; ++i) {
            Entity entity = entitySystem.addEntity();
            // equivalent to mask = i % (2 ^ componentTypes.length)
            int mask = i & ((1 << componentTypes.length) - 1);
            for (int ii = 0, nn = componentTypes.length; ii < nn; ++ii) {
                if (((mask >> ii) & 1) == 1) {
                    entity.add(componentTypes[ii]);
                }
            }
        }

        /* @off */
        systems = new EntreriSystem[componentTypes.length];
        systems[0] = new RetrievalSystemA(entitySystem);
        systems[1] = new RetrievalSystemB(entitySystem);
        systems[2] = new RetrievalSystemC(entitySystem);
        systems[3] = new RetrievalSystemD(entitySystem);
        systems[4] = new RetrievalSystemE(entitySystem);
        systems[5] = new RetrievalSystemF(entitySystem);
        systems[6] = new RetrievalSystemG(entitySystem);
        systems[7] = new RetrievalSystemH(entitySystem);
        /* @on */
    }

    @Benchmark
    public void benchmarkRetrieval() {
        for (EntreriSystem system : systems) {
            system.process();
        }
    }
}
