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

import java.util.Arrays;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

//@off
final class ComponentProcessor extends ChainVisitor implements Opcodes {
    private ComponentMetadata metadata;
    
    public ComponentProcessor(ComponentMetadata metadata) {
        super(ASM5);
        
        this.metadata = metadata;
    }
    
    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, String[] interfaces) {
        if (!"java/lang/Object".equals(superName))
            throw new WeaverException("Failed to weave " + metadata.internalName + "; cannot extend other classes");

        if ((access & ACC_FINAL) == 0)
            throw new WeaverException("Failed to weave " + metadata.internalName + "; must be final");

        if (Arrays.asList(interfaces).contains(WeaverConstants.PACKED_COMPONENT_NAME))
            throw new WeaverException("Failed to weave " + metadata.internalName + "; already packed");

        if (signature != null)
            throw new WeaverException("Failed to weave " + metadata.internalName + "; cannot use generic type arguments");

        interfaces = Arrays.copyOf(interfaces, interfaces.length);
        for (int i = 0; i < interfaces.length; i++)
            if (interfaces[i].equals(WeaverConstants.COMPONENT_NAME))
                interfaces[i] = WeaverConstants.PACKED_COMPONENT_NAME;
        
        super.visit(version, access, name, signature, superName, interfaces);

        super.visitField(ACC_PUBLIC, WeaverConstants.INDEX_FIELD_NAME, WeaverConstants.INDEX_FIELD_DESC, null, null);
        
        new MethodVisitor(ASM5, super.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)) {{
            visitCode();
            visitVarInsn(ALOAD, 0);
            visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            visitInsn(RETURN);
            visitMaxs(-1, -1);
            visitEnd();
        }};
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {

        if ((access & ACC_STATIC) > 0)
            return super.visitField(access, name, desc, signature, value);
        
        final ComponentProperty property = metadata.propertiesByName.get(name);
        
        new MethodVisitor(ASM5, super.visitMethod(ACC_PUBLIC, property.getGetterName(), property.getGetterDesc(), null, null)) {{
            visitCode();
            visitVarInsn(ALOAD, 0);
            visitFieldInsn(GETFIELD, metadata.internalName, property.getBagName(), property.getBagDesc());
            visitVarInsn(ALOAD, 0);
            visitFieldInsn(GETFIELD, metadata.internalName, WeaverConstants.INDEX_FIELD_NAME, WeaverConstants.INDEX_FIELD_DESC);
            Type erasedType = property.type.getSort() == Type.OBJECT ? Type.getObjectType("java/lang/Object") : property.type;
            visitMethodInsn(INVOKEVIRTUAL, WeaverConstants.getBagName(property.type), "get", "(I)" + erasedType.getDescriptor(), false);
            visitInsn(property.type.getOpcode(IRETURN));
            visitMaxs(-1, -1);
            visitEnd();
        }};

        new MethodVisitor(ASM5, super.visitMethod(ACC_PUBLIC, property.getSetterName(), property.getSetterDesc(), null, null)) {{
            visitCode();
            visitVarInsn(ALOAD, 0);
            visitFieldInsn(GETFIELD, metadata.internalName, property.getBagName(), property.getBagDesc());
            visitVarInsn(ALOAD, 0);
            visitFieldInsn(GETFIELD, metadata.internalName, WeaverConstants.INDEX_FIELD_NAME, WeaverConstants.INDEX_FIELD_DESC);
            visitVarInsn(property.type.getOpcode(ILOAD), 1);
            Type erasedType = property.type.getSort() == Type.OBJECT ? Type.getObjectType("java/lang/Object") : property.type;
            visitMethodInsn(INVOKEVIRTUAL, WeaverConstants.getBagName(property.type), "set", "(I" + erasedType.getDescriptor() + ")V", false);
            visitInsn(Opcodes.RETURN);
            visitMaxs(-1, -1);
            visitEnd();
        }};

        FieldVisitor storeField =  super.visitField(ACC_PUBLIC, property.getBagName(), property.getBagDesc(), null, null);
        if (storeField != null)
            storeField.visitEnd();

        return null; // Remove backing field
    }
    
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (!name.equals("<init>"))
            return super.visitMethod(access, name, desc, signature, exceptions);

        // Turn the no-arg/default constructor into a reset() method.
        if (!desc.equals("()V"))
            throw new WeaverException("Failed to weave " + metadata.internalName + "; cannot have multi-arg constructor");

        if (exceptions != null && exceptions.length > 0)
            throw new WeaverException("Failed to weave " + metadata.internalName + "; constructor cannot throw exceptions");

        return new MethodVisitor(ASM5, super.visitMethod(access, WeaverConstants.RESET_METHOD_NAME, WeaverConstants.RESET_METHOD_DESC, null, null)) {
            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                // Replace the super constructor invocation with a call to the clear() method
                if (opcode == INVOKESPECIAL && owner.equals("java/lang/Object") && name.equals("<init>") && desc.equals("()V") && !itf) {
                    super.visitMethodInsn(INVOKEVIRTUAL, metadata.internalName,
                            WeaverConstants.CLEAR_METHOD_NAME, WeaverConstants.CLEAR_METHOD_DESC, false);
                } else {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                }
            }
        };
    }
    
    @Override
    public void visitEnd() {
        // Generate clear() method
        new MethodVisitor(ASM5, super.visitMethod(ACC_PUBLIC, WeaverConstants.CLEAR_METHOD_NAME, WeaverConstants.CLEAR_METHOD_DESC, null, null)) {{
            visitCode();
            for (ComponentProperty property : metadata.properties) {
                visitVarInsn(ALOAD, 0);

                switch (property.type.getSort()) {
                case Type.BOOLEAN:
                case Type.BYTE:
                case Type.CHAR:
                case Type.SHORT:
                case Type.INT:
                    visitLdcInsn((int) 0);
                    break;
                case Type.FLOAT:
                    visitLdcInsn((float) 0);
                    break;
                case Type.DOUBLE:
                    visitLdcInsn((double) 0);
                    break;
                case Type.LONG:
                    visitLdcInsn((long) 0);
                    break;
                default:
                    visitInsn(ACONST_NULL);
                    break;
                }
                visitFieldInsn(PUTFIELD, metadata.internalName, property.name, property.type.getDescriptor());
            }
            visitInsn(RETURN);
            visitMaxs(-1, -1);
            visitEnd();
        }};

        super.visitEnd();
    }
}
