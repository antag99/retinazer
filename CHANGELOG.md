## Changelog

# Version 0.1.3-SNAPSHOT
- Enhanced `Handle` API
  - `Handle.getIndex()` -> `Handle.idx()`
  - `Handle.setIndex(int)` -> `Handle.idx(int)`
  - `Handle.duplicate()` -> `Handle.cpy()`
  - `Handle.get(Class<? extends Component>)`
  - `Handle.has(Class<? extends Component>)`
- Enhanced `Mapper` API
  - `Mapper.get(Handle)`
  - `Mapper.has(Handle)`
  - `Mapper.create(Handle)`
  - `Mapper.add(Handle, Component)`
  - `Mapper.remove(Handle)`
- Revised `Wire` API and semantics
  - `Wire` is inherited from superclass
  - An exception is always thrown when a field is not handled by a resolver
  - `Wire.Ignore` -> `SkipWire`
- `EntitySystem.setEngine(Engine)` is now `EntitySystem.setup()`
- `Engine.getEntitiesFor(FamilyConfig)` is now `Engine.getFamily(FamilyConfig).getEntities()`
- Reset method for `Engine` (`Engine.reset()`)
- Enhanced `Mask` API
  - `Mask.getWord(int)`
  - `Mask.getWordCount()`
  - `Mask.set(long[])`
  - `Mask.setWord(int, long)`
  - `Mask.getWords()`
- Revised `EntitySet` API
  - `EntitySet.addEntities(IntArray)` removed
  - `EntitySet.removeEntities(IntArray)` removed
  - `EntitySet.addEntities(Mask)` checks existing entities
  - `EntitySet.removeEntities(Mask)` checks existing entities
- Support library for prototyping/jamming ("retinazer-beam")
  - `DependencyConfig` and `DependencyResolver` moved to retinazer-beam
- `toString()` implementation for `EntitySet`
- `RetinazerException` as a general-purpose runtime exception
- Fix: `IndexOutOfBoundsException` when negative index is used for `Bag.get`
- Fix: No more global state, different engines running in different threads should work.

# Version 0.1.2 (released 2015-08-22)
- Made `Mask#getIndices(IntArray)` append elements to the array.
- Fixed issue where destroying an entity didn't remove it's components

# Version 0.1.1 (released 2015-08-21)
- Updated to libgdx 1.6.5

# Version 0.1.0 (released 2015-08-20)
- Initial release
