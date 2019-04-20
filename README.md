JCL Equinox [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/com.github.marschall.jcl-equinox/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/com.github.marschall.jcl-equinox)  [![Javadocs](https://www.javadoc.io/badge/com.github.marschall/com.github.marschall.jcl-equinox.svg)](https://www.javadoc.io/doc/com.github.marschall/com.github.marschall.jcl-equinox)
===========
This is an implementation of [Apache Commons Logging](https://commons.apache.org/proper/commons-logging/) using the [Equinox](https://www.eclipse.org/equinox/) [ExtendedLogService](https://bugs.eclipse.org/bugs/show_bug.cgi?id=260672).


Implementation Notes
--------------------

The implementation is a fragment of the `org.apache.commons.logging` with a [commons-logging.properties](https://commons.apache.org/proper/commons-logging/guide.html) setting the implementation of the `LogFactory`.

Building
--------
If you want to build this project then you need [Maven 3](https://maven.apache.org/) and add the following section to your <code>settings.xml</code>

```xml
<profile>
  <id>photon</id>
  <activation>
    <activeByDefault>false</activeByDefault>
  </activation>
  <repositories>
    <repository>
      <id>photon</id>
      <layout>p2</layout>
      <url>http://download.eclipse.org/releases/photon</url>
    </repository>
  </repositories>
</profile>