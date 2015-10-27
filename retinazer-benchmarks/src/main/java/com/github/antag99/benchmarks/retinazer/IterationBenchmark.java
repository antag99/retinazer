package com.github.antag99.benchmarks.retinazer;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;
import com.github.antag99.retinazer.Handle;

public class IterationBenchmark extends RetinazerBenchmark {
    private Engine engine;
    private Handle handle;

    @Setup
    public void setup() {
        // @off
        engine = new Engine(new EngineConfig()
                .addSystem(new IterationSystemA())
                .addSystem(new IterationSystemB())
                .addSystem(new IterationSystemC())
                .addSystem(new IterationSystemD())
                .addSystem(new IterationSystemE())
                .addSystem(new IterationSystemF())
                .addSystem(new IterationSystemG())
                .addSystem(new IterationSystemH()));
        // @on
        handle = engine.createHandle();

        Class<? extends Component>[] componentTypes = getComponentTypes();
        for (int i = 0, n = getEntityCount(); i < n; ++i) {
            Handle entity = handle.idx(engine.createEntity());
            // equivalent to mask = i % (2 ^ componentTypes.length)
            int mask = i & ((1 << componentTypes.length) - 1);
            for (int ii = 0, nn = componentTypes.length; ii < nn; ++ii) {
                if (((mask >> ii) & 1) == 1) {
                    entity.add(newInstance(componentTypes[ii]));
                }
            }
        }

        engine.update();
    }

    @Benchmark
    public void benchmarkIteration() {
        engine.update();
    }
}
