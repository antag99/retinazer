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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

final class SystemMetadata extends ClassVisitor implements Opcodes {
    public String internalName;

    public String superName;

    public List<SystemMapper> mappers = new ArrayList<>();

    public Map<String, SystemMapper> mappersByName = new HashMap<>();

    public SystemMetadata(ClassVisitor cv) {
        super(ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.internalName = name;
        this.superName = superName;

        super.visit(version, access, name, signature, superName, interfaces);
    }

    // Regex to extract the component type out of a mapper signature.
    private final Pattern signaturePattern = Pattern.compile(
            Pattern.quote("L" + WeaverConstants.MAPPER_NAME + "<L") + "([\\w/]+)" + Pattern.quote(";>;"));

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if ((access & ACC_STATIC) == 0 && desc.equals("L" + WeaverConstants.MAPPER_NAME + ";") && signature != null) {
            Matcher matcher = signaturePattern.matcher(signature);
            if (matcher.matches()) {
                SystemMapper mapper = new SystemMapper(name, Type.getObjectType(matcher.group(1)));
                mappers.add(mapper);
                mappersByName.put(name, mapper);
            }
        }

        return super.visitField(access, name, desc, signature, value);
    }
}
