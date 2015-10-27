package com.github.antag99.retinazer;

public class GWTMapperTest extends RetinazerTestCase {

    // This should usually not be done... bad.
    public static final class BadComponent implements Component {
        public BadComponent(int requiresAnArgument) {
        }
    }

    public void testNoConstructor() {
        try {
            Engine engine = new Engine(new EngineConfig());
            int entity = engine.createEntity();
            Mapper<BadComponent> mBad = engine.getMapper(BadComponent.class);
            mBad.create(entity);
        } catch (RetinazerException ex) {
            return;
        }
        fail("RetinazerException expected");
    }

    // This should *never* be done
    public static final class ReallyBadComponent implements Component {
        public ReallyBadComponent() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }

    public void testErrorConstructor() {
        try {
            Engine engine = new Engine(new EngineConfig());
            int entity = engine.createEntity();
            Mapper<ReallyBadComponent> mReallyBad = engine.getMapper(ReallyBadComponent.class);
            mReallyBad.create(entity);
        } catch (UnsupportedOperationException ex) {
            return;
        }
        fail("UnsupportedOperationException expected");
    }

    public void testRemoveNothing() {
        Engine engine = new Engine(new EngineConfig());
        Mapper<FlagComponentA> mFlagA = engine.getMapper(FlagComponentA.class);
        int entity = engine.createEntity();
        mFlagA.remove(entity); // nothing should happen
        engine.update();
        mFlagA.create(entity);
        mFlagA.remove(entity);
        mFlagA.remove(entity);
        engine.update();
    }

    public void testAddTwice() {
        try {
            Engine engine = new Engine(new EngineConfig());
            Mapper<FlagComponentA> mFlagA = engine.getMapper(FlagComponentA.class);
            int entity = engine.createEntity();
            mFlagA.add(entity, new FlagComponentA());
            mFlagA.add(entity, new FlagComponentA());
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail("IllegalArgumentException expected");
    }
}
