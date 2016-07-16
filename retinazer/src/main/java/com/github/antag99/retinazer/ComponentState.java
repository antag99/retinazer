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

import com.github.antag99.retinazer.util.AnyBag;
import com.github.antag99.retinazer.util.Bag;

/**
 * State for a specific component type.
 *
 * @param <T>
 *            The component type.
 */
public interface ComponentState<T extends Component> {

    /**
     * Gets the component type this state handles.
     *
     * @return
     *         the component type this state handles.
     */
    Class<T> getType();

    /**
     * Sets this {@code ComponentState} to the other {@code ComponentState}.
     *
     * @param other
     *            the other state to copy from.
     */
    void copyFrom(ComponentState<T> other);

    /**
     * Sets this {@code ComponentState} to the other {@code ComponentState}.
     *
     * @param other
     *            the other state to copy from.
     * @param handler
     *            {@link CopyHandler} to use for copying component data.
     */
    void copyFrom(ComponentState<T> other, CopyHandler handler);

    /**
     * Gets a copy of this {@code ComponentState}.
     *
     * @return
     *         a copy of this state.
     */
    ComponentState<T> copy();

    /**
     * Gets a copy of this {@code ComponentState}.
     *
     * @param handler
     *            {@link CopyHandler} to use for copying component data.
     * @return
     *         a copy of this state.
     */
    ComponentState<T> copy(CopyHandler handler);

    /**
     * Handles copying of component data. Can be customized to,
     * for example, support deep copying of objects.
     */
    interface CopyHandler {

        /**
         * Copies the contents from one bag to another.
         *
         * @param from
         *            the bag to copy from.
         * @param to
         *            the bag to copy to.
         */
        <B extends AnyBag<B>> void copy(B from, B to);
    }

    /**
     * Default {@code CopyHandler} implementation, does not handle
     * object references separately. Works well with packed components.
     */
    public static final CopyHandler DEFAULT_COPY_HANDLER = new CopyHandler() {
        @Override
        public <B extends AnyBag<B>> void copy(B from, B to) {
            to.copyFrom(from, to instanceof Bag);
        }
    };
}
