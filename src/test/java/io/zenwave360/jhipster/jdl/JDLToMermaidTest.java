package io.zenwave360.jhipster.jdl;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JDLToMermaidTest {

    @Test
    public void testExportToMermaid21PointsJDL() throws IOException {
        String jdlString = loadClassPathFile("jdl-samples-main/21-points.jh");
        String mermaid = JDLParser.jdlToMermaid(jdlString);
        Assert.assertNotNull(mermaid);
        Assert.assertTrue(mermaid.startsWith("classDiagram"));
    }

    @Test
    public void testExportToMermaidAllJDLs() throws IOException {
        Collection<String> jdlFiles = Stream.of(new File("src/test/resources/jdl-samples-main").listFiles())
                .filter(file -> !file.isDirectory() && (file.getName().endsWith(".jh") || file.getName().endsWith(".jdl")))
                .map(f -> "jdl-samples-main/" + f.getName())
                .collect(Collectors.toSet());
        Map<String, Exception> exceptions = new HashMap();
        for (String jdlFile : jdlFiles) {
            String jdlString = loadClassPathFile(jdlFile);
            try {
                System.out.println("Parsing " + jdlFile + " ...");
                String mermaid = JDLParser.jdlToMermaid(jdlString);
                Assert.assertNotNull(mermaid);
                Assert.assertTrue(mermaid.startsWith("classDiagram"));
            } catch (Exception e) {
                exceptions.put(jdlFile, e);
            }
        }
        Assert.assertEquals(new ArrayList<>(), new ArrayList<>(exceptions.keySet()));
    }

    static String loadClassPathFile(String fileName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            java.net.URL url = classLoader.getResource(fileName);
            if (url == null) {
                throw new Exception("File not found!");
            }
            java.nio.file.Path path = java.nio.file.Paths.get(url.toURI());
            return java.nio.file.Files.readString(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
