package com.github.antag99.benchmarks.retinazer;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;

public class RetrievalBenchmark extends RetinazerBenchmark {
    private Engine engine;

    @Setup
    public void setup() {
        // @off
        engine = new Engine(new EngineConfig()
                .addSystem(new RetrievalSystemA())
                .addSystem(new RetrievalSystemB())
                .addSystem(new RetrievalSystemC())
                .addSystem(new RetrievalSystemD())
                .addSystem(new RetrievalSystemE())
                .addSystem(new RetrievalSystemF())
                .addSystem(new RetrievalSystemG())
                .addSystem(new RetrievalSystemH()));
        // @on

        Class<? extends Component>[] componentTypes = getComponentTypes();
        for (int i = 0, n = getEntityCount(); i < n; ++i) {
            int entity = engine.createEntity();
            // equivalent to mask = i % (2 ^ componentTypes.length)
            int mask = i & ((1 << componentTypes.length) - 1);
            for (int ii = 0, nn = componentTypes.length; ii < nn; ++ii) {
                if (((mask >> ii) & 1) == 1) {
                    engine.getMapper(componentTypes[ii]).create(entity);
                }
            }
        }

        engine.update();
    }

    @Benchmark
    public void benchmarkRetrieval() {
        engine.update();
    }
}
