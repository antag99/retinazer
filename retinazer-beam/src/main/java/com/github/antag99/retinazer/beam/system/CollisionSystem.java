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

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.retinazer.beam.component.Collision;
import com.github.antag99.retinazer.beam.component.Position;
import com.github.antag99.retinazer.beam.component.Size;
import com.github.antag99.retinazer.beam.component.Spatial;
import com.github.antag99.retinazer.beam.util.Category;
import com.github.antag99.retinazer.beam.util.CollisionListener;

public final class CollisionSystem extends EntityProcessorSystem {
    private Mapper<Spatial> mSpatial;

    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Collision> mCollision;

    private static final class CollisionListenerRegistration {
        Category a;
        Category b;
        CollisionListener listener;
    }

    @SkipWire
    private Array<CollisionListenerRegistration> collisionListeners = new Array<>();

    public CollisionSystem() {
        super(Family.with(Spatial.class));
    }

    public void addCollisionListener(Category a, Category b, CollisionListener listener) {
        CollisionListenerRegistration registration = new CollisionListenerRegistration();
        registration.listener = listener;
        registration.a = a;
        registration.b = b;
        collisionListeners.add(registration);
    }

    public void removeCollisionListener(CollisionListener listener) {
        for (int i = 0; i < collisionListeners.size; i++) {
            if (collisionListeners.get(i).listener == listener) {
                collisionListeners.removeIndex(i--);
            }
        }
    }

    @Override
    protected void process(int entity) {
        Spatial spatial = mSpatial.get(entity);

        IntArray indices = spatial.entities.getIndices();
        int[] items = indices.items;

        for (int i = 0, n = indices.size; i < n; i++) {
            int a = items[i];
            Position aPosition = mPosition.get(a);
            Size aSize = mSize.get(a);
            Collision aCollision = mCollision.get(a);

            if (aCollision == null)
                continue;

            for (int j = 0; j < i; j++) {
                int b = items[j];
                Position bPosition = mPosition.get(b);
                Size bSize = mSize.get(b);
                Collision bCollision = mCollision.get(b);

                if (bCollision == null)
                    continue;

                float aX = aPosition.x;
                float aY = aPosition.y;
                float aW = aSize.width;
                float aH = aSize.height;

                float bX = bPosition.x;
                float bY = bPosition.y;
                float bW = bSize.width;
                float bH = bSize.height;

                if (aX + aW > bX && aY + aH > bY && bX + bW > aX && bY + bH > aY) {
                    if (aCollision.category.intersects(bCollision.collidesWith) ||
                            bCollision.category.intersects(aCollision.collidesWith)) {
                        Object[] listeners = collisionListeners.items;
                        for (int ii = 0, nn = collisionListeners.size; ii < nn; ii++) {
                            CollisionListenerRegistration registration = (CollisionListenerRegistration) listeners[ii];
                            if (aCollision.category.contains(registration.a) && bCollision.category.contains(registration.b)) {
                                registration.listener.onCollison(a, b);
                            } else if (aCollision.category.contains(registration.b) && bCollision.category.contains(registration.a)) {
                                registration.listener.onCollison(b, a);
                            }
                        }
                    }
                }
            }
        }

    }
}
