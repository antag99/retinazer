package com.github.antag99.retinazer.util;

import java.util.Objects;

@Experimental
public final class ByteProperty implements Property<ByteBag, Byte> {
    private String name;
    private ByteBag bag;

    public ByteProperty(String name) {
        this.name = Objects.requireNonNull(name);
        this.bag = new ByteBag();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ByteBag getBag() {
        return bag;
    }

    @Override
    public Class<Byte> getType() {
        return Byte.TYPE;
    }
}