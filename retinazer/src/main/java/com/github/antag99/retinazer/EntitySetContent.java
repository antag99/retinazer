package com.github.antag99.retinazer;

import com.github.antag99.retinazer.util.IntBag;
import com.github.antag99.retinazer.util.Mask;

final class EntitySetContent {
    public Mask entities = new Mask();
    public IntBag indices = new IntBag();
    public boolean indicesDirty = false;
}
