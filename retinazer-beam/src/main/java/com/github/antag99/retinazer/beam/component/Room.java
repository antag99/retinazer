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
package com.github.antag99.retinazer.beam.component;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.github.antag99.retinazer.Component;

/**
 * Tracks the properties of a room. This includes the active partitions.
 *
 * Partitions are dynamically created when an entity enters a new region of the room.
 */
public final class Room implements Component {

    /**
     * Mapping of partition position (in partition coordinates) to partition
     * entity. The partition entity in turn has the {@link Spatial} component,
     * which stores the entities inside the partition. The benefit of using
     * entities for partitions is that users can extend partitions with support
     * for storing geometry or other clever stuff.
     */
    public ObjectIntMap<GridPoint2> partitions = new ObjectIntMap<>();
}
