package com.github.antag99.benchmarks.artemis;

import com.artemis.Component;
import com.github.antag99.benchmarks.JmhSettings;

public abstract class PackedArtemisBenchmark extends JmhSettings {
    @SuppressWarnings("unchecked")
    private static Class<? extends Component>[] componentTypes = new Class[] {
            PackedComponentA.class,
            PackedComponentB.class,
            PackedComponentC.class,
            PackedComponentD.class,
            PackedComponentE.class,
            PackedComponentF.class,
            PackedComponentG.class,
            PackedComponentH.class
    };

    public Class<? extends Component>[] getComponentTypes() {
        return componentTypes;
    }
}
