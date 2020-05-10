package com.pthariensflame.sylvia.parser

import com.oracle.truffle.api.CompilerDirectives.*
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.nodes.Node
import com.pthariensflame.sylvia.SylviaLanguage
import com.pthariensflame.sylvia.ast.SylviaNode
import com.pthariensflame.sylvia.ast.SylviaProgramBodyNode
import com.pthariensflame.sylvia.ast.SylviaProgramNode
import com.pthariensflame.sylvia.ast.declarations.DeclarationNode
import com.pthariensflame.sylvia.ast.expressions.ExpressionNode
import com.pthariensflame.sylvia.ast.statements.ExpressionStatementNode
import com.pthariensflame.sylvia.ast.statements.StatementNode
import com.pthariensflame.sylvia.parser.antlr.SylviaBaseVisitor
import com.pthariensflame.sylvia.parser.antlr.SylviaParser
import com.pthariensflame.sylvia.util.TruffleUtil
import com.pthariensflame.sylvia.values.SylviaException
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ErrorNode
import org.jetbrains.annotations.Contract

// TODO
class SylviaASTGenVisitor
@JvmOverloads constructor(
    @JvmField val langInstance: SylviaLanguage? = null,
) : SylviaBaseVisitor<SylviaNode>() {
    override fun visitProgram(ctx: SylviaParser.ProgramContext): SylviaNode =
        ctx.sourceSpan.let { srcSpan ->
            SylviaProgramNode(langInstance, null, srcSpan).apply {
                bodyNode = SylviaProgramBodyNode(srcSpan).apply {
                    val stmtNodes: Iterable<StatementNode>
                    val endExprNode: ExpressionNode?
                    val lastStatement = ctx.stmts.lastOrNull()
                    when {
                        TruffleUtil.injectBranchProbability(
                            UNLIKELY_PROBABILITY,
                            ctx.stmts.isEmpty()
                        ) -> {
                            stmtNodes = emptyList()
                            endExprNode = null
                        }
                        TruffleUtil.injectBranchProbability(
                            LIKELY_PROBABILITY,
                            lastStatement is SylviaParser.ExpressionStmtContext
                        ) -> {
                            stmtNodes = ctx.stmts.subList(0, ctx.stmts.size - 2).map {
                                visit(it) as StatementNode
                            }
                            endExprNode = visit(lastStatement.expr) as ExpressionNode
                        }
                        else -> {
                            stmtNodes = ctx.stmts.map {
                                visit(it) as StatementNode
                            }
                            endExprNode = null
                        }
                    }
                    addStatements(stmtNodes)
                    endExpr = endExprNode
                }
            }
        }

    override fun visitExpressionStmt(ctx: SylviaParser.ExpressionStmtContext): StatementNode =
        ExpressionStatementNode(ctx.sourceSpan).apply {
            inner = super.visit(ctx.expr) as ExpressionNode
        }

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
                    return SourceSpan(s..e)
                }
            }
    }
}
