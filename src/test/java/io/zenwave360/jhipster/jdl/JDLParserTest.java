package io.zenwave360.jhipster.jdl;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class JDLParserTest {

    @Test
    public void testLoad21PointsJDL() throws IOException {
        String jdlString = loadClassPathFile("jdl-samples-main/21-points.jh");
        Map<String, Object> parsedJDL = JDLParser.parseJDL(jdlString);
        Assert.assertNotNull(parsedJDL);
    }

    static String loadClassPathFile(String fileName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            java.net.URL url = classLoader.getResource(fileName);
            if (url == null) {
                throw new Exception("File not found!");
            }
            java.nio.file.Path path = java.nio.file.Paths.get(url.toURI());
            return new String(java.nio.file.Files.readAllBytes(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
