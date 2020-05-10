package com.pthariensflame.sylvia.ast.statements

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.GenerateWrapper
import com.oracle.truffle.api.instrumentation.InstrumentableNode
import com.oracle.truffle.api.instrumentation.ProbeNode
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeCost
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.ast.expressions.ExpressionNode
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.util.TruffleUtil
import com.pthariensflame.sylvia.util.runAtomic

@NodeInfo(
    shortName = "expr-stmt",
    description = "An expression statement",
    cost = NodeCost.NONE
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
open class ExpressionStatementNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null,
) : StatementNode(srcSpan) {
    @field:Node.Child
    private var _inner: ExpressionNode? = null

    var inner: ExpressionNode
        get() = _inner!!
        set(newInner) = runAtomic {
            if (TruffleUtil.injectBranchProbability(CompilerDirectives.LIKELY_PROBABILITY, _inner == null)) {
                _inner = insert(newInner)
                notifyInserted(newInner)
            } else {
                _inner!!.replace(newInner)
            }
        }

    override fun isInstrumentable(): Boolean = true

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        ExpressionStatementNodeWrapper(this, probe)

    override fun executeVoid(frame: VirtualFrame) =
        inner.executeVoid(frame)
}
