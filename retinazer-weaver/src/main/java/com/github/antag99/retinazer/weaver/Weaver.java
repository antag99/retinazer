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
