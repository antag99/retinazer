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
package com.github.antag99.retinazer.generator


enum class Properties(
        val type: Bags,
        val rawPropertyName: String,
        val genericPropertyName: String = rawPropertyName
) {


    BYTE(
            type = Bags.BYTE,
            rawPropertyName = "ByteProperty"
    ),
    SHORT(
            type = Bags.SHORT,
            rawPropertyName = "ShortProperty"
    ),
    CHAR(
            type = Bags.CHAR,
            rawPropertyName = "CharProperty"
    ),
    INT(
            type = Bags.INT,
            rawPropertyName = "IntProperty"
    ),
    LONG(
            type = Bags.LONG,
            rawPropertyName = "LongProperty"
    ),
    FLOAT(
            type = Bags.FLOAT,
            rawPropertyName = "FloatProperty"
    ),
    DOUBLE(
            type = Bags.DOUBLE,
            rawPropertyName = "DoubleProperty"
    ),
    BOOLEAN(
            type = Bags.BOOLEAN,
            rawPropertyName = "BooleanProperty"
    ),
    OBJECT(
            type = Bags.OBJECT,
            rawPropertyName = "ObjectProperty",
            genericPropertyName = "ObjectProperty<T>"
    );

    val generatedCode: String =
"""${LICENSE_HEADER.trim()}
package com.github.antag99.retinazer.util;

import static java.util.Objects.requireNonNull;

/**
 *
 */
@Experimental
$GENERATED_NOTICE
public final class $genericPropertyName implements Property<${type.wrapperTypeName}, ${type.genericBagName}> {
    private String name;
    private ${type.genericBagName} bag;
${
    if (type == Bags.OBJECT) "    private Class<${type.wrapperTypeName}> type;\n" else ""
}
    public $rawPropertyName(String name${if (type == Bags.OBJECT) ", Class<${type.wrapperTypeName}> type" else ""}) {
        this.name = requireNonNull(name, "name must not be null");${if (type == Bags.OBJECT) "\n        this.type = type;" else ""}
        this.bag = new ${type.genericBagName}();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<${type.wrapperTypeName}> getType() {
        return ${if (type == Bags.OBJECT) "type" else "${type.wrapperTypeName}.class"};
    }

    @Override
    public ${type.genericBagName} getBag() {
        return bag;
    }
}
"""
}