package com.pthariensflame.sylvia.ast.expressions

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.CompilerDirectives.LIKELY_PROBABILITY
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
import com.pthariensflame.sylvia.util.TruffleUtil
import com.pthariensflame.sylvia.util.locking
import com.pthariensflame.sylvia.util.runAtomic
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
) : ExpressionNode(srcSpan) {
    companion object {
        private const val MSG: String =
            "Semantic comment expression; should never be evaluated"
    }

    @field:Node.Child
    private var _inner: ExpressionNode? = null

    var inner: ExpressionNode
    get() = _inner!!
    set(newInner) = runAtomic {
        if (TruffleUtil.injectBranchProbability(LIKELY_PROBABILITY, _inner == null)) {
            _inner = insert(newInner)
            notifyInserted(newInner)
        } else {
            _inner!!.replace(newInner)
        }
    }

    override fun isInstrumentable(): Boolean = false

    override fun executeVal(frame: VirtualFrame): SylviaVal {
        CompilerAsserts.neverPartOfCompilation(MSG)
        throw UnsupportedOperationException(MSG)
    }

    @TruffleBoundary
    override fun getSourceSection(): SourceSection? =
        encapsulatingSourceSection?.source?.let { src ->
            srcSpan?.asSectionOf(src) ?: src.createUnavailableSection()
        }
}