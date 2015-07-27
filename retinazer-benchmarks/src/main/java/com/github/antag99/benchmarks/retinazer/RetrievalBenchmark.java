package com.github.antag99.benchmarks.retinazer;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.Entity;

public class RetrievalBenchmark extends RetinazerBenchmark {
    private Engine engine;

    @Setup
    public void setup() {
        // @off
        engine = getConfig()
                .withSystem(new RetrievalSystem(ComponentA.class) {})
                .withSystem(new RetrievalSystem(ComponentB.class) {})
                .withSystem(new RetrievalSystem(ComponentC.class) {})
                .withSystem(new RetrievalSystem(ComponentD.class) {})
                .withSystem(new RetrievalSystem(ComponentE.class) {})
                .withSystem(new RetrievalSystem(ComponentF.class) {})
                .withSystem(new RetrievalSystem(ComponentG.class) {})
                .withSystem(new RetrievalSystem(ComponentH.class) {})
                .finish();
        // @on

        Class<? extends Component>[] componentTypes = getComponentTypes();
        for (int i = 0, n = getEntityCount(); i < n; ++i) {
            Entity entity = engine.createEntity();
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
    public void benchmarkRetrieval() {
        engine.update();
    }
}
