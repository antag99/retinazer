/*******************************************************************************
 * Retinazer, an entity-component-system framework for Java
 *
 * Copyright (C) 2015-2016 Anton Gustafsson
 *
 * This file is part of Retinazer.
 *
 * Retinazer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Retinazer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Retinazer.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.antag99.retinazer.system;

import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.component.FloatValue;
import com.github.antag99.retinazer.component.IntegerValue;
import com.github.antag99.retinazer.component.ObjectValue;

public final class EngineStateSystem extends EntitySystem {
    private Mapper<FloatValue> mFloatValue;
    private Mapper<IntegerValue> mIntegerValue;
    private Mapper<ObjectValue> mObjectValue;

    public boolean entityExists(int entity) {
        return engine.getEntities().getMask().get(entity);
    }

    public int createEntity() {
        return engine.createEntity();
    }

    public void destroyEntity(int entity) {
        engine.destroyEntity(entity);
    }

    public float getFloat(int entity) {
        return mFloatValue.has(entity) ? mFloatValue.get(entity).value : 0f;
    }

    public void setFloat(int entity, float value) {
        if (!mFloatValue.has(entity))
            mFloatValue.create(entity);
        mFloatValue.get(entity).value = value;
    }

    public int getInteger(int entity) {
        return mIntegerValue.has(entity) ? mIntegerValue.get(entity).value : 0;
    }

    public void setInteger(int entity, int value) {
        if (!mIntegerValue.has(entity))
            mIntegerValue.create(entity);
        mIntegerValue.get(entity).value = value;
    }

    public Object getObject(int entity) {
        return mObjectValue.has(entity) ? mObjectValue.get(entity).value : null;
    }

    public void setInteger(int entity, Object value) {
        if (!mObjectValue.has(entity))
            mObjectValue.create(entity);
        mObjectValue.get(entity).value = value;
    }

}
