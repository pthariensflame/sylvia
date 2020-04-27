open module com.pthariensflame.sylvia {
    requires transitive org.graalvm.truffle;
    requires org.graalvm.sdk;
    requires org.graalvm.launcher;
    requires com.oracle.truffle.truffle_nfi;
    requires transitive kotlin.stdlib;
    requires transitive kotlin.stdlib.jdk8;
    requires transitive kotlin.stdlib.jdk7;
    requires transitive kotlin.stdlib.common;

    exports com.pthariensflame.sylvia.ast;
    exports com.pthariensflame.sylvia.ast.expressions;
    exports com.pthariensflame.sylvia.ast.statements;
    exports com.pthariensflame.sylvia.ast.declarations;
    exports com.pthariensflame.sylvia.values;
    exports com.pthariensflame.sylvia.parser;
    exports com.pthariensflame.sylvia.parser.antlr;
    exports com.pthariensflame.sylvia.shell;
}
