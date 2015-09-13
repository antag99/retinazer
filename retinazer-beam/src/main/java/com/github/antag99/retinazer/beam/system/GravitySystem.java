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
package com.github.antag99.retinazer.beam.system;

import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.retinazer.beam.component.Velocity;
import com.github.antag99.retinazer.beam.component.Gravity;

public final class GravitySystem extends EntityProcessorSystem {
    private DeltaSystem deltaSystem;
    private Mapper<Gravity> mGravity;
    private Mapper<Velocity> mVelocity;

    @SkipWire
    private float gravityX;
    @SkipWire
    private float gravityY;
    @SkipWire
    private float maxGravityX;
    @SkipWire
    private float maxGravityY;

    public GravitySystem(float gravityX, float gravityY,
            float maxGravityX, float maxGravityY) {
        super(Family.with(Gravity.class, Velocity.class));

        this.gravityX = gravityX;
        this.gravityY = gravityY;
        this.maxGravityX = maxGravityX;
        this.maxGravityY = maxGravityY;
    }

    public float getGravityX() {
        return gravityX;
    }

    public float getGravityY() {
        return gravityY;
    }

    public float getMaxGravityX() {
        return maxGravityX;
    }

    public float getMaxGravityY() {
        return maxGravityY;
    }

    @Override
    protected void process(int entity) {
        Gravity gravity = mGravity.get(entity);
        Velocity velocity = mVelocity.get(entity);

        float deltaTime = deltaSystem.getDeltaTime();

        float gravityX = this.gravityX * gravity.gravityScaleX;
        float gravityY = this.gravityY * gravity.gravityScaleY;
        float maxGravityX = this.maxGravityX * gravity.gravityScaleX;
        float maxGravityY = this.maxGravityY * gravity.gravityScaleY;

        if (velocity.x * maxGravityX < maxGravityX * maxGravityX) {
            velocity.x += gravityX * deltaTime;
            if (velocity.x * maxGravityX > maxGravityX * maxGravityX) {
                velocity.x = maxGravityX;
            }
        }

        if (velocity.y * maxGravityY < maxGravityY * maxGravityY) {
            velocity.y += gravityY * deltaTime;
            if (velocity.y * maxGravityY > maxGravityY * maxGravityY) {
                velocity.y = maxGravityY;
            }
        }
    }
}
