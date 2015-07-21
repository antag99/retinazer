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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Events are used for communication between systems. They can be parameterized
 * with entities, and then filtered on the listener size using the {@link WithEntity}
 * annotation. All that needs to be done is to add public getters for the entities.
 *
 * @see WithEntity
 */
public interface Event {

    /**
     * <p>
     * WithEntity is used for filtering events based on entities
     * </p>
     * <h5>Collision system example</h5>
     *
     * <pre>
     * <code>
     * public final class CollisionEvent implements Event {
     *     private Entity collider;
     *     private Entity collidee;
     *
     *     public CollisionEvent(Entity collider, Entity collidee) {
     *         this.collider = collider;
     *         this.collidee = collidee;
     *     }
     *
     *     public Entity getCollider() {
     *         return collider;
     *     }
     *
     *     public Entity getCollidee() {
     *         return collidee;
     *     }
     * }
     * </code>
     * </pre>
     *
     * <pre>
     * <code>
     * public final class PlayerSystem extends EntitySystem {
     *     // ...
     *
     *     &#64;EventHandler({
     *         &#64;WithEntity(name = "collider", with = {
     *             PlayerComponent.class
     *         }),
     *         &#64;WithEntity(name = "collidee", with = {
     *             EnemyComponent.class
     *         })
     *     })
     *     private void handlePlayerCollision(CollisionEvent event) {
     *         // ...
     *     }
     * }
     * </code>
     * </pre>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({})
    public @interface WithEntity {
        /**
         * Name of the property in the event returning the entity
         */
        public String name();

        /**
         * The entity must have <b>all</b> of these components in order for the
         * listener to receive the event.
         */
        public Class<? extends Component>[]with() default {};

        /**
         * The entity must have <b>none</b> of these components in order for the
         * listener to receive the event.
         */
        public Class<? extends Component>[]exclude() default {};
    }
}
