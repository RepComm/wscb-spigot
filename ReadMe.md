
# wscb-spigot
[wscb](https://github.com/RepComm/wscb) available as a spigot jar

## building
Lots of information from [spigot/creating a blank plugin](https://www.spigotmc.org/wiki/creating-a-blank-spigot-plugin-in-vs-code)

Helpful articles:
[maven/shaded jars](https://maven.apache.org/plugins/maven-shade-plugin/usage.html)

Requirements:
- [apache maven](https://maven.apache.org)
- [jdk](https://www.oracle.com/java/technologies/javase-downloads.html)

run `mvn package` or `./build.sh`

install wscb-spigot-X.X-SNAPSHOT.jar to mc server plugins directory
