package com.github.antag99.benchmarks.ashley;

import com.badlogic.ashley.core.Component;
import com.github.antag99.benchmarks.JmhSettings;

public abstract class AshleyBenchmark extends JmhSettings {
    // @off
    public static final class ComponentA implements Component {}
    public static final class ComponentB implements Component {}
    public static final class ComponentC implements Component {}
    public static final class ComponentD implements Component {}
    public static final class ComponentE implements Component {}
    public static final class ComponentF implements Component {}
    public static final class ComponentG implements Component {}
    public static final class ComponentH implements Component {}
    // @on

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
