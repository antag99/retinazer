package com.github.antag99.benchmarks.retinazer;

import com.github.antag99.retinazer.Component;

public final class SingletonComponent implements Component {
    public static final SingletonComponent INSTANCE = new SingletonComponent();

    private SingletonComponent() {
    }
}
