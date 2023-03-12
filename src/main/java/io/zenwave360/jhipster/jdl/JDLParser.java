package io.zenwave360.jhipster.jdl;

import com.oracle.truffle.js.lang.JavaScriptLanguage;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JDLParser {

    private static Value parseJDL;

    public static Map<String, Object> parseJDL(String JDL) throws IOException {
        return parseJDL(JDL, Collections.emptyMap());
    }

    public static Map<String, Object> parseJDL(String JDL, Map<String, String> configuration) throws IOException {
        Value returned = null;
        try {
            returned = getParseJDL().execute(JDL, configuration);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return (Map<String, Object>) copy(returned.as(Map.class));
    }

    private static Object copy(Object source) {
        if(source instanceof Map) {
            source = new LinkedHashMap<>((Map) source);
            ((HashMap<String, Object>) source).entrySet().forEach(e -> e.setValue(copy(e.getValue())));
        } else if(source instanceof List) {
            source = new ArrayList<>((List) source);
        }
        return source;
    }

    private static Value getParseJDL() throws IOException {
        if(parseJDL == null) {
            System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
            String jsCode = loadClassPathFile("io/zenwave360/jhipster/jdl/jdl-parser.js");
            Source jdlParsedJsSource = Source.newBuilder(JavaScriptLanguage.ID, jsCode, "jdl-parser.js").build();
            Context context = Context.create(JavaScriptLanguage.ID);
            context.eval(jdlParsedJsSource);
            parseJDL = context.getBindings(JavaScriptLanguage.ID).getMember("parseJDL");
        }
        return parseJDL;
    }

    private static String loadClassPathFile(String fileName) throws IOException {
        try {
            return new String(JDLParser.class.getClassLoader().getResourceAsStream(fileName).readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
