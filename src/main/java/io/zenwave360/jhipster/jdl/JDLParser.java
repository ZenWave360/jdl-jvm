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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDLParser {

    public static Map<String, Object> parseJDL(String JDL) throws IOException {
        return parseJDL(JDL, Collections.emptyMap());
    }

    public static Map<String, Object> parseJDL(String JDL, Map<String, String> configuration) throws IOException {
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        String jsCode = loadClassPathFile("io/zenwave360/jhipster/jdl/jdl-parser.js");
        Source jdlParsedJsSource = Source.newBuilder(JavaScriptLanguage.ID, jsCode, "jdl-parser.js").build();
        try (Context context = Context.create(JavaScriptLanguage.ID)) {
            context.eval(jdlParsedJsSource);
            Value parseJDL = context.getBindings(JavaScriptLanguage.ID).getMember("parseJDL");
            Value returned = null;
            try {
               returned = parseJDL.execute(JDL, configuration);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            return (Map<String, Object>) copy(returned.as(Map.class));
        }
    }

    public static String jdlToMermaid(String JDL) throws IOException {
        return jdlToMermaid(JDL, Collections.emptyMap());
    }
    public static String jdlToMermaid(String JDL, Map<String, String> configuration) throws IOException {
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        String jsCode = loadClassPathFile("io/zenwave360/jhipster/jdl/jdl-parser.js");
        Source jdlParsedJsSource = Source.newBuilder(JavaScriptLanguage.ID, jsCode, "jdl-parser.js").build();
        try (Context context = Context.create(JavaScriptLanguage.ID)) {
            context.eval(jdlParsedJsSource);
            Value jdlToMermaid = context.getBindings(JavaScriptLanguage.ID).getMember("jdlToMermaid");
            Value returned = null;
            try {
                returned = jdlToMermaid.execute(JDL, configuration);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            return returned.asString();
        }
    }

    private static Object copy(Object source) {
        if(source instanceof Map) {
            source = new HashMap<>((Map) source);
            ((HashMap<String, Object>) source).entrySet().forEach(e -> e.setValue(copy(e.getValue())));
        } else if(source instanceof List) {
            source = new ArrayList<>((List) source);
        }
        return source;
    }

    private static String loadClassPathFile(String fileName) throws IOException {
        try {
            return new String(JDLParser.class.getClassLoader().getResourceAsStream(fileName).readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
