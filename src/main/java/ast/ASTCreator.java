package ast;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import java.util.Arrays;

public class ASTCreator {

    public static void createAst(String[] javaFiles, String[] jarFiles) {
        var parser = ASTParser.newParser(AST.JLS14);
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        setParserOptions(parser);
        parser.setEnvironment(jarFiles, new String[0], new String[0], true);
        var encodings = Arrays.stream(javaFiles)
                                      .map(path -> "UTF-8")
                                      .toArray(String[]::new);
        parser.createASTs(javaFiles, encodings, new String[0], new ASTRequester(), null);
    }

    private static void setParserOptions(ASTParser parser) {
        var options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
        parser.setCompilerOptions(options);
    }

}
