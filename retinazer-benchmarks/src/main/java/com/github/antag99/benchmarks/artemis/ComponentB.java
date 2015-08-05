package com.github.antag99.benchmarks.artemis;

import com.artemis.Component;
import com.artemis.annotations.PackedWeaver;
import com.artemis.annotations.PooledWeaver;

@PooledWeaver
@PackedWeaver
public final class ComponentB extends Component {
    public float a, b, c, d, e, f, g, h;
}