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
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.Priority;
import com.github.antag99.retinazer.RetinazerTestCase;
import com.github.antag99.retinazer.beam.component.Collision;
import com.github.antag99.retinazer.beam.component.Location;
import com.github.antag99.retinazer.beam.component.Position;
import com.github.antag99.retinazer.beam.component.Room;
import com.github.antag99.retinazer.beam.component.Size;
import com.github.antag99.retinazer.beam.util.Category;
import com.github.antag99.retinazer.beam.util.CollisionListener;

public final class GWTCollisionSystemTest extends RetinazerTestCase {
    private static final int PARTITION_WIDTH = 32;
    private static final int PARTITION_HEIGHT = 32;

    private static final float ENTITY_WIDTH = 4f;
    private static final float ENTITY_HEIGHT = 4f;

    Engine engine;
    SpatialSystem spatialSystem;
    CollisionSystem collisionSystem;

    @Override
    protected void gwtSetUp() throws Exception {
        engine = new Engine(new EngineConfig()
                .addSystem(spatialSystem = new SpatialSystem(PARTITION_WIDTH, PARTITION_HEIGHT), Priority.HIGH)
                .addSystem(collisionSystem = new CollisionSystem(), Priority.DEFAULT));
    }

    private static int pair(int a, int b) {
        return (a << 16) | b;
    }

    public static final class CollisionListenerMock implements CollisionListener {
        public IntArray array = new IntArray();

        @Override
        public void onCollison(int a, int b) {
            array.add(pair(a, b));
        }

        public void verify(int a, int b) {
            if (!array.removeValue(pair(a, b)))
                fail("Expected collision between " + a + " and " + b);
        }

        public void verifyUnordered(int a, int b) {
            if (!array.removeValue(pair(a, b)) && !array.removeValue(pair(b, a)))
                fail("Expected collision between " + a + " and " + b);

        }

        public void verifyNoMoreInteractions() {
            if (array.size > 0) {
                fail("Unexpected collisions occured");
            }
        }
    }

    public void testCollision() {
        CollisionListenerMock mock = new CollisionListenerMock();
        collisionSystem.addCollisionListener(Category.ALL, Category.ALL, mock);

        Handle roomEntity = engine.createEntity().cpy();
        roomEntity.create(Room.class);

        Handle entityA = engine.createEntity().cpy();

        Position positionA = entityA.create(Position.class);
        positionA.x = 0f;
        positionA.y = 0f;

        Size sizeA = entityA.create(Size.class);
        sizeA.set(ENTITY_WIDTH, ENTITY_HEIGHT);

        Location locationA = entityA.create(Location.class);
        locationA.room(roomEntity.idx());

        Collision collisionA = entityA.create(Collision.class);
        collisionA.collidesWith(Category.ALL);

        Handle entityB = engine.createEntity().cpy();

        Position positionB = entityB.create(Position.class);
        positionB.x = 0f;
        positionB.y = 0f;

        Size sizeB = entityB.create(Size.class);
        sizeB.set(ENTITY_WIDTH, ENTITY_HEIGHT);

        Location locationB = entityB.create(Location.class);
        locationB.room(roomEntity.idx());

        Collision collisionB = entityB.create(Collision.class);
        collisionB.collidesWith(Category.ALL);

        engine.update();

        mock.verifyUnordered(entityA.idx(), entityB.idx());
        mock.verifyNoMoreInteractions();
    }
}
