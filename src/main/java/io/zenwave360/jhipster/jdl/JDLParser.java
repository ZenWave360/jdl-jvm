package io.zenwave360.jhipster.jdl;

import static com.oracle.truffle.js.runtime.JSContextOptions.ESM_EVAL_RETURNS_EXPORTS_NAME;

import com.oracle.truffle.js.lang.JavaScriptLanguage;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import javax.swing.plaf.synth.SynthDesktopIconUI;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class JDLParser {

    public static Map<String, Object> parseJDL(String JDL) throws IOException {
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        String jsCode = loadClassPathFile("io/zenwave360/jhipster/jdl/jdl-parser.js");
        Source jdlParsedJsSource = Source.newBuilder(JavaScriptLanguage.ID, jsCode, "jdl-parser.js").build();
        try (Context context = Context.create(JavaScriptLanguage.ID)) {
            context.eval(jdlParsedJsSource);
            Value parseJDL = context.getBindings(JavaScriptLanguage.ID).getMember("parseJDL");
            Value returned = null;
            try {
               returned = parseJDL.execute(JDL);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            return (Map<String, Object>) copy(returned.as(Map.class));
        }
    }

    private static Object copy(Object source) {
        if(source instanceof Map) {
            source = new HashMap<>((Map) source);
            ((HashMap<String, Object>) source).entrySet().forEach(e -> e.setValue(copy(e.getValue())));
        }
        return source;
    }

    private static String loadClassPathFile(String fileName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        java.net.URL url = classLoader.getResource(fileName);
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(url.toURI());
            return java.nio.file.Files.readString(path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
}
