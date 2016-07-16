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
