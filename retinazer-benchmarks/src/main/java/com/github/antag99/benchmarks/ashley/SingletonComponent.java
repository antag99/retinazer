package com.github.antag99.benchmarks.ashley;

import com.badlogic.ashley.core.Component;

public final class SingletonComponent implements Component {
    public static final SingletonComponent INSTANCE = new SingletonComponent();

    private SingletonComponent() {
    }
}
