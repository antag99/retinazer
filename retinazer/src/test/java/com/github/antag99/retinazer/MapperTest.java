package com.github.antag99.retinazer;

import org.junit.Test;

public class MapperTest {

    // This should usually not be done... bad.
    public static final class BadComponent implements Component {
        public BadComponent(int requiresAnArgument) {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoConstructor() {
        Engine engine = EngineConfig.create().finish();
        int entity = engine.createEntity().getEntity();
        Mapper<BadComponent> mBad = engine.getMapper(BadComponent.class);
        mBad.create(entity);
    }

    // This should *never* be done
    public static final class ReallyBadComponent implements Component {
        public ReallyBadComponent() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testErrorConstructor() {
        Engine engine = EngineConfig.create().finish();
        int entity = engine.createEntity().getEntity();
        Mapper<ReallyBadComponent> mReallyBad = engine.getMapper(ReallyBadComponent.class);
        mReallyBad.create(entity);
    }

    @Test
    public void testRemoveNothing() {
        Engine engine = EngineConfig.create().finish();
        Mapper<FlagComponentA> mFlagA = engine.getMapper(FlagComponentA.class);
        int entity = engine.createEntity().getEntity();
        mFlagA.remove(entity); // nothing should happen
        engine.update();
        mFlagA.create(entity);
        mFlagA.remove(entity);
        mFlagA.remove(entity);
        engine.update();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTwice() {
        Engine engine = EngineConfig.create().finish();
        Mapper<FlagComponentA> mFlagA = engine.getMapper(FlagComponentA.class);
        int entity = engine.createEntity().getEntity();
        mFlagA.add(entity, new FlagComponentA());
        mFlagA.add(entity, new FlagComponentA());
    }
}
