package com.pthariensflame.sylvia.ast.statements

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
import com.pthariensflame.sylvia.ast.expressions.ImpossibleExpressionNode
import com.pthariensflame.sylvia.parser.SourceSpan

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
    @JvmField @Node.Child var inner: ExpressionNode = ImpossibleExpressionNode(),
) : StatementNode(srcSpan) {
    override fun isInstrumentable(): Boolean = true

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        ExpressionStatementNodeWrapper(this, probe)

    override fun executeVoid(frame: VirtualFrame) {
        inner.executeVal(frame)
    }
}
