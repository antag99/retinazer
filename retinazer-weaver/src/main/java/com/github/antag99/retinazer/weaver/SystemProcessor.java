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
import java.util.List;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

final class SystemProcessor extends ChainVisitor implements Opcodes {
    private SystemMetadata metadata;

    private static class FlyweightField {
        public FieldNode fieldNode;
        public SystemMapper mapper;
    }

    private List<FlyweightField> flyweightFields = new ArrayList<>();
    private MethodNode setupMethod = null;

    public SystemProcessor(SystemMetadata metadata) {
        super(ASM5);

        this.metadata = metadata;
    }

    private void processMethod(MethodNode methodNode) {
        InsnList insns = methodNode.instructions;

        // Filter out debugging nodes/labels
        int count = 0;
        int maxCount = insns.size();
        AbstractInsnNode[] nodes = new AbstractInsnNode[maxCount];
        for (AbstractInsnNode node = insns.getFirst(); node != null; node = node.getNext())
            if (node.getOpcode() > 0)
                nodes[count++] = node;

        // Find mapper get() calls and create an own flyweight instance for each
        for (int i = 0; i <= count - 4; i++) {
            if (!(nodes[i + 0] instanceof VarInsnNode))
                continue;
            if (!(nodes[i + 1] instanceof FieldInsnNode))
                continue;
            if (!(nodes[i + 2] instanceof VarInsnNode))
                continue;
            if (!(nodes[i + 3] instanceof MethodInsnNode))
                continue;

            VarInsnNode loadThis = (VarInsnNode) nodes[i + 0];
            FieldInsnNode getField = (FieldInsnNode) nodes[i + 1];
            VarInsnNode loadEntity = (VarInsnNode) nodes[i + 2];
            MethodInsnNode getMethod = (MethodInsnNode) nodes[i + 3];

            if (loadThis.var != 0 || loadThis.getOpcode() != ALOAD)
                continue;

            if (!getField.owner.equals(metadata.internalName) ||
                    !getField.desc.equals("L" + WeaverConstants.MAPPER_NAME + ";") ||
                    !metadata.mappersByName.containsKey(getField.name))
                continue;
            if (loadEntity.getOpcode() != ILOAD)
                continue;
            if (!getMethod.owner.equals(WeaverConstants.MAPPER_NAME) ||
                    !getMethod.desc.equals("(I)L" + WeaverConstants.COMPONENT_NAME + ";") ||
                    !getMethod.name.equals("get"))
                continue;

            SystemMapper mapper = metadata.mappersByName.get(getField.name);

            // Add field to hold the flyweight
            String fieldName = "flyweight$" + flyweightFields.size();
            String fieldDesc = mapper.componentType.getDescriptor();
            FieldNode fieldNode = new FieldNode(ACC_PRIVATE, fieldName, fieldDesc, null, null);
            fieldNode.visitAnnotation("Lcom/github/antag99/retinazer/SkipWire;", true);
            FlyweightField flyweightField = new FlyweightField();
            flyweightField.fieldNode = fieldNode;
            flyweightField.mapper = mapper;
            flyweightFields.add(flyweightField);

            // Rewrite access to use the flyweight
            getField.owner = metadata.internalName;
            getField.name = fieldName;
            getField.desc = fieldDesc;
            insns.insert(getField, new InsnNode(DUP));
            insns.insert(loadEntity, new FieldInsnNode(PUTFIELD, mapper.componentType.getInternalName(),
                    WeaverConstants.INDEX_FIELD_NAME, WeaverConstants.INDEX_FIELD_DESC));
            insns.remove(getMethod);
        }
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        if ((access & ACC_STATIC) != 0 || (access & ACC_ABSTRACT) != 0)
            return super.visitMethod(access, name, desc, signature, exceptions);

        final MethodNode methodNode = new MethodNode(access, name, desc, signature, exceptions);

        return new MethodVisitor(ASM5, methodNode) {
            @Override
            public void visitEnd() {
                super.visitEnd();
                processMethod(methodNode);

                if (name.equals("setup") && desc.equals("()V"))
                    setupMethod = methodNode;
                else
                    methodNode.accept(SystemProcessor.super.visitMethod(access, name, desc, signature, exceptions));
            }
        };
    }

    @Override
    public void visitEnd() {
        for (FlyweightField field : flyweightFields)
            field.fieldNode.accept(cv);

        if (setupMethod == null) {
            setupMethod = new MethodNode(ACC_PROTECTED, "setup", "()V", null, null);
            setupMethod.instructions.add(new VarInsnNode(ALOAD, 0));
            setupMethod.instructions.add(new MethodInsnNode(INVOKESPECIAL, metadata.superName, "setup", "()V", false));
            setupMethod.instructions.add(new InsnNode(RETURN));
        }

        InsnList insns = new InsnList();
        for (FlyweightField field : flyweightFields) {
            String mapperName = field.mapper.componentType.getInternalName() + "$Mapper";
            insns.add(new VarInsnNode(ALOAD, 0));
            insns.add(new InsnNode(DUP));
            insns.add(new FieldInsnNode(GETFIELD, metadata.internalName,
                    field.mapper.name, "Lcom/github/antag99/retinazer/Mapper;"));
            insns.add(new TypeInsnNode(CHECKCAST, mapperName));
            insns.add(new MethodInsnNode(INVOKEVIRTUAL, mapperName,
                    "createFlyweight", "()" + field.mapper.componentType.getDescriptor(), false));
            insns.add(new FieldInsnNode(PUTFIELD, metadata.internalName,
                    field.fieldNode.name, field.fieldNode.desc));
        }

        setupMethod.instructions.insertBefore(setupMethod.instructions.getFirst(), insns);
        setupMethod.accept(cv);

        super.visitEnd();
    }
}
