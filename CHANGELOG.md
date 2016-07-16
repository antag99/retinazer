## Changelog

# Version 0.3.1-SNAPSHOT
- Implemented experimental bytecode weaving
  - Classes can be weaved at compile-time using the command-line interface,
      or at runtime using a custom `ClassLoader` implementation.
  - The weaver packs components into arrays, which improves cache locality;
      this currently yields a 15% performance boost, but will hopefully improve
      as more optimizations are introduced.
  - `PackedMapper` that allows access to the backing arrays for fast access
  - `Property` classes that represent properties of packed components
- Enhanced `Bag` API:
  - Added interface `AnyBag`
    - `copyFrom` methods for replacing elements with those of another bag
    - `copyPartFrom` method for copying some elements from another bag
    - `clear(Mask)` method
  - Removed `Bag(int)` constructor
  - Introduced `BooleanBag`
  - Introduced `CharBag`
- Removed `Mapper.add(int, Component)`, `create` should be used instead
- `Mask.set(Mask/long[])` renamed to `copyFrom`
- `Engine.getSystems()` returns `EntitySystem[]` instead of `Iterable<EntitySystem>`
- Fix: `ByteBag` implements `ensureCapacity(int)`
- Fix: `Bag.ensureCapacity(int)` throws `NegativeArraySizeException` if `capacity` is negative.

# Version 0.3.0 (released 2016-07-04)
- `ensureCapacity(int)` for all `Bag` implementations
- Removed GWT support (for now at least)
- Removed libGDX dependency
- Fix: `Bag.set(int)` no longer treats the `0` value specially

# Version 0.2.1 (released 2015-10-30)
- Fix: `OutOfMemoryError` due to never-reset inserted component list

# Version 0.2.0 (released 2015-10-30)
- Revised `Wire` API and semantics
  - `Wire` is inherited from superclass
  - An exception is always thrown when a field is not handled by a resolver
  - `Wire.Ignore` -> `SkipWire`
- `EntitySystem.setEngine(Engine)` is now `EntitySystem.setup()`
- `Engine.getEntitiesFor(FamilyConfig)` is now `Engine.getFamily(FamilyConfig).getEntities()`
- Reset method for `Engine` (`Engine.reset()`)
- Enhanced `EntitySetListener` API
  - Renamed to `EntityListener`
  - `inserted(IntArray)` -> `inserted(EntitySet)`
  - `removed(IntArray)` -> `removed(EntitySet)`
- `Engine.addEntityListener(EntityListener)`
- `Engine.removeEntityListener(EntityListener)`
- `Family.addListener(EntityListener)`
- `Family.removeListener(EntityListener)`
- Revised `Mask` API
  - `Mask.getWord(int)`
  - `Mask.getWordCount()`
  - `Mask.set(long[])`
  - `Mask.setWord(int, long)`
  - `Mask.getWords()`
  - `Mask.isEmpty()`
  - Removed 'Mask.push(int)'
  - Removed 'Mask.pop(int)'
- Revised `EntitySet` API
  - Modification delegated to `EntitySetEdit`
    - `EntitySet.addEntities(IntArray)` removed
    - `EntitySet.removeEntities(IntArray)` removed
    - `EntitySet.addEntities(Mask)` checks existing entities
    - `EntitySet.removeEntities(Mask)` checks existing entities
  - `EntitySet.edit()` returns `EntitySetEdit` or throws an exception if unmodifiable
  - `EntitySet.unmodifiable()` renamed to `EntitySet.view()`
  - `EntitySet.size()`
  - `EntitySet.isEmpty()`
  - Listeners are no longer supported, `EntityListener` is used for family events.
- `DependencyConfig` and `DependencyResolver` removed
- `Handle` removed
- `toString()` implementation for `EntitySet`
- `RetinazerException` as a general-purpose runtime exception
- Fix: EntitySet indices were invalidated even when there was no change
- Fix: `IndexOutOfBoundsException` when negative index is used for `Bag.get`
- Fix: No more global state, different engines running in different threads should work.

# Version 0.1.2 (released 2015-08-22)
- Made `Mask#getIndices(IntArray)` append elements to the array.
- Fixed issue where destroying an entity didn't remove it's components

# Version 0.1.1 (released 2015-08-21)
- Updated to libgdx 1.6.5

# Version 0.1.0 (released 2015-08-20)
- Initial release
