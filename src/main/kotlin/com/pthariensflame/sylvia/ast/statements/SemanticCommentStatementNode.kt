package com.pthariensflame.sylvia.ast.statements

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
import com.pthariensflame.sylvia.parser.createSection

@NodeInfo(
    shortName = "cmnt-stmt",
    description = "A semantic comment in statement position"
)
@GenerateNodeFactory
@GenerateUncached(inherit = true)
@Introspectable
open class SemanticCommentStatementNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null,
    @JvmField @Node.Child var inner: StatementNode = ImpossibleStatementNode(),
) : StatementNode(srcSpan) {
    companion object {
        private const val MSG: String =
            "Semantic comment statement; should never be executed"
    }

    override fun isInstrumentable(): Boolean = false

    override fun executeVoid(frame: VirtualFrame) {
        CompilerAsserts.neverPartOfCompilation(MSG)
        throw UnsupportedOperationException(MSG)
    }

    @TruffleBoundary
    override fun getSourceSection(): SourceSection {
        val src: Source = encapsulatingSourceSection.source
        return srcSpan?.asSectionOf(src) ?: src.createUnavailableSection()
    }
}