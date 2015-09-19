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
import com.badlogic.gdx.utils.LongMap;
import com.github.antag99.retinazer.EntitySetListener;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.RetinazerException;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.retinazer.beam.component.Uuid;

/**
 * {@code UuidSystem} maps entities by their {@code Uuid}. It offers a
 * {@link #createEntity(long)} method for creating an entity with an uuid, and
 * the {@link #getEntity(long)} and {@link #getEntity(long, boolean)} methods
 * for retriving entities based on uuid. The user is responsible for
 * implementing a scheme for assigning uuids to entities -- typically this is
 * done server-side, which then notifies clients about the new entity.
 */
public final class UuidSystem extends EntitySystem {
    private Mapper<Uuid> mUuid;

    @SkipWire
    private LongMap<Integer> entitiesByUuid = new LongMap<>();

    @Override
    protected void initialize() {
        engine.getFamily(Family.with(Uuid.class)).getEntities().addListener(new EntitySetListener() {
            @Override
            public void inserted(IntArray entities) {
                int[] items = entities.items;
                for (int i = 0, n = entities.size; i < n; i++) {
                    long uuid = mUuid.get(items[i]).uuid;
                    if (entitiesByUuid.containsKey(uuid))
                        throw new RetinazerException("both " + items[i] +
                                " and " + entitiesByUuid.get(uuid) +
                                " have the uuid " + uuid);
                    entitiesByUuid.put(uuid, items[i]);
                }
            }

            @Override
            public void removed(IntArray entities) {
                int[] items = entities.items;
                for (int i = 0, n = entities.size; i < n; i++) {
                    long uuid = mUuid.get(items[i]).uuid;
                    Integer entity = entitiesByUuid.remove(uuid);
                    if (entity == null || entity.intValue() != items[i])
                        throw new RetinazerException("the uuid of " +
                                items[i] + " has unexpectedly changed to " +
                                uuid);
                }
            }
        });
    }

    /**
     * Gets the entity with the given uuid. If the entity does not exist, an
     * exception will be thrown. See the {@link #getEntity(long, boolean)}
     * overload for optionally retrieving an entity.
     *
     * @param uuid
     *            the uuid.
     * @return the entity with the given uuid.
     */
    public int getEntity(long uuid) {
        return getEntity(uuid, false);
    }

    /**
     * Gets the entity with the given uuid. If the entity does not exist,
     * returns {@code -1} if {@code optional} is true, otherwise an exception
     * will be thrown.
     *
     * @param uuid
     *            the uuid.
     * @param optional
     *            whether {@code -1} may be returned.
     * @return the entity with the given uuid, or {@code -1}.
     */
    public int getEntity(long uuid, boolean optional) {
        Integer entity = entitiesByUuid.get(uuid);
        if (entity == null) {
            if (!optional)
                throw new IllegalArgumentException("no entity with uuid " + uuid);
            return -1;
        }
        return entity.intValue();
    }

    /**
     * Creates an entity with the given uuid. If such an entity already exists,
     * an exception will be thrown when the new entity is inserted.
     *
     * @param uuid
     *            the uuid.
     * @return reused handle for accessing components of the created entity.
     */
    public Handle createEntity(long uuid) {
        Handle entity = engine.createEntity();
        entity.create(Uuid.class).uuid = uuid;
        return entity;
    }
}
