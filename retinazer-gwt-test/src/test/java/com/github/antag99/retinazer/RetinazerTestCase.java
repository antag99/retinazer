package com.github.antag99.retinazer;

import com.google.gwt.junit.client.GWTTestCase;

public abstract class RetinazerTestCase extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "com.github.antag99.RetinazerTest";
    }

    public static void assertNotEquals(Object first, Object second) {
        if (first == null ? second == null : first.equals(second)) {
            fail("Values should be different. " + first);
        }
    }
}
