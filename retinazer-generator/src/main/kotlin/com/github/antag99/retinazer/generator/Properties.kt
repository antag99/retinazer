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