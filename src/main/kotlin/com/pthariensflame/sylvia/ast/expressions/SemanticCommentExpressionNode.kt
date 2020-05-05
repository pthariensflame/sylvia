package com.pthariensflame.sylvia.ast.expressions

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.source.Source
import com.oracle.truffle.api.source.SourceSection
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.values.SylviaVal

@NodeInfo(
    shortName = "cmnt-expr",
    description = "A semantic comment in expression position"
)
@GenerateNodeFactory
@GenerateUncached(inherit = true)
@Introspectable
open class SemanticCommentExpressionNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null,
    @JvmField @Node.Child var inner: ExpressionNode = ImpossibleExpressionNode(),
) : ExpressionNode(srcSpan) {
    companion object {
        private const val MSG: String =
            "Semantic comment expression; should never be evaluated"
    }

    override fun isInstrumentable(): Boolean = false

    override fun executeVal(frame: VirtualFrame): SylviaVal {
        CompilerAsserts.neverPartOfCompilation(MSG)
        throw UnsupportedOperationException(MSG)
    }

    @TruffleBoundary
    override fun getSourceSection(): SourceSection {
        val src: Source = encapsulatingSourceSection.source
        return srcSpan?.asSectionOf(src) ?: src.createUnavailableSection()
    }
}