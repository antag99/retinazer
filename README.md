# Retinazer
[![Build Status][build_badge]][build]
[![Coverage Status][coverage_badge]][coverage]
[![Download][bintray_badge]][bintray]
[![Join the chat at https://gitter.im/antag99/retinazer][gitter_badge]][gitter]

[![GNU Lesser General Public License][lgpl_logo]][lgpl]

Retinazer is an implementation of the [entity-component-system][ecs] design
pattern in Java.

## Using retinazer in your project
Head over to [bintray](https://bintray.com/antag99/maven/retinazer/view)
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
    <version>0.3.0</version>
  </dependency>
</dependencies>
```
or add this to your `build.gradle`:
```groovy
repositories {
  maven {
    name "bintray-antag99-maven"
    url "http://dl.bintray.com/antag99/maven"
  }
}

dependencies {
  compile "com.github.antag99.retinazer:retinazer:0.3.0"
}
```
Snapshot builds can be obtained via `https://jitpack.io/` using `-SNAPSHOT` as
version.

## License
The latest version of Retinazer is available under the GNU Lesser General
Public License. `85350a7fa951666bc7e47713129dca6ce601813d` and earlier
revisions are available under the MIT license. The range `2015-2016` is used in
source file headers to denote the years within that range, inclusively.

[build]: https://travis-ci.org/antag99/retinazer
[build_badge]: https://travis-ci.org/antag99/retinazer.svg?branch=master
[coverage]: https://coveralls.io/github/antag99/retinazer?branch=master
[coverage_badge]: https://coveralls.io/repos/antag99/retinazer/badge.svg?branch=master&service=github
[bintray]: https://bintray.com/antag99/maven/retinazer/_latestVersion
[bintray_badge]: https://api.bintray.com/packages/antag99/maven/retinazer/images/download.svg
[gitter]: https://gitter.im/antag99/retinazer
[gitter_badge]: https://img.shields.io/badge/GITTER-JOIN_CHAT_%E2%86%92-1dce73.svg
[lgpl]: https://www.gnu.org/copyleft/lesser.html
[lgpl_logo]: https://www.gnu.org/graphics/lgplv3-147x51.png
[ecs]: https://en.wikipedia.org/wiki/Entity_component_system
