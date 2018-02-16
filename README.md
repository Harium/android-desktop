# android-desktop
Stub classes to run Android projects in Desktop environment

It is a *very* experimental project in early stage.
This repo is used to generate/publish Android maven libraries without using Android emulator. Classes are added by need and sometimes do not reflect the exact behavior as in Android, be careful.


## Maven
```
<dependency>
    <groupId>com.harium.android</groupId>
    <artifactId>core</artifactId>
    <version>0.3.0</version>
</dependency>
```

## Generating a local jar
```
mvn package -Dmaven.javadoc.skip=true
```
