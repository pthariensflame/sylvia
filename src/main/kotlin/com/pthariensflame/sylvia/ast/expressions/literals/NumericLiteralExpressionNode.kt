package com.pthariensflame.sylvia.ast.expressions.literals

import com.oracle.truffle.api.dsl.*
import com.oracle.truffle.api.instrumentation.GenerateWrapper
import com.oracle.truffle.api.instrumentation.InstrumentableNode
import com.oracle.truffle.api.instrumentation.ProbeNode
import com.oracle.truffle.api.nodes.NodeInfo

@NodeInfo(
    shortName = "num-lit",
    description = "A numeric literal expression"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
@NodeField(name = "content", type = String::class)
abstract class NumericLiteralExpressionNode : LiteralExpressionNode() {
    abstract fun getContent(): String

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        NumericLiteralExpressionNodeWrapper(this, probe)
}