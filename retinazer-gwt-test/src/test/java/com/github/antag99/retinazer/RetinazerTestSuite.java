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
package com.github.antag99.retinazer;

import java.util.Arrays;

import com.github.antag99.retinazer.utils.GWTBagTest;
import com.github.antag99.retinazer.utils.GWTByteBagTest;
import com.github.antag99.retinazer.utils.GWTDoubleBagTest;
import com.github.antag99.retinazer.utils.GWTFloatBagTest;
import com.github.antag99.retinazer.utils.GWTIntBagTest;
import com.github.antag99.retinazer.utils.GWTLongBagTest;
import com.github.antag99.retinazer.utils.GWTMaskTest;
import com.github.antag99.retinazer.utils.GWTShortBagTest;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.junit.tools.GWTTestSuite;

import junit.framework.TestCase;

public class RetinazerTestSuite extends TestCase {
    public static GWTTestSuite suite() {
        GWTTestSuite suite = new GWTTestSuite("retinazer gwt tests");
        for (Class<? extends TestCase> tc : Arrays.<Class<? extends GWTTestCase>> asList(
                GWTDependencyTest.class,
                GWTEngineTest.class,
                GWTEntityProcessorSystemTest.class,
                GWTEntitySetListenerTest.class,
                GWTEntitySetTest.class,
                GWTFamilyTest.class,
                GWTHandleTest.class,
                GWTMapperTest.class,
                GWTBagTest.class,
                GWTByteBagTest.class,
                GWTDoubleBagTest.class,
                GWTFloatBagTest.class,
                GWTIntBagTest.class,
                GWTLongBagTest.class,
                GWTMaskTest.class,
                GWTShortBagTest.class)) {
            suite.addTestSuite(tc);
        }
        return suite;
    }
}
