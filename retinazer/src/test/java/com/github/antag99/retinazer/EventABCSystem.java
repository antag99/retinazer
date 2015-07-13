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

public class EventABCSystem extends EntitySystem {
    @EventHandler(priority = -5)
    public void beforeHandleEventA(EventA event, Entity entity) {
    }

    @EventHandler(priority = -5)
    public void beforeHandleEventB(EventB event, Entity entity) {
    }

    @EventHandler(priority = -5)
    public void beforeHandleEventC(EventC event, Entity entity) {
    }

    @EventHandler(priority = -10)
    public void beforeHandleEvent(Event event, Entity entity) {
    }

    @EventHandler(priority = 0)
    public void handleEventA(EventA event, Entity entity) {
    }

    @EventHandler(priority = 0)
    public void handleEventB(EventB event, Entity entity) {
    }

    @EventHandler(priority = 0)
    public void handleEventC(EventC event, Entity entity) {
    }

    @EventHandler(priority = 1)
    public void handleEvent(Event event, Entity entity) {
    }

    @EventHandler(priority = 5)
    public void afterHandleEventA(EventA event, Entity entity) {
    }

    @EventHandler(priority = 5)
    public void afterHandleEventB(EventB event, Entity entity) {
    }

    @EventHandler(priority = 5)
    public void afterHandleEventC(EventC event, Entity entity) {
    }

    @EventHandler(priority = 10)
    public void afterHandleEvent(Event event, Entity entity) {
    }
}
