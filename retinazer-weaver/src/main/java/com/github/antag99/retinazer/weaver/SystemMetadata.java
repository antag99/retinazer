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
