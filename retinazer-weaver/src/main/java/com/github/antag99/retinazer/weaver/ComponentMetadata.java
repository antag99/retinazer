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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

final class ComponentMetadata extends ClassVisitor implements Opcodes {

    /**
     * Internal name of this component type.
     */
    public String internalName;

    /**
     * Properties of this component type.
     */
    public List<ComponentProperty> properties = new ArrayList<>();

    /**
     * Properties of this component type, by name.
     */
    public Map<String, ComponentProperty> propertiesByName = new HashMap<>();

    public boolean isInterface;

    public ComponentMetadata(ClassVisitor cv) {
        super(ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.internalName = name;
        this.isInterface = (access & ACC_INTERFACE) != 0;

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if ((access & ACC_STATIC) == 0) {
            if ((access & ACC_FINAL) != 0)
                throw new WeaverException("Failed to weave " + internalName + "; cannot declare final property " + name);
            ComponentProperty property = new ComponentProperty(Type.getType(desc), name);
            properties.add(property);
            propertiesByName.put(name, property);
        }

        return super.visitField(access, name, desc, signature, value);
    }

    public String getMapperName() {
        return internalName + "$Mapper";
    }
}
