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

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EntityListener;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.retinazer.beam.component.Location;
import com.github.antag99.retinazer.beam.component.Position;
import com.github.antag99.retinazer.beam.component.Room;
import com.github.antag99.retinazer.beam.component.Size;

public final class SpatialSystem extends EntityProcessorSystem {
    private Engine engine;
    private Mapper<Room> mRoom;
    private Mapper<Location> mLocation;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;

    @SkipWire
    private Pool<EntitySet> sets = new Pool<EntitySet>() {
        @Override
        protected EntitySet newObject() {
            return new EntitySet();
        }
    };

    @SkipWire
    private final int partitionWidth;

    @SkipWire
    private final int partitionHeight;

    public SpatialSystem(int partitionWidth, int partitionHeight) {
        super(Family.with(Location.class, Position.class, Size.class));
        this.partitionWidth = partitionWidth;
        this.partitionHeight = partitionHeight;
    }

    @Override
    protected void initialize() {
        engine.getFamily(getFamily()).addListener(new EntityListener() {
            @Override
            public void inserted(EntitySet entities) {
            }

            @Override
            public void removed(EntitySet entities) {
                int[] items = entities.getIndices().items;
                for (int i = 0, n = entities.size(); i < n; i++) {
                    Location location = mLocation.get(items[i]);
                    for (int ii = location.partitionStartX; ii < location.partitionEndX; ii++) {
                        for (int jj = location.partitionStartY; jj < location.partitionEndY; jj++) {
                            removeEntity(location.room, ii, jj, items[i]);
                        }
                    }
                }
            }
        });
    }

    private int partitionStartX(Position position, Size size) {
        return MathUtils.floor(position.x / partitionWidth);
    }

    private int partitionEndX(Position position, Size size) {
        return MathUtils.ceil((position.x + size.width) / partitionWidth);
    }

    private int partitionStartY(Position position, Size size) {
        return MathUtils.floor(position.y / partitionHeight);
    }

    private int partitionEndY(Position position, Size size) {
        return MathUtils.ceil((position.y + size.height) / partitionHeight);
    }

    private void addEntity(int room, int x, int y, int entity) {
        EntitySet set = mRoom.get(room).getPartition(x, y);
        if (set == null) {
            set = sets.obtain();
            mRoom.get(room).setPartition(x, y, set);
        }
        set.edit().addEntity(entity);
    }

    private void removeEntity(int room, int x, int y, int entity) {
        EntitySet set = mRoom.get(room).getPartition(x, y);
        if (set == null) {
            return;
        }
        set.edit().removeEntity(entity);
        if (set.isEmpty()) {
            mRoom.get(room).setPartition(x, y, null);
            sets.free(set);
        }
    }

    @Override
    protected void process(int entity) {
        final Location location = mLocation.get(entity);
        final Position position = mPosition.get(entity);
        final Size size = mSize.get(entity);

        int oldPartitionStartX = location.partitionStartX;
        int oldPartitionStartY = location.partitionStartY;
        int oldPartitionEndX = location.partitionEndX;
        int oldPartitionEndY = location.partitionEndY;

        location.partitionStartX = partitionStartX(position, size);
        location.partitionStartY = partitionStartY(position, size);
        location.partitionEndX = partitionEndX(position, size);
        location.partitionEndY = partitionEndY(position, size);

        for (int i = oldPartitionStartX; i < oldPartitionEndX; i++) {
            for (int j = oldPartitionStartY; j < oldPartitionEndY; j++) {
                if (i < location.partitionStartX || j < location.partitionStartY ||
                        i >= location.partitionEndX || j >= location.partitionEndY) {
                    removeEntity(location.room, i, j, entity);
                }
            }
        }

        for (int i = location.partitionStartX; i < location.partitionEndX; i++) {
            for (int j = location.partitionStartY; j < location.partitionEndY; j++) {
                if (i < oldPartitionStartX || j < oldPartitionStartY ||
                        i >= oldPartitionEndX || j >= oldPartitionEndY) {
                    addEntity(location.room, i, j, entity);
                }
            }
        }
    }
}
