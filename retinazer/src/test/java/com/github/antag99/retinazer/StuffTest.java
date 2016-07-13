package com.github.antag99.retinazer;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.antag99.retinazer.system.StuffSystem;
import com.github.antag99.retinazer.test.PlainTestRunner;

@RunWith(PlainTestRunner.class)
public final class StuffTest {
    
    @Test
    public void testStuff() {
        Engine engine = new Engine(new EngineConfig()
                .addSystem(new StuffSystem(false)));
        StuffSystem stuffSystem = engine.getSystem(StuffSystem.class);
        stuffSystem.createStuff();
        stuffSystem.createStuff();
        stuffSystem.createStuff();
        engine.update();
        engine.update();
        engine.update();
        stuffSystem.createStuff();
        engine.update();
        engine.update();
        engine.update();
        engine.update();
        stuffSystem.createStuff();
        stuffSystem.createStuff();

        for (int i = 0; i < 10000; i++) {
            stuffSystem.createStuff();
            if (i % 100 == 0)
                engine.update();
            if (i % 200 == 0)
                engine.reset();
        }
    }
}
