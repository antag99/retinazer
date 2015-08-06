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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.antag99.retinazer.utils.Experimental;

/**
 * Events are used for communication between systems.
 */
public interface Event {

    /**
     * Specifies the {@link EventConstraintHandler} implementation handling a constraint.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @Experimental
    public @interface UseConstraintHandler {
        public Class<? extends EventConstraintHandler>value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface WithEntity {

        /**
         * Name of the property in the event returning the entity.
         *
         * @return Property name
         */
        public String name();

        /**
         * The entity must have <b>all</b> of these components in order for the
         * handler to receive the event.
         *
         * @return Required components
         */
        public Class<? extends Component>[]with() default {};

        /**
         * The entity must have <b>none</b> of these components in order for the
         * handler to receive the event.
         *
         * @return Excluded components
         */
        public Class<? extends Component>[]exclude() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @UseConstraintHandler(EntityConstraintHandler.class)
    public @interface WithEntities {
        public WithEntity[]value() default {};
    }

    /**
     * <p>
     * WithFamily is used for filtering events based on what families they are
     * referring to. This is denoted by the {@code with()} and {@code exclude()}
     * methods of the event.
     * </p>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @UseConstraintHandler(FamilyConstraintHandler.class)
    public @interface WithFamily {

        /**
         * @return What the {@code with()} method of the event must return.
         */
        public Class<? extends Component>[]with() default {};

        /**
         * @return What the {@code exclude()} method of the event must return.
         */
        public Class<? extends Component>[]exclude() default {};
    }
}
