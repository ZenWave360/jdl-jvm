# JHipster Domain Language (JDL) Wrapped for the JVM

[![Maven Central](https://img.shields.io/maven-central/v/io.github.zenwave360.jhipster/jdl-jvm.svg?label=Maven%20Central&logo=apachemaven)](https://search.maven.org/artifact/io.github.zenwave360.jhipster/jdl-jvm)

Java wrapper for the [JHipster Domain Language JDL](https://www.jhipster.tech/jdl/intro) using Graalvm JS Truffle Language...

This is part of the [ZenWave Code Generator](https://github.com/ZenWave360/zenwave-code-generator) project:

> Configurable and extensible Code Generator for DDD and API-First modeling with support for JHipster JDL, AsyncAPI and OpenAPI

## Usage

```xml
<dependency>
    <groupId>io.github.zenwave360.jhipster</groupId>
    <artifactId>jdl-jvm</artifactId>
    <version>${jdl-jvm.version}</version>
</dependency>
```

```java
String jdlString = Files.readString(Paths.get("jdl-samples-main/21-points.jh"));
Map<String, Object> parsedJDL = JDLParser.parseJDL(jdlString);
```

```java
String jdlString = Files.readString(Paths.get("jdl-samples-main/21-points.jh"));
Map<String, Object> parsedJDL = JDLParser.parseJDL(jdlString, , Map.of("databaseType", "mongodb"));
```

Note: It uses Graalvm JS Truffle Language and performance is not that bad. It takes about 3' to load the js script and parse the jdl string.

## Generating jdl-parser.js bundle

Upstream JDL source code is located under `src/main/jdl-core` folder.

You can bundle `target/classes/io/zenwave360/jhipster/jdl/jdl-parser.js` using:

- `mvn generate-resources` from project root, or
- `npm run generate-resources` from `src/main/jdl-core` folder

## Custom Extensions

This wrapper includes some extensions compatible with the original JDL format, meaning that valid JDL will produce the same jdlObject as the upstream jdl library.

### Extensions In JDL Language

- Field Types: in addition to enums and basic types it allows other entities (and arrays of) as field type, this is useful for embedded fields which are not relations.
- Service: in addition to `serviceClass` and `serviceImpl` it allows configuring free text value as `serviceName` to allow grouping multiple entities in a given service. Then it's up to each generator to generate an interface or just an implementation class.

### Extensions In Parsed Result Object

- Adds annotation options to the entity object
- Adds `isEnum`, `isEntity` and `isArray` to entity fields
- Adds `className`, `instanceName`, `classNamePlural` and `instanceNamePlural` to entity fields
- Fill globaly configured options (like service, dto ,search...) to entity options for consistency
