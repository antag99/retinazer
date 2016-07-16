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
package com.github.antag99.retinazer;

import java.util.Arrays;

import com.github.antag99.retinazer.ComponentState.CopyHandler;
import com.github.antag99.retinazer.util.Bag;
import com.github.antag99.retinazer.util.Mask;

@SuppressWarnings({ "rawtypes", "unchecked" })
class EngineStateBase implements EngineState {
    Engine engine;
    Mask entities;

    Bag<ComponentStateBase<?>> componentState;
    int componentStateCount;

    EngineStateBase(Engine engine, Mask entities, Bag<ComponentStateBase<?>> componentState, int componentStateCount) {
        this.engine = engine;
        this.entities = entities;
        this.componentState = componentState;
        this.componentStateCount = componentStateCount;
    }

    EngineState shallowCopy() {
        Bag<ComponentStateBase<?>> componentState = this.componentState;
        Bag<ComponentStateBase<?>> copyComponentState = new Bag<>();
        for (int i = 0, n = this.componentStateCount; i < n; i++)
            copyComponentState.set(i, componentState.get(i).shallowCopy());
        return new EngineStateBase(engine, new Mask(), copyComponentState, componentStateCount);
    }

    @Override
    public final EngineState copy() {
        return copy(ComponentState.DEFAULT_COPY_HANDLER);
    }

    @Override
    public EngineState copy(CopyHandler handler) {
        EngineState state = shallowCopy();
        state.copyFrom(this, handler);
        return state;
    }

    @Override
    public final void copyFrom(EngineState other) {
        copyFrom(other, ComponentState.DEFAULT_COPY_HANDLER);
    }

    @Override
    public void copyFrom(EngineState other, CopyHandler handler) {
        EngineStateBase otherState = (EngineStateBase) other;

        if (otherState.engine != engine)
            throw new IllegalArgumentException();

        Bag<ComponentStateBase<?>> components = this.componentState;
        Bag<ComponentStateBase<?>> otherComponents = otherState.componentState;

        // Clear exceeding components
        for (int i = otherState.componentStateCount, n = this.componentStateCount; i < n; i++) {
            components.get(i).clear();
        }

        // Fill in missing components
        for (int i = this.componentStateCount, n = otherState.componentStateCount; i < n; i++) {
            components.set(i, otherComponents.get(i).shallowCopy());
        }

        // Update count
        if (otherState.componentStateCount > this.componentStateCount)
            this.componentStateCount = otherState.componentStateCount;

        // Copy from other state
        entities.copyFrom(otherState.entities);
        for (int i = 0, n = Math.min(componentStateCount, otherState.componentStateCount); i < n; i++)
            ((ComponentState) components.get(i)).copyFrom(otherComponents.get(i), handler);
    }

    @Override
    public <T extends Component> ComponentState<T> getComponentState(Class<T> componentType) {
        Bag<ComponentStateBase<?>> componentState = this.componentState;
        for (int i = 0, n = this.componentStateCount; i < n; i++)
            if (componentState.get(i).getType() == componentType)
                return (ComponentState<T>) componentState.get(i);
        return null;
    }

    @Override
    public ComponentState<?>[] getComponentStates() {
        return Arrays.copyOf(componentState.buffer, componentStateCount, ComponentState[].class);
    }
}
