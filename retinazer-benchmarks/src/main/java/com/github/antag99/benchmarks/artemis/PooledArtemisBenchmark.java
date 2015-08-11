package com.github.antag99.benchmarks.artemis;

import com.artemis.Component;
import com.github.antag99.benchmarks.JmhSettings;

public abstract class PooledArtemisBenchmark extends JmhSettings {
    @SuppressWarnings("unchecked")
    private static Class<? extends Component>[] componentTypes = new Class[] {
            PooledComponentA.class,
            PooledComponentB.class,
            PooledComponentC.class,
            PooledComponentD.class,
            PooledComponentE.class,
            PooledComponentF.class,
            PooledComponentG.class,
            PooledComponentH.class
    };

    public Class<? extends Component>[] getComponentTypes() {
        return componentTypes;
    }
}
