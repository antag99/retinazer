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

import com.github.antag99.retinazer.utils.DestroyEvent;
import com.github.antag99.retinazer.utils.InitializeEvent;
import com.github.antag99.retinazer.utils.Inject;
import com.github.antag99.retinazer.utils.UpdateEvent;

public abstract class EntityProcessorSystem extends EntitySystem
        implements EntityProcessor, EntityListener {
    private @Inject Engine engine;
    private FamilyConfig family;
    private EntitySet entities;

    public EntityProcessorSystem(FamilyConfig family) {
        this.family = family;
    }

    @EventHandler
    private void initialize(InitializeEvent event) {
        entities = engine.getEntitiesFor(family);
        engine.addEntityListener(family, this);
    }

    @EventHandler
    private void destroy(DestroyEvent event) {
        entities = null;
    }

    @EventHandler
    private void update(UpdateEvent event) {
        for (Entity entity : getEntities().getEntities()) {
            process(entity);
        }
    }

    public EntitySet getEntities() {
        return entities;
    }

    public FamilyConfig getFamily() {
        return family;
    }

    @Override
    public abstract void process(Entity entity);

    @Override
    public void entityAdd(Entity entity) {
    }

    @Override
    public void entityRemove(Entity entity) {
    }
}
