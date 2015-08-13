package com.github.antag99.benchmarks.artemis;

import com.artemis.Component;

public final class SingletonComponent extends Component {
    public static final SingletonComponent INSTANCE = new SingletonComponent();

    private SingletonComponent() {
    }
}
