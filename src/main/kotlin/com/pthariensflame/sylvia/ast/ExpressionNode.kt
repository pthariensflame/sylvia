package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.*
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.values.SylviaVal

@NodeInfo(
        shortName = "expr",
        description = "An expression"
         )
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached
abstract class ExpressionNode : Node(), SylviaNode, InstrumentableNode {
    override abstract fun isInstrumentable(): Boolean

    abstract fun executeVal(frame: VirtualFrame): SylviaVal

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
            ExpressionNodeWrapper(this, probe)

    override fun hasTag(tag: Class<out Tag>): Boolean =
            tag.kotlin == StandardTags.ExpressionTag::class || super.hasTag(tag)
}