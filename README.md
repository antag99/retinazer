# Retinazer
[![Build Status][build_badge]][build]
[![Coverage Status][coverage_badge]][coverage]
[![Lines of Code][loc_badge]][loc]
[![License][license_badge]][license]
[![Join the chat at https://gitter.im/antag99/retinazer][gitter_badge]][gitter]
[![Download][bintray_badge]][bintray]

Retinazer is an implementation of the [entity-component-system][ecs] design
pattern in Java. Check out the source code for documentation; or help me set up
the [wiki](https://github.com/antag99/retinazer/wiki).

## Using retinazer in your project
Head over to [bintray](https://bintray.com/antag99/maven/retinazer/0.1.0/view)
for instructions; alternatively add this to your `pom.xml`;
```xml
<repositories>
  <repository>
    <snapshots>
      <enabled>false</enabled>
    </snapshots>
    <id>bintray-antag99-maven</id>
    <name>bintray</name>
    <url>http://dl.bintray.com/antag99/maven</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.github.antag99.retinazer</groupId>
    <artifactId>retinazer</artifactId>
    <version>0.1.0</version>
  </dependency>
</dependencies>
```
or add this to your `build.gradle`:
```groovy
repositories {
  maven {
    id "bintray-antag99-maven"
    url "http://dl.bintray.com/antag99/maven"
  }
}

dependencies {
  compile "com.github.antag99.retinazer:retinazer:0.1.0"
}
```
Nightly builds can be obtained via `https://jitpack.io/` using `-SNAPSHOT` as
version.

## Contributing
Contributions are welcome, but make sure to follow the [contribution guidelines](CONTRIBUTING.md).
Also, if you plan to implement a new major feature, please open an issue first
so I can provide input.

[build]: https://travis-ci.org/antag99/retinazer
[build_badge]: https://travis-ci.org/antag99/retinazer.svg?branch=master
[coverage]: https://coveralls.io/github/antag99/retinazer?branch=master
[coverage_badge]: https://coveralls.io/repos/antag99/retinazer/badge.svg?branch=master&service=github
[loc]: https://github.com/antag99/retinazer/search?l=java
[loc_badge]: https://antag99.github.io/loc/retinazer.svg
[license]: http://choosealicense.com/licenses/mit/
[license_badge]: https://img.shields.io/badge/license-MIT-blue.svg
[gitter]: https://gitter.im/antag99/retinazer
[gitter_badge]: https://img.shields.io/badge/GITTER-JOIN_CHAT_%E2%86%92-1dce73.svg
[bintray]: https://bintray.com/antag99/maven/retinazer/_latestVersion
[bintray_badge]: https://api.bintray.com/packages/antag99/maven/retinazer/images/download.svg
[ecs]: https://en.wikipedia.org/wiki/Entity_component_system
