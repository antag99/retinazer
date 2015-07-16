package com.github.antag99.benchmarks.retinazer;

import com.github.antag99.benchmarks.JmhSettings;
import com.github.antag99.benchmarks.retinazer.components.ComponentA;
import com.github.antag99.benchmarks.retinazer.components.ComponentB;
import com.github.antag99.benchmarks.retinazer.components.ComponentC;
import com.github.antag99.benchmarks.retinazer.components.ComponentD;
import com.github.antag99.benchmarks.retinazer.components.ComponentE;
import com.github.antag99.benchmarks.retinazer.components.ComponentF;
import com.github.antag99.benchmarks.retinazer.components.ComponentG;
import com.github.antag99.benchmarks.retinazer.components.ComponentH;
import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.EngineConfig;

public abstract class RetinazerBenchmark extends JmhSettings {
    @SuppressWarnings("unchecked")
    private static Class<? extends Component>[] componentTypes = new Class[] {
            ComponentA.class,
            ComponentB.class,
            ComponentC.class,
            ComponentD.class,
            ComponentE.class,
            ComponentF.class,
            ComponentG.class,
            ComponentH.class
    };

    private static EngineConfig config = EngineConfig.create();

    static {
        for (Class<? extends Component> componentType : componentTypes) {
            config = config.withComponentType(componentType);
        }
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.cast(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new IllegalArgumentException("Failed to instantiate object", ex);
        }
    }

    public Class<? extends Component>[] getComponentTypes() {
        return componentTypes;
    }

    public EngineConfig getConfig() {
        return config;
    }
}
