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
     * structure a game using Entreri; let's use Runnable here. Iteration
     * benchmarks are pretty useless in Entreri, as they're basically the
     * same as retrieval.
     */
    private Runnable[] systems;

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
        systems = new Runnable[componentTypes.length];
        systems[0] = new RetrievalSystem(entitySystem, ComponentA.class) { };
        systems[1] = new RetrievalSystem(entitySystem, ComponentB.class) { };
        systems[2] = new RetrievalSystem(entitySystem, ComponentC.class) { };
        systems[3] = new RetrievalSystem(entitySystem, ComponentD.class) { };
        systems[4] = new RetrievalSystem(entitySystem, ComponentE.class) { };
        systems[5] = new RetrievalSystem(entitySystem, ComponentF.class) { };
        systems[6] = new RetrievalSystem(entitySystem, ComponentG.class) { };
        systems[7] = new RetrievalSystem(entitySystem, ComponentH.class) { };
        /* @on */
    }

    @Benchmark
    public void benchmarkRetrieval() {
        for (Runnable system : systems) {
            system.run();
        }
    }
}
