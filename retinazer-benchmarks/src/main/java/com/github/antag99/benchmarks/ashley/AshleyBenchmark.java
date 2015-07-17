package com.github.antag99.benchmarks.ashley;

import com.badlogic.ashley.core.Component;
import com.github.antag99.benchmarks.JmhSettings;

public abstract class AshleyBenchmark extends JmhSettings {
    // @off
    public static final class ComponentA extends Component {}
    public static final class ComponentB extends Component {}
    public static final class ComponentC extends Component {}
    public static final class ComponentD extends Component {}
    public static final class ComponentE extends Component {}
    public static final class ComponentF extends Component {}
    public static final class ComponentG extends Component {}
    public static final class ComponentH extends Component {}
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
