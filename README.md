# apt-gui

This is a graphical user interface for [apt](https://github.com/CvO-Theory/apt).

## How to build

It is expected that the root folder of apt is next to the root folder of apt-gui in the file system.

```
home/
 |--- apt/
 |     |--- classes/
 |     |--- lib/
 |     |--- build.xml
 |--- apt-gui/
       |--- build.gradle
```

You need to build apt first since the apt/classes folder is expected to be available for compilation of apt-gui. Then you can build apt-gui using the following commands:

```
cd apt-gui
./gradlew jar
```

Some other available build targets are: eclipse, javadoc
