package com.pthariensflame.sylvia.ast.expressions

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.instrumentation.GenerateWrapper
import com.oracle.truffle.api.instrumentation.InstrumentableNode
import com.oracle.truffle.api.instrumentation.ProbeNode
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.parser.SourceSpan

@NodeInfo(
    shortName = "lit",
    description = "A literal expression"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
abstract class LiteralExpressionNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null
) : ExpressionNode(srcSpan) {
    override fun isInstrumentable(): Boolean = true

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        LiteralExpressionNodeWrapper(this, probe)
}
