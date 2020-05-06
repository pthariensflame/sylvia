package com.pthariensflame.sylvia.parser

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.nodes.Node
import com.pthariensflame.sylvia.SylviaLanguage
import com.pthariensflame.sylvia.ast.ProcedureBodyNode
import com.pthariensflame.sylvia.ast.ProcedureNode
import com.pthariensflame.sylvia.ast.SylviaNode
import com.pthariensflame.sylvia.ast.expressions.ExpressionNode
import com.pthariensflame.sylvia.ast.statements.StatementNode
import com.pthariensflame.sylvia.parser.SourceSpan.Companion.sourceSpan
import com.pthariensflame.sylvia.parser.antlr.SylviaBaseVisitor
import com.pthariensflame.sylvia.parser.antlr.SylviaParser
import com.pthariensflame.sylvia.values.SylviaException
import org.antlr.v4.runtime.tree.ErrorNode
import org.jetbrains.annotations.Contract
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.text.RegexOption.CANON_EQ
import kotlin.text.RegexOption.DOT_MATCHES_ALL

@OptIn(ExperimentalContracts::class)
class SylviaASTGenVisitor
@JvmOverloads constructor(
    @JvmField val langInstance: SylviaLanguage? = null,
) : SylviaBaseVisitor<SylviaNode>() {
    companion object {
        private val commentCheckRegex: Regex by lazy {
            Regex(
                """^#([^\p{Space}()⦅⦆]*)(?:\(.*\)|⦅.*⦆)([^\p{Space}()⦅⦆]*)#$""",
                setOf(CANON_EQ, DOT_MATCHES_ALL)
            )
        }

        @JvmStatic
        @Contract(pure = true)
        fun checkMatchedComment(txt: CharSequence): Boolean {
            contract {
                returns()
            }
            return commentCheckRegex.also {
                CompilerAsserts.partialEvaluationConstant<Regex>(it)
            }.matchEntire(txt)?.run {
                groups[1] == groups[2]
            } ?: false
        }
    }

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
    @Contract(pure = true)
    override fun visitErrorNode(node: ErrorNode): SylviaNode =
        throw ErrorNodeSyntaxException

    @TruffleBoundary
    @Contract("-> null", pure = true)
    override fun defaultResult(): SylviaNode? = null

    override fun visitProcedure_decl(ctx: SylviaParser.Procedure_declContext): ProcedureNode =
        ProcedureNode(
            langInstance,
            null, // TODO
            ctx.sourceSpan(),
            ProcedureBodyNode(
                ctx.body.sourceSpan(),
                ctx.body.stmts.map {
                    visit(it) as StatementNode
                }.toTypedArray()
            )
        )

    override fun visitParenExpr(ctx: SylviaParser.ParenExprContext): ExpressionNode =
        super.visit(ctx.innerExpr) as ExpressionNode
}
