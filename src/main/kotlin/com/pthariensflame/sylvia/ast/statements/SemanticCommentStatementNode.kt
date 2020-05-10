package com.pthariensflame.sylvia.ast.statements

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.source.Source
import com.oracle.truffle.api.source.SourceSection
import com.pthariensflame.sylvia.ast.expressions.ExpressionNode
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.util.TruffleUtil
import com.pthariensflame.sylvia.util.runAtomic

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
) : StatementNode(srcSpan) {
    companion object {
        private const val MSG: String =
            "Semantic comment statement; should never be executed"
    }

    @field:Node.Child
    private var _inner: StatementNode? = null

    var inner: StatementNode
        get() = _inner!!
        set(newInner) = runAtomic {
            if (TruffleUtil.injectBranchProbability(CompilerDirectives.LIKELY_PROBABILITY, _inner == null)) {
                _inner = insert(newInner)
                notifyInserted(newInner)
            } else {
                _inner!!.replace(newInner)
            }
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