package com.github.antag99.benchmarks.retinazer;

import com.github.antag99.benchmarks.JmhSettings;
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

    public Class<? extends Component>[] getComponentTypes() {
        return componentTypes;
    }

    public EngineConfig getConfig() {
        return config;
    }

    private static EngineConfig config = EngineConfig.create();

    static {
        for (Class<? extends Component> componentType : componentTypes) {
            config = config.withComponentType(componentType);
        }
    }
}
