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
package com.github.antag99.retinazer.weaver;

import org.objectweb.asm.Type;

final class WeaverConstants {
    private WeaverConstants() {
    }

    public static final String COMPONENT_NAME = "com/github/antag99/retinazer/Component";
    public static final String PACKED_COMPONENT_NAME = "com/github/antag99/retinazer/PackedComponent";

    public static final String ENTITY_SYSTEM_NAME = "com/github/antag99/retinazer/EntitySystem";

    public static final String MAPPER_NAME = "com/github/antag99/retinazer/Mapper";
    public static final String PLAIN_MAPPER_NAME = "com/github/antag99/retinazer/PlainMapper";
    public static final String PACKED_MAPPER_NAME = "com/github/antag99/retinazer/PackedMapper";

    public static final String RESET_METHOD_NAME = "reset";
    public static final String RESET_METHOD_DESC = "()V";

    public static final String CLEAR_METHOD_NAME = "clear";
    public static final String CLEAR_METHOD_DESC = "()V";

    public static final String INDEX_FIELD_NAME = "$index";
    public static final String INDEX_FIELD_DESC = "I";

    public static String getBagName(Type type) {
        switch (type.getSort()) {
        case Type.VOID:
            throw new IllegalArgumentException(type.getClassName());
        case Type.BOOLEAN:
            return "com/github/antag99/retinazer/util/BooleanBag";
        case Type.CHAR:
            return "com/github/antag99/retinazer/util/CharBag";
        case Type.BYTE:
            return "com/github/antag99/retinazer/util/ByteBag";
        case Type.SHORT:
            return "com/github/antag99/retinazer/util/ShortBag";
        case Type.INT:
            return "com/github/antag99/retinazer/util/IntBag";
        case Type.FLOAT:
            return "com/github/antag99/retinazer/util/FloatBag";
        case Type.LONG:
            return "com/github/antag99/retinazer/util/LongBag";
        case Type.DOUBLE:
            return "com/github/antag99/retinazer/util/DoubleBag";
        default:
            return "com/github/antag99/retinazer/util/Bag";
        }
    }

    public static String getMetadataName(Type type) {
        switch (type.getSort()) {
        case Type.VOID:
            throw new IllegalArgumentException(type.getClassName());
        case Type.BOOLEAN:
            return "com/github/antag99/retinazer/util/BooleanProperty";
        case Type.CHAR:
            return "com/github/antag99/retinazer/util/CharProperty";
        case Type.BYTE:
            return "com/github/antag99/retinazer/util/ByteProperty";
        case Type.SHORT:
            return "com/github/antag99/retinazer/util/ShortProperty";
        case Type.INT:
            return "com/github/antag99/retinazer/util/IntProperty";
        case Type.FLOAT:
            return "com/github/antag99/retinazer/util/FloatProperty";
        case Type.LONG:
            return "com/github/antag99/retinazer/util/LongProperty";
        case Type.DOUBLE:
            return "com/github/antag99/retinazer/util/DoubleProperty";
        default:
            return "com/github/antag99/retinazer/util/ObjectProperty";
        }
    }

}
