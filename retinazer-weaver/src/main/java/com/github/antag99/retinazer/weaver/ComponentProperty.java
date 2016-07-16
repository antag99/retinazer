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
