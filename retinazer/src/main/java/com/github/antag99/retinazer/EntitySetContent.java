package com.github.antag99.retinazer;

import com.badlogic.gdx.utils.IntArray;
import com.github.antag99.retinazer.util.Mask;

final class EntitySetContent {
    public Mask entities = new Mask();
    public IntArray indices = new IntArray();
    public boolean indicesDirty = false;
}
