package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.*
import com.oracle.truffle.api.nodes.ExecutableNode
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.SylviaLanguage
import com.pthariensflame.sylvia.values.NoReturnVal

@NodeInfo(
    shortName = "stmt",
    description = "A statement"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached
abstract class StatementNode
@JvmOverloads constructor(
    langInstance: SylviaLanguage? = null,
) : ExecutableNode(langInstance), SylviaNode, InstrumentableNode {
    override fun isInstrumentable(): Boolean = true

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        StatementNodeWrapper(this, probe)

    abstract override fun execute(frame: VirtualFrame): NoReturnVal

    override fun hasTag(tag: Class<out Tag>): Boolean =
        tag.kotlin == StandardTags.StatementTag::class || super.hasTag(tag)

    @GenerateWrapper.OutgoingConverter
    protected fun outConv(@Suppress("UNUSED_PARAMETER") v: Any?): Any? = null
}
