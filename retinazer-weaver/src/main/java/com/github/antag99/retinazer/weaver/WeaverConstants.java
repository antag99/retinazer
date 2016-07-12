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
            return "com/github/antag99/retinazer/util/Mask";
        case Type.CHAR:
            throw new IllegalArgumentException(type.getClassName());
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
            throw new IllegalArgumentException(type.getClassName());
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
