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

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

final class WeaverMetadata implements Opcodes {
    /** The weaver instance */
    public Weaver weaver;
    /** Internal name of the class */
    public String internalName;
    /** Name of the super class; may be null if unknown. */
    public String superName;
    /** Interfaces of the class; may be null if unknown. */
    public String[] interfaces;
    /** Source of this class; may be null if unknown. */
    public ChainSource source;

    /** Component metadata for this class; may be null if not a component */
    public ComponentMetadata componentMetadata;
    /** System metadata for this class; may be null if not a system */
    public SystemMetadata systemMetadata;

    private boolean processed = false;

    public WeaverMetadata(Weaver weaver, final String internalName) {
        this.weaver = weaver;
        this.internalName = internalName;
    }

    public boolean isLoaded() {
        return source != null;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void load(byte[] classFile) {
        final ClassReader classReader = new ClassReader(classFile);
        source = new ChainSource() {
            @Override
            public void accept(ClassVisitor visitor) {
                classReader.accept(visitor, 0);
            }
        };
    }

    public void processMetadata() {
        final ClassVisitor classVisitor = new ClassVisitor(ASM5) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                WeaverMetadata.this.internalName = name;
                WeaverMetadata.this.superName = name;
                WeaverMetadata.this.interfaces = interfaces != null ? interfaces : new String[0];

                boolean isComponent = false;

                for (String interfaceName : WeaverMetadata.this.interfaces) {
                    isComponent |= interfaceName.equals(WeaverConstants.COMPONENT_NAME);
                    isComponent |= weaver.getMetadata(interfaceName).componentMetadata != null;

                    if (isComponent)
                        break;
                }

                boolean isSystem = false;

                if (!"java/lang/Object".equals(superName)) {
                    isSystem |= superName.equals(WeaverConstants.ENTITY_SYSTEM_NAME);
                    isSystem |= weaver.getMetadata(superName).systemMetadata != null;
                }

                if (isComponent) {
                    cv = (componentMetadata = new ComponentMetadata(cv));
                }

                if (isSystem) {
                    cv = (systemMetadata = new SystemMetadata(cv));
                }

                super.visit(version, access, name, signature, superName, interfaces);
            }
        };
        source.accept(classVisitor);
    }

    public void process() {
        if (processed)
            return;
        processed = true;

        List<ChainVisitor> visitors = new ArrayList<>();
        if (componentMetadata != null && !componentMetadata.isInterface) {
            visitors.add(new ComponentProcessor(componentMetadata));
            visitors.add(new AccessProcessor(weaver));

            // Generate component mapper
            WeaverMetadata metadata = weaver.createMetadata(componentMetadata.getMapperName());
            metadata.source = new MapperGenerator(componentMetadata);
            metadata.processMetadata();
            metadata.process();
        }

        if (systemMetadata != null) {
            visitors.add(new SystemProcessor(systemMetadata));
            visitors.add(new AccessProcessor(weaver));
        }

        ChainVisitor chainWriter = new ChainVisitor(ASM5);
        chainWriter.setClassVisitor(weaver.createClass(internalName));
        visitors.add(chainWriter);

        for (int i = 1; i < visitors.size(); i++)
            visitors.get(i - 1).setClassVisitor(visitors.get(i));

        source.accept(visitors.get(0));
    }
}
