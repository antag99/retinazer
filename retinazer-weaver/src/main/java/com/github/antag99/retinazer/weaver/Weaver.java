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

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import static java.util.Objects.requireNonNull;

public final class Weaver {
    private final WeaverHandler handler;
    private final Map<String, WeaverMetadata> metadatas = new HashMap<>();

    public Weaver(WeaverHandler handler) {
        this.handler = requireNonNull(handler, "handler must not be null");
    }

    /**
     * Processes the class with the given internal name.
     *
     * @param internalName
     *            Internal name of the class.
     * @throws WeaverException
     *             If weaving fails.
     */
    public void process(String internalName) throws WeaverException {
        WeaverMetadata metadata = getMetadata(internalName);
        if (!metadata.isLoaded())
            throw new WeaverException("Class not found: " + internalName);
        metadata.process();
    }

    /**
     * Returns a class visitor that creates the given class.
     *
     * @param internalName
     *            the internal name of the class.
     * @return
     *         the class visitor.
     */
    ClassVisitor createClass(final String internalName) {
        return new ClassVisitor(Opcodes.ASM5, new ClassWriter(ClassWriter.COMPUTE_FRAMES)) {
            @Override
            public void visitEnd() {
                super.visitEnd();

                handler.saveClass(internalName, ((ClassWriter) cv).toByteArray());
            }
        };
    }

    WeaverMetadata createMetadata(String internalName) {
        WeaverMetadata metadata = new WeaverMetadata(this, internalName);
        metadatas.put(internalName, metadata);
        return metadata;
    }

    WeaverMetadata getMetadata(String internalName) {
        WeaverMetadata metadata = metadatas.get(internalName);
        if (metadata == null && !metadatas.containsKey(internalName)) {
            metadata = new WeaverMetadata(this, internalName);
            byte[] classFile = handler.findClass(internalName);
            if (classFile != null) {
                metadata.load(classFile);
                metadata.processMetadata();
            }
            metadatas.put(internalName, metadata);
        }
        return metadata;
    }

    /**
     * Finds classes, and saves the results of weaving operations.
     */
    public interface WeaverHandler {

        /**
         * Gets the bytecode of the class of the given name.
         * 
         * @param internalName
         *            Internal name of the class, e.g. {@code java/lang/Object}.
         * @return
         *         Bytecode of the class; may be null if not found.
         */
        byte[] findClass(String internalName);

        /**
         * Saves the given class.
         *
         * @param internalName
         *            Internal name of the class, e.g. {@code java/lang/Object}.
         * @param classFile
         *            Bytecode of the class.
         */
        void saveClass(String internalName, byte[] classFile);
    }
}
