package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.*
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.parser.SourceSpan

@NodeInfo(
        shortName = "stmt",
        description = "A statement"
         )
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached
abstract class StatementNode
@JvmOverloads constructor(
        @JvmField val srcSpan: SourceSpan? = null,
                         ) : Node(), SylviaNode, InstrumentableNode {
    override fun isInstrumentable(): Boolean = true

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
            StatementNodeWrapper(this, probe)

    abstract fun executeVoid(frame: VirtualFrame)

    override fun hasTag(tag: Class<out Tag>): Boolean =
            tag.kotlin == StandardTags.StatementTag::class || super.hasTag(tag)

    @GenerateWrapper.OutgoingConverter
    protected fun outConv(@Suppress("UNUSED_PARAMETER") v: Any?): Any? = null
}
