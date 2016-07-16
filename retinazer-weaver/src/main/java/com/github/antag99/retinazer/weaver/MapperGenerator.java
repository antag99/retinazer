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

import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

final class MapperGenerator implements ChainSource, Opcodes {
    private ComponentMetadata metadata;

    public MapperGenerator(ComponentMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public void accept(ClassVisitor visitor) {
        visitor.visit(V1_7, ACC_PUBLIC + ACC_FINAL + ACC_SYNTHETIC, metadata.getMapperName(),
                "Lcom/github/antag99/retinazer/PackedMapper<L" + metadata.getMapperName() + ";>;",
                "com/github/antag99/retinazer/PackedMapper", null);
        for (ComponentProperty property : metadata.properties) {
            FieldVisitor propertyField = visitor.visitField(ACC_PRIVATE + ACC_FINAL,
                    property.getMetadataName(), property.getMetadataDesc(), null, null);
            if (propertyField != null)
                propertyField.visitEnd();
        }

        FieldVisitor flyweightField = visitor.visitField(ACC_PRIVATE + ACC_FINAL, "flyweight", "L" + metadata.internalName + ";", null, null);
        if (flyweightField != null)
            flyweightField.visitEnd();

        // @off: Formatter mangles the elegant method syntax

        new MethodVisitor(ASM5, visitor.visitMethod(ACC_PUBLIC, "<init>", "(Lcom/github/antag99/retinazer/Engine;I)V", null, null)) {{
            visitCode();
            visitVarInsn(ALOAD, 0);
            visitVarInsn(ALOAD, 1);
            visitLdcInsn(Type.getObjectType(metadata.internalName));
            visitVarInsn(ILOAD, 2);
            visitMethodInsn(Opcodes.INVOKESPECIAL, "com/github/antag99/retinazer/PackedMapper",
                    "<init>", "(Lcom/github/antag99/retinazer/Engine;Ljava/lang/Class;I)V", false);

            for (ComponentProperty property : metadata.properties) {
                visitVarInsn(ALOAD, 0);
                visitTypeInsn(NEW, WeaverConstants.getMetadataName(property.type));
                visitInsn(Opcodes.DUP);
                visitLdcInsn(property.name);
                if (property.type.getSort() == Type.OBJECT) {
                    visitLdcInsn(property.type);
                    visitMethodInsn(INVOKESPECIAL, WeaverConstants.getMetadataName(property.type),
                            "<init>", "(Ljava/lang/String;Ljava/lang/Class;)V", false);
                } else {
                    visitMethodInsn(INVOKESPECIAL, WeaverConstants.getMetadataName(property.type),
                            "<init>", "(Ljava/lang/String;)V", false);
                }
                visitFieldInsn(PUTFIELD, metadata.getMapperName(), property.getMetadataName(), property.getMetadataDesc());
            }

            visitVarInsn(ALOAD, 0);
            visitInsn(DUP);
            visitMethodInsn(INVOKEVIRTUAL, metadata.getMapperName(), "createFlyweight", "()L" + metadata.internalName + ";", false);
            visitFieldInsn(PUTFIELD, metadata.getMapperName(), "flyweight", "L" + metadata.internalName + ";");
            visitInsn(RETURN);
            visitMaxs(0, 0);
            visitEnd();
        }};

        new MethodVisitor(ASM5, visitor.visitMethod(ACC_PUBLIC, "createFlyweight", "()L" + metadata.internalName + ";", null, null)) {{
            visitCode();
            visitTypeInsn(NEW, metadata.internalName);
            visitInsn(DUP);
            visitMethodInsn(INVOKESPECIAL, metadata.internalName, "<init>", "()V", false);

            for (ComponentProperty property : metadata.properties) {
                visitInsn(DUP);
                visitVarInsn(ALOAD, 0);
                visitFieldInsn(GETFIELD, metadata.getMapperName(), property.getMetadataName(), property.getMetadataDesc());
                visitMethodInsn(INVOKEVIRTUAL, WeaverConstants.getMetadataName(property.type), "getBag", "()" + property.getBagDesc(), false);
                visitFieldInsn(PUTFIELD, metadata.internalName, property.getBagName(), property.getBagDesc());
            }

            visitInsn(ARETURN);

            visitMaxs(0, 0);
            visitEnd();
        }};

        new MethodVisitor(ASM5, visitor.visitMethod(ACC_PUBLIC, "create", "(I)L" + metadata.internalName + ";", null, null)) {{
            visitCode();
            visitVarInsn(ALOAD, 0);
            visitVarInsn(ILOAD, 1);
            visitMethodInsn(INVOKEVIRTUAL, "com/github/antag99/retinazer/Mapper", "checkCreate", "(I)V", false);
            visitVarInsn(ALOAD, 0);
            visitVarInsn(ILOAD, 1);
            visitMethodInsn(INVOKEVIRTUAL, metadata.getMapperName(), "get", "(I)L" + metadata.internalName + ";", false);
            visitInsn(DUP);
            visitMethodInsn(INVOKEVIRTUAL, metadata.internalName, WeaverConstants.RESET_METHOD_NAME, WeaverConstants.RESET_METHOD_DESC, false);
            visitInsn(ARETURN);
            visitMaxs(0, 0);
            visitEnd();
        }};

        new MethodVisitor(ASM5, visitor.visitMethod(ACC_PUBLIC + ACC_SYNTHETIC + ACC_BRIDGE, "create",
                "(I)Lcom/github/antag99/retinazer/Component;", null, null)) {{
            visitCode();
            visitVarInsn(ALOAD, 0);
            visitVarInsn(ILOAD, 1);
            visitMethodInsn(INVOKEVIRTUAL, metadata.getMapperName(), "create", "(I)L" + metadata.internalName + ";", false);
            visitInsn(ARETURN);
            visitMaxs(0, 0);
            visitEnd();
        }};

        new MethodVisitor(ASM5, visitor.visitMethod(ACC_PUBLIC,"get","(I)L" + metadata.internalName + ";",null,null)) {{
            visitCode();
            visitVarInsn(ALOAD, 0);
            visitFieldInsn(GETFIELD, metadata.getMapperName(), "flyweight", "L" + metadata.internalName + ";");
            visitInsn(DUP);
            visitVarInsn(ILOAD, 1);
            visitFieldInsn(PUTFIELD, metadata.internalName, WeaverConstants.INDEX_FIELD_NAME, WeaverConstants.INDEX_FIELD_DESC);
            visitInsn(ARETURN);
            visitMaxs(0, 0);
            visitEnd();
        }};
    
        new MethodVisitor(ASM5, visitor.visitMethod(ACC_PUBLIC+ACC_BRIDGE+ACC_SYNTHETIC,"get","(I)Lcom/github/antag99/retinazer/Component;",null,null)) {{
            visitCode();
            visitVarInsn(ALOAD, 0);
            visitVarInsn(ILOAD, 1);
            visitMethodInsn(INVOKEVIRTUAL, metadata.getMapperName(), "get", "(I)L" + metadata.internalName + ";", false);
            visitInsn(ARETURN);
            visitMaxs(0, 0);
            visitEnd();
        }};
    
        new MethodVisitor(ASM5, visitor.visitMethod(ACC_PUBLIC,"getProperties","()[Lcom/github/antag99/retinazer/util/Property;",null,null)) {{
            visitCode();
            visitLdcInsn(metadata.properties.size());
            visitTypeInsn(ANEWARRAY, "com/github/antag99/retinazer/util/Property");

            List<ComponentProperty> properties = metadata.properties;
            for (int i = 0, n = properties.size(); i < n; i++) {
                ComponentProperty property = properties.get(i);
                visitInsn(DUP);
                visitLdcInsn(i);
                visitVarInsn(ALOAD, 0);
                visitFieldInsn(GETFIELD, metadata.getMapperName(), property.getMetadataName(), property.getMetadataDesc());
                visitInsn(AASTORE);
            }

            visitInsn(ARETURN);
            visitMaxs(0, 0);
            visitEnd();
        }};
    
        new MethodVisitor(ASM5, visitor.visitMethod(ACC_PUBLIC, "applyComponentChanges", "()V", null, null)) {{
            visitCode();
            visitVarInsn(ALOAD, 0);
            visitFieldInsn(GETFIELD, "com/github/antag99/retinazer/Mapper", "componentsMask", "Lcom/github/antag99/retinazer/util/Mask;");
            visitVarInsn(ASTORE, 1);
            visitVarInsn(ALOAD, 0);
            visitFieldInsn(GETFIELD, "com/github/antag99/retinazer/Mapper", "removeMask", "Lcom/github/antag99/retinazer/util/Mask;");
            visitVarInsn(ASTORE, 2);
            for (ComponentProperty property : metadata.properties) {
                visitVarInsn(ALOAD, 0);
                visitFieldInsn(GETFIELD, metadata.getMapperName(), property.getMetadataName(), property.getMetadataDesc());
                visitMethodInsn(INVOKEVIRTUAL, WeaverConstants.getMetadataName(property.type), "getBag", "()" + property.getBagDesc(), false);
                visitVarInsn(ALOAD, 2);
                visitMethodInsn(INVOKEVIRTUAL, WeaverConstants.getBagName(property.type),
                        "clear", "(Lcom/github/antag99/retinazer/util/Mask;)V", false);
            }
            visitVarInsn(ALOAD, 1);
            visitVarInsn(ALOAD, 2);
            visitMethodInsn(INVOKEVIRTUAL, "com/github/antag99/retinazer/util/Mask", "andNot", "(Lcom/github/antag99/retinazer/util/Mask;)V", false);
            visitInsn(RETURN);
            visitMaxs(0, 0);
            visitEnd();
        }};

        visitor.visitEnd();
    }
}
