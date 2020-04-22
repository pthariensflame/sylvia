package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.*
import com.oracle.truffle.api.nodes.BlockNode
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.parser.SourceSpan

@NodeInfo(
        shortName = "proc",
        description = "A procedure"
         )
@GenerateNodeFactory
@GenerateWrapper
open class ProcedureBodyNode
@JvmOverloads constructor(
        @JvmField val srcSpan: SourceSpan? = null,
        @Node.Children @JvmField var statements: Array<StatementNode> = emptyArray(),
                         ) : Node(), SylviaNode, InstrumentableNode {
    override fun isInstrumentable(): Boolean = true

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
            ProcedureBodyNodeWrapper(this, probe)

    open fun executeVoid(outerFrame: VirtualFrame) {
        val block: BlockNode<StatementNode> =
                BlockNode.create(statements) { frame: VirtualFrame, node: StatementNode, _: Int, _: Int ->
                    node.executeVoid(frame)
                }
        block.executeVoid(outerFrame, BlockNode.NO_ARGUMENT)
    }

    override fun hasTag(tag: Class<out Tag>): Boolean =
            tag.kotlin == StandardTags.RootBodyTag::class || super.hasTag(tag)

    @GenerateWrapper.OutgoingConverter
    protected fun outConv(@Suppress("UNUSED_PARAMETER") v: Any?): Any? = null
}
