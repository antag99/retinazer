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