# JHipster Domain Language (JDL) Wrapped for the JVM

This is a java wrapper for JDL, very much work in progress but working...

You need to build from source then:

```xml
<dependency>
    <groupId>io.zenwave360.jhipster</groupId>
    <artifactId>jdl-jvm</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Usage:

```java
String jdlString = Files.readString(Paths.get("jdl-samples-main/21-points.jh"));
Map<String, Object> parsedJDL = JDLParser.parseJDL(jdlString);
```

Note: It uses Graalvm JS Truffle Language and preformance is not that bad. It takes about 3' to load the js script and parse the jdl string.

TODO: Add the script to bundle `jdl-parser.js` from upstream jdl code to this project.

