package com.github.antag99.benchmarks.ashley;

import com.badlogic.ashley.core.Component;
import com.github.antag99.benchmarks.JmhSettings;

public abstract class AshleyBenchmark extends JmhSettings {
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
}
