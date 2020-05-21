import com.oracle.truffle.api.TruffleLanguage.Provider;
import com.pthariensflame.sylvia.SylviaLanguageProvider;

@SuppressWarnings("JavaRequiresAutoModule")
open module com.pthariensflame.sylvia {
    requires transitive org.graalvm.truffle;
    requires transitive org.graalvm.sdk;
    requires transitive org.graalvm.launcher;
    requires transitive org.graalvm.tools.api.lsp;
    requires transitive org.antlr.antlr4.runtime;
    requires transitive kotlin.stdlib;
    requires transitive kotlin.reflect;
//    requires transitive jline.reader;
//    requires transitive jline.style;
//    requires transitive jline.terminal;
//    requires jline.terminal.jansi;
    requires transitive org.jetbrains.annotations;

    exports com.pthariensflame.sylvia;
    exports com.pthariensflame.sylvia.ast;
    exports com.pthariensflame.sylvia.ast.expressions;
    exports com.pthariensflame.sylvia.ast.expressions.literals;
    exports com.pthariensflame.sylvia.ast.expressions.types;
    exports com.pthariensflame.sylvia.ast.statements;
    exports com.pthariensflame.sylvia.ast.declarations;
    exports com.pthariensflame.sylvia.values;
    exports com.pthariensflame.sylvia.values.types;
    exports com.pthariensflame.sylvia.parser;
    exports com.pthariensflame.sylvia.parser.antlr;
    exports com.pthariensflame.sylvia.shell;
    exports com.pthariensflame.sylvia.util;
    exports com.pthariensflame.sylvia.util.rosebush;

    provides Provider with SylviaLanguageProvider;
}
