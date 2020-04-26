package com.pthariensflame.sylvia.parser

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary
import com.oracle.truffle.api.TruffleException
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.TruffleObject
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import com.oracle.truffle.api.nodes.Node
import com.pthariensflame.sylvia.SylviaLanguage
import com.pthariensflame.sylvia.ast.ProcedureBodyNode
import com.pthariensflame.sylvia.ast.ProcedureNode
import com.pthariensflame.sylvia.ast.SylviaNode
import com.pthariensflame.sylvia.ast.expressions.ExpressionNode
import com.pthariensflame.sylvia.ast.statements.StatementNode
import com.pthariensflame.sylvia.parser.antlr.SylviaBaseVisitor
import com.pthariensflame.sylvia.parser.antlr.SylviaParser
import com.pthariensflame.sylvia.values.SylviaException
import org.antlr.v4.runtime.tree.ErrorNode

class SylviaASTGenVisitor
@JvmOverloads constructor(
    @JvmField val langInstance: SylviaLanguage? = null,
) : SylviaBaseVisitor<SylviaNode>() {
    @ExportLibrary(InteropLibrary::class)
    object ErrorNodeSyntaxException: SylviaException() {
        override fun getLocation(): Node? = null
        override fun isSyntaxError(): Boolean = true
    }
    @TruffleBoundary
    override fun visitErrorNode(node: ErrorNode): SylviaNode = throw ErrorNodeSyntaxException

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
