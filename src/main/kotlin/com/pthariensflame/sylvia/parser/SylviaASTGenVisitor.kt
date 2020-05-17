package com.pthariensflame.sylvia.parser

import com.oracle.truffle.api.CompilerDirectives.*
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.source.Source
import com.oracle.truffle.api.source.SourceSection
import com.pthariensflame.sylvia.SylviaLanguage
import com.pthariensflame.sylvia.ast.SylviaNode
import com.pthariensflame.sylvia.ast.SylviaProgramBodyNode
import com.pthariensflame.sylvia.ast.SylviaProgramNode
import com.pthariensflame.sylvia.ast.declarations.DeclarationNode
import com.pthariensflame.sylvia.ast.expressions.ExpressionNode
import com.pthariensflame.sylvia.ast.expressions.literals.StringLiteralExpressionNode
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
    @JvmField val source: Source? = null,
) : SylviaBaseVisitor<SylviaNode>() {
    override fun visitProgram(ctx: SylviaParser.ProgramContext): SylviaNode =
        ctx.sourceSpan.let { srcSpan ->
            SylviaProgramNode(langInstance, null, source, srcSpan).apply {
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
            inner = visit(ctx.expr) as ExpressionNode
        }

    override fun visitEnclosedExpr(ctx: SylviaParser.EnclosedExprContext): ExpressionNode =
        visit(ctx.inner) as ExpressionNode

    override fun visitStraightSingleStringLiteral(ctx: SylviaParser.StraightSingleStringLiteralContext): StringLiteralExpressionNode =
        StringLiteralExpressionNode(ctx.sourceSpan)
            .apply {
            delimiterKind = StringLiteralExpressionNode.StringLiteralDelimiterKind.StraightSingleQuotes
            content = StringLiteralExpressionNode.processStringLiteralContent(ctx.text.subSequence(
                1..ctx.text.length - 2
            ))
        }

    override fun visitStraightDoubleStringLiteral(ctx: SylviaParser.StraightDoubleStringLiteralContext): StringLiteralExpressionNode =
        StringLiteralExpressionNode(ctx.sourceSpan)
            .apply {
            delimiterKind = StringLiteralExpressionNode.StringLiteralDelimiterKind.StraightDoubleQuotes
            content = StringLiteralExpressionNode.processStringLiteralContent(ctx.text.subSequence(
                1..ctx.text.length - 2
            ))
        }

    override fun visitStraightBacktickStringLiteral(ctx: SylviaParser.StraightBacktickStringLiteralContext): StringLiteralExpressionNode =
        StringLiteralExpressionNode(ctx.sourceSpan)
            .apply {
            delimiterKind = StringLiteralExpressionNode.StringLiteralDelimiterKind.StraightBackticks
            content = StringLiteralExpressionNode.processStringLiteralContent(ctx.text.subSequence(
                1..ctx.text.length - 2
            ))
        }

    override fun visitSmartSingleStringLiteral(ctx: SylviaParser.SmartSingleStringLiteralContext): StringLiteralExpressionNode =
        StringLiteralExpressionNode(ctx.sourceSpan)
            .apply {
            delimiterKind = StringLiteralExpressionNode.StringLiteralDelimiterKind.SmartSingleQuotes
            content = StringLiteralExpressionNode.processStringLiteralContent(ctx.text.subSequence(
                1..ctx.text.length - 2
            ))
        }

    override fun visitSmartDoubleStringLiteral(ctx: SylviaParser.SmartDoubleStringLiteralContext): StringLiteralExpressionNode =
        StringLiteralExpressionNode(ctx.sourceSpan)
            .apply {
            delimiterKind = StringLiteralExpressionNode.StringLiteralDelimiterKind.SmartDoubleQuotes
            content = StringLiteralExpressionNode.processStringLiteralContent(ctx.text.subSequence(
                1..ctx.text.length - 2
            ))
        }

    override fun visitSmartChevronStringLiteral(ctx: SylviaParser.SmartChevronStringLiteralContext): StringLiteralExpressionNode =
        StringLiteralExpressionNode(ctx.sourceSpan)
            .apply {
            delimiterKind = StringLiteralExpressionNode.StringLiteralDelimiterKind.SmartChevrons
            content = StringLiteralExpressionNode.processStringLiteralContent(ctx.text.subSequence(
                1..ctx.text.length - 2
            ))
        }

    override fun visitDeclarationStmt(ctx: SylviaParser.DeclarationStmtContext): StatementNode =
        visit(ctx.decl) as DeclarationNode

    override fun visitParenExpr(ctx: SylviaParser.ParenExprContext): ExpressionNode =
        visit(ctx.innerExpr) as ExpressionNode

    @ExportLibrary.Repeat(
        ExportLibrary(InteropLibrary::class),
//        ExportLibrary(LSPLibrary::class),
    )
    data class DoBindVisibilitySyntaxException(
        @JvmField val ctx: SylviaParser.VisDeclContext,
        @JvmField val source: Source?,
    ) : SylviaException() {
        @Contract("-> this", pure = true)
        override fun fillInStackTrace(): Throwable = this

        @Contract("-> null", pure = true)
        override fun getLocation(): Node? = null

        @Contract("-> true", pure = true)
        override fun isSyntaxError(): Boolean = true

        override fun getSourceLocation(): SourceSection? =
            source?.let { ctx.sourceSpan?.asSectionOf(source) }
    }

    @ExportLibrary.Repeat(
        ExportLibrary(InteropLibrary::class),
//        ExportLibrary(LSPLibrary::class),
    )
    data class MultiVisibilitySyntaxException(
        @JvmField val ctx: SylviaParser.VisDeclContext,
        @JvmField val source: Source?,
    ) : SylviaException() {
        @Contract("-> this", pure = true)
        override fun fillInStackTrace(): Throwable = this

        @Contract("-> null", pure = true)
        override fun getLocation(): Node? = null

        @Contract("-> true", pure = true)
        override fun isSyntaxError(): Boolean = true

        override fun getSourceLocation(): SourceSection? =
            source?.let { ctx.sourceSpan?.asSectionOf(source) }
    }

    override fun visitVisDecl(ctx: SylviaParser.VisDeclContext): DeclarationNode = when (ctx.inner) {
        is SylviaParser.DoBlockDeclContext -> throw DoBindVisibilitySyntaxException(ctx, source)
        is SylviaParser.VisDeclContext -> throw MultiVisibilitySyntaxException(ctx, source)
        else -> (visit(ctx.inner) as DeclarationNode).apply {
            visibility = when (ctx.vis) {
                is SylviaParser.VisibleVisibilityContext -> DeclarationNode.DeclarationVisibility.Visible
                is SylviaParser.HiddenVisibilityContext -> DeclarationNode.DeclarationVisibility.Hidden
                else -> throw IllegalStateException("Unreachable code")
            }
        }
    }

    override fun visitDocumentedDecl(ctx: SylviaParser.DocumentedDeclContext): SylviaNode =
        (visit(ctx.documented.inner) as DeclarationNode).apply {
            documentation = arrayOf(
                (visit(ctx.documented.documentation) as StringLiteralExpressionNode).content,
                *documentation
            )
        }

    @ExportLibrary.Repeat(
        ExportLibrary(InteropLibrary::class),
//        ExportLibrary(LSPLibrary::class),
    )
    data class ErrorNodeSyntaxException(
        @JvmField val errorNode: ErrorNode,
    ) : SylviaException() {
        @Contract("-> this", pure = true)
        override fun fillInStackTrace(): Throwable = this

        @Contract("-> null", pure = true)
        override fun getLocation(): Node? = null

        @Contract("-> true", pure = true)
        override fun isSyntaxError(): Boolean = true
    }

    @TruffleBoundary
    @Contract("_ -> fail", pure = true)
    override fun visitErrorNode(node: ErrorNode): Nothing =
        throw ErrorNodeSyntaxException(node)

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
