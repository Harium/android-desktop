# android-desktop

This repo has classes to run Android projects in Desktop environment.
It is also used to generate/publish Android maven libraries without using Android emulator.
Some classes are just stubs but some classes replicates real functionality.


It is an experimental project in *very* early stages.
Classes are added by need and sometimes do not reflect the exact Android behavior, be warned.


## Maven
```
<dependency>
    <groupId>com.harium.android</groupId>
    <artifactId>core</artifactId>
    <version>0.3.3</version>
</dependency>
```

## Generating a local jar
```
mvn package -Dmaven.javadoc.skip=true
```

## License
Apache 2.0

## Reference
- [Android Source Code](https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/)