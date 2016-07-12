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

final class ComponentProperty {
    /** Type of this property */
    public final Type type;

    /** Name of this property */
    public final String name;

    public ComponentProperty(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getGetterName() {
        return "$get_" + name;
    }

    public String getGetterDesc() {
        return "()" + type.getDescriptor();
    }

    public String getSetterName() {
        return "$set_" + name;
    }

    public String getSetterDesc() {
        return "(" + type.getDescriptor() + ")V";
    }

    public String getMetadataName() {
        return "$metadata_" + name;
    }

    public String getMetadataDesc() {
        return "L" + WeaverConstants.getMetadataName(type) + ";";
    }

    public String getBagName() {
        return "$bag_" + name;
    }

    public String getBagDesc() {
        return "L" + WeaverConstants.getBagName(type) + ";";
    }
}
