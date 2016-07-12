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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Pattern;

import com.beust.jcommander.JCommander;
import com.github.antag99.retinazer.weaver.Weaver.WeaverHandler;

final class WeaverMain {
    public static void main(final String[] args) {
        final WeaverParameters parameters = new WeaverParameters();
        final JCommander commander = new JCommander(parameters, args);
        if (parameters.help) {
            commander.usage("java -jar retinazer-weaver.jar");
            return;
        }
        if (parameters.classesDirectory == null) {
            System.err.println("Please specify class directory");
            return;
        }

        if (parameters.outputDirectory == null)
            parameters.outputDirectory = parameters.classesDirectory;

        if (!parameters.classesDirectory.exists()) {
            System.err.println("Class directory does not exist: " + parameters.classesDirectory.getPath());
            return;
        }
        if (!parameters.classesDirectory.isDirectory()) {
            System.err.println("Class directory is not a directory: " + parameters.classesDirectory.getPath());
            return;
        }
        if (!parameters.outputDirectory.exists()) {
            if (!parameters.outputDirectory.mkdirs()) {
                System.err.println("Failed to create output directory: " + parameters.outputDirectory.getPath());
                return;
            }
        }
        if (!parameters.outputDirectory.isDirectory()) {
            System.err.println("Output directory is not a directory: " + parameters.outputDirectory.getPath());
            return;
        }
        final Pattern pattern = parameters.filterPattern == null ? null : Pattern.compile(parameters.filterPattern);
        final Weaver weaver = new Weaver(new WeaverHandler() {
            final File inputDirectory = parameters.classesDirectory;
            final File outputDirectory = parameters.outputDirectory;

            @Override
            public byte[] findClass(String internalName) {
                File inputFile = new File(inputDirectory, internalName + ".class");
                if (!inputFile.exists())
                    return null;
                try {
                    return Files.readAllBytes(inputFile.toPath());
                } catch (IOException ex) {
                    throw new WeaverException("Failed to access class " + internalName, ex);
                }
            }

            @Override
            public void saveClass(String internalName, byte[] classFile) {
                File outputFile = new File(outputDirectory, internalName + ".class");
                outputFile.getParentFile().mkdirs();
                try {
                    Files.write(outputFile.toPath(), classFile);
                } catch (IOException ex) {
                    throw new WeaverException("Failed to write class " + internalName, ex);
                }
            }
        });
        process(weaver, pattern, parameters.classesDirectory, parameters.classesDirectory);
    }

    private static void process(Weaver weaver, Pattern pattern, File inputDirectory, File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                process(weaver, pattern, inputDirectory, child);
            }
        } else {
            if (file.getName().endsWith(".class")) {
                String filePath = file.getAbsolutePath();
                String inputDirectoryPath = inputDirectory.getAbsolutePath();
                String relativePath = filePath.substring(inputDirectoryPath.length() + 1);
                String internalName = relativePath.substring(0, relativePath.length() - ".class".length());
                if (pattern == null || pattern.matcher(internalName).matches()) {
                    weaver.process(internalName);
                }
            }
        }
    }
}
