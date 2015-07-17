package com.github.antag99.retinazer;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.github.antag99.retinazer.utils.Mask;

public final class EntitySet implements Iterable<Entity> {
    // Engine the entities belong to
    final Engine engine;
    // Mask updated by FamilyManager
    final Mask entities;

    EntitySet(Engine engine) {
        this.engine = engine;
        this.entities = new Mask();
    }

    private final class EntityIterator implements Iterator<Entity> {
        private int index = 0;
        private int previousIndex = -1;

        @Override
        public boolean hasNext() {
            if (index == -1)
                return false;
            index = entities.nextSetBit(index);
            return index != -1;
        }

        @Override
        public Entity next() {
            if (!hasNext())
                throw new NoSuchElementException();
            previousIndex = index;
            index = index + 1;
            return engine.getEntityForIndex(previousIndex);
        }

        @Override
        public void remove() {
            if (previousIndex == -1)
                throw new IllegalStateException();
            engine.getEntityForIndex(previousIndex).destroy();
            previousIndex = -1;
        }
    }

    @Override
    public Iterator<Entity> iterator() {
        return new EntityIterator();
    }

    public void process(EntityProcessor processor) {
        final Engine engine = this.engine;
        final Mask entities = this.entities;
        for (int i = entities.nextSetBit(0); i != -1; i = entities.nextSetBit(i + 1)) {
            processor.process(engine.getEntityForIndex(i));
        }
    }
}
