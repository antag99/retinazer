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

import com.beust.jcommander.Parameter;

final class WeaverParameters {

    @Parameter(names = { "--help", "-h" }, description = "Display this help message and exit")
    public boolean help = false;

    @Parameter(names = { "--classesDirectory", "-i" }, description = "Classes directory")
    public File classesDirectory = null;

    @Parameter(names = { "--outputDirectory", "-o" }, description = "Output directory", required = false)
    public File outputDirectory = null;

    @Parameter(names = { "--filter", "-f" }, description = "Pattern for internal names of classes to be included")
    public String filterPattern = null;
}
