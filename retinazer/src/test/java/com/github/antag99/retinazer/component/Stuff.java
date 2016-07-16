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
package com.github.antag99.retinazer.component;

import com.github.antag99.retinazer.Component;

public final class Stuff implements Component {
    public byte myByte = 42;
    public short myShort = 13;
    public char myChar = 3;
    public int myInt = 4;
    public long myLong = 42424242;
    public float myFloat = 3.141592653f;
    public double myDouble = 0.333333333333d;
    public boolean myBoolean = true;

    public void shuffle() {
        myShort = myByte;
        myByte++;
        myInt = (int) (myLong & 0xff00);
        myFloat *= 42;
        myDouble = 1f / myDouble;
        myBoolean = !myBoolean ^ (myBoolean && myInt == 0);
        myLong = myBoolean ? myLong + 42 : myLong - 42;
        myChar = 'Z';
        myChar = 'Z';
        myChar = 'Z';
    }

    public double square() {
        return myDouble * myDouble;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("myByte=").append(myByte).append("\n")
                .append("myShort=").append(myShort).append("\n")
                .append("myChar=").append(myChar).append("\n")
                .append("myInt=").append(myInt).append("\n")
                .append("myLong=").append(myLong).append("\n")
                .append("myFloat=").append(myFloat).append("\n")
                .append("myDouble=").append(myDouble).append("\n")
                .append("myBoolean=").append(myBoolean).append("\n")
                .toString();
    }
}
