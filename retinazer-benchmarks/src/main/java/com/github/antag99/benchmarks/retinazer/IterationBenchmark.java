package com.github.antag99.benchmarks.retinazer;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.Entity;

public class IterationBenchmark extends RetinazerBenchmark {
    private Engine engine;

    @Setup
    public void setup() {
        engine = getConfig()
                // @off
                .withSystem(new IterationSystem(ComponentA.class) {})
                .withSystem(new IterationSystem(ComponentB.class) {})
                .withSystem(new IterationSystem(ComponentC.class) {})
                .withSystem(new IterationSystem(ComponentD.class) {})
                .withSystem(new IterationSystem(ComponentE.class) {})
                .withSystem(new IterationSystem(ComponentF.class) {})
                .withSystem(new IterationSystem(ComponentG.class) {})
                .withSystem(new IterationSystem(ComponentH.class) {})
                // @on
                .finish();

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
    public void benchmarkIteration() {
        engine.update();
    }
}
