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
package com.github.antag99.retinazer.generator

import java.io.File

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject

@Mojo(name = "retinazer-generator", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
class GeneratorMojo : AbstractMojo() {

    @Parameter(property = "project", readonly = true, required = true)
    lateinit var project: MavenProject

    override fun execute() {
        if (project.artifactId != "retinazer-parent")
            return;

        log.info("Generating bags...")
        for (bag in Bags.values()) {
            File(project.basedir, "retinazer/src/main/java/com/github/antag99/retinazer/util/" +
                    "${bag.rawBagName}.java").writeText(bag.generatedCode, Charsets.UTF_8)
            File(project.basedir, "retinazer/src/test/java/com/github/antag99/retinazer/util/" +
                    "${bag.rawBagName}Test.java").writeText(bag.generatedTestCode, Charsets.UTF_8)
        }
        log.info("Generating properties...")
        for (property in Properties.values()) {
            File(project.basedir, "retinazer/src/main/java/com/github/antag99/retinazer/util/" +
                    "${property.rawPropertyName}.java").writeText(property.generatedCode, Charsets.UTF_8)
        }
        log.info("Done.")
    }
}