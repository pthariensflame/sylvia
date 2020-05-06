package com.pthariensflame.sylvia.parser

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.nodes.Node
import com.pthariensflame.sylvia.SylviaLanguage
import com.pthariensflame.sylvia.ast.SylviaNode
import com.pthariensflame.sylvia.ast.declarations.DeclarationNode
import com.pthariensflame.sylvia.ast.expressions.ExpressionNode
import com.pthariensflame.sylvia.ast.statements.ExpressionStatementNode
import com.pthariensflame.sylvia.ast.statements.StatementNode
import com.pthariensflame.sylvia.parser.antlr.SylviaBaseVisitor
import com.pthariensflame.sylvia.parser.antlr.SylviaParser
import com.pthariensflame.sylvia.values.SylviaException
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ErrorNode
import org.jetbrains.annotations.Contract

// TODO
class SylviaASTGenVisitor
@JvmOverloads constructor(
    @JvmField val langInstance: SylviaLanguage? = null,
) : SylviaBaseVisitor<SylviaNode>() {
    override fun visitExpressionStmt(ctx: SylviaParser.ExpressionStmtContext): StatementNode =
        ExpressionStatementNode(super.visit(ctx.expr) as ExpressionNode)

    override fun visitDeclarationStmt(ctx: SylviaParser.DeclarationStmtContext): StatementNode =
        super.visit(ctx.decl) as DeclarationNode

    override fun visitParenExpr(ctx: SylviaParser.ParenExprContext): ExpressionNode =
        super.visit(ctx.innerExpr) as ExpressionNode

    @ExportLibrary(InteropLibrary::class)
    object ErrorNodeSyntaxException : SylviaException() {
        @Contract("-> this", pure = true)
        override fun fillInStackTrace(): Throwable = this

        @Contract("-> null", pure = true)
        override fun getLocation(): Node? = null

        @Contract("-> true", pure = true)
        override fun isSyntaxError(): Boolean = true
    }

    @TruffleBoundary
    @Contract("_ -> fail", pure = true)
    override fun visitErrorNode(node: ErrorNode): SylviaNode =
        throw ErrorNodeSyntaxException

    companion object {
        @JvmStatic
        @get:Contract(pure = true)
        private val ParserRuleContext.sourceSpan: SourceSpan?
            get() {
                val s = getStart().startIndex
                val e = getStop().stopIndex
                if (s == -1 || e == -1) {
                    return null
                } else {
                    return SourceSpan(s, e - s + 1)
                }
            }
    }
}
