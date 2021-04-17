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
package com.github.antag99.retinazer;

import com.github.antag99.retinazer.util.Mask;

public final class EntitySetEdit {
    private final EntitySetContent content;

    EntitySetEdit(EntitySetContent content) {
        this.content = content;
    }

    public void addEntity(int entity) {
        if (!content.entities.get(entity)) {
            content.indicesDirty = true;
            content.entities.set(entity);
        }
    }

    public void addEntities(Mask entities) {
        if (!content.entities.isSupersetOf(entities)) {
            content.indicesDirty = true;
            content.entities.or(entities);
        }
    }

    public void removeEntity(int entity) {
        if (content.entities.get(entity)) {
            content.indicesDirty = true;
            content.entities.clear(entity);
        }
    }

    public void removeEntities(Mask entities) {
        if (content.entities.intersects(entities)) {
            content.indicesDirty = true;
            content.entities.andNot(entities);
        }
    }

    public void clear() {
        if (!content.entities.isEmpty()) {
            content.indicesDirty = true;
            content.entities.clear();
        }
    }
}
