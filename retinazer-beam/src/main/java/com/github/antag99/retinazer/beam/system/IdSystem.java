/*******************************************************************************
 * Copyright (C) 2015 Anton Gustafsson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.github.antag99.retinazer.beam.system;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EntitySetListener;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;
import com.github.antag99.retinazer.Wire.Ignore;
import com.github.antag99.retinazer.beam.component.Id;

@Wire
public final class IdSystem extends EntitySystem {
    private Engine engine;
    private Mapper<Id> mId;

    @Ignore
    private ObjectIntMap<String> idToEntity = new ObjectIntMap<>();

    @Override
    protected void initialize() {
        engine.getEntitiesFor(Family.with(Id.class)).addListener(new EntitySetListener() {
            @Override
            public void inserted(IntArray entities) {
                int[] items = entities.items;
                for (int i = 0, n = entities.size; i < n; i++) {
                    String id = mId.get(items[i]).id;
                    if (id == null) {
                        throw new IllegalStateException("Entity " + items[i] +
                                " cannot have a null ID");
                    }
                    int old = idToEntity.get(id, -1);
                    if (old >= 0) {
                        throw new IllegalStateException("Entity " + items[i] +
                                " cannot have the same ID as " + old + " (" + id + ")");
                    }
                    idToEntity.put(id, items[i]);
                }
            }

            @Override
            public void removed(IntArray entities) {
                int[] items = entities.items;
                for (int i = 0, n = entities.size; i < n; i++) {
                    int removed = idToEntity.remove(mId.get(items[i]).id, -1);
                    if (removed != items[i]) {
                        idToEntity.put(mId.get(items[i]).id, removed);

                        throw new IllegalStateException("The ID of " + items[i] +
                                " has been tampered with (current ID is " +
                                mId.get(items[i]).id + ")");
                    }
                }
            }
        });
    }

    public int getEntity(String id) {
        return getEntity(id, false);
    }

    public int getEntity(String id, boolean optional) {
        int entity = idToEntity.get(id, -1);
        if (entity == -1 && !optional) {
            throw new IllegalArgumentException("No such entity with id: " + id);
        }
        return entity;
    }
}
