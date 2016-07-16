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
package com.github.antag99.retinazer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class DefaultWireResolver implements WireResolver {

    private Class<? extends Component> getParamType(Field field) {
        Type type = field.getGenericType();
        if (!(type instanceof ParameterizedType))
            return null;
        Type param = ((ParameterizedType) type).getActualTypeArguments()[0];
        if (!(param instanceof Class<?>))
            return null;
        return ((Class<?>) param).asSubclass(Component.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean wire(Engine engine, Object object, Field field) throws Throwable {
        Class<?> type = field.getType();
        if (type == Engine.class) {
            field.set(object, engine);
        } else if (EntitySystem.class.isAssignableFrom(type)) {
            field.set(object, engine.getSystem((Class<? extends EntitySystem>) type));
        } else if (Mapper.class.isAssignableFrom(type)) {
            field.set(object, engine.getMapper(getParamType(field)));
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean unwire(Engine engine, Object object, Field field) throws Throwable {
        Class<?> type = field.getType();
        if (type == Engine.class || EntitySystem.class.isAssignableFrom(type)) {
            field.set(object, null);
        } else if (Mapper.class.isAssignableFrom(type)) {
            field.set(object, null);
        } else {
            return false;
        }
        return true;
    }
}
