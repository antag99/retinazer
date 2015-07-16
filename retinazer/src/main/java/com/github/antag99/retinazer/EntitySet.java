package com.github.antag99.retinazer;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.github.antag99.retinazer.utils.Mask;

/**
 * Entity set; supports retrieving and destroying entities, but does
 * not support insertion; use {@link Engine#createEntity()} for that.
 * </p>
 * A set of entities is updated as entities are added to the engine; all sets
 * target a specific kind of entity matched by a {@link Family}. To get an entity set,
 * use {@link Engine#getEntities()} or {@link Engine#getEntitiesFor(FamilyConfig)}.
 * </p>
 * To process all entities in a set, the {@link #process(EntityProcessor)} method can be used.
 */
public final class EntitySet extends AbstractSet<Entity> {
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
    public boolean contains(Object o) {
        if (!(o instanceof Entity))
            return false;
        Entity entity = (Entity) o;
        if (entity.getEngine() != engine)
            return false;
        return entities.get(entity.getIndex());
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Entity))
            return false;
        Entity entity = (Entity) o;
        if (entity.getEngine() != engine)
            return false;
        boolean removed;
        if (removed = entities.get(entity.getIndex())) {
            entity.destroy();
        }
        return removed;
    }

    @Override
    public Iterator<Entity> iterator() {
        return new EntityIterator();
    }

    @Override
    public int size() {
        return entities.cardinality();
    }

    public void process(EntityProcessor processor) {
        final Engine engine = this.engine;
        final Mask entities = this.entities;
        for (int i = entities.nextSetBit(0); i != -1; i = entities.nextSetBit(i + 1)) {
            processor.process(engine.getEntityForIndex(i));
        }
    }
}
