package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.*
import com.oracle.truffle.api.nodes.BlockNode
import com.oracle.truffle.api.nodes.ExecutableNode
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.SylviaLanguage
import com.pthariensflame.sylvia.values.NoReturnVal

@NodeInfo(
    shortName = "proc",
    description = "A procedure"
)
@GenerateNodeFactory
@GenerateWrapper
open class ProcedureBodyNode
@JvmOverloads constructor(
    langInstance: SylviaLanguage? = null,
    @Node.Children @JvmField var statements: Array<StatementNode> = emptyArray(),
) : ExecutableNode(langInstance), SylviaNode, InstrumentableNode {
    override fun isInstrumentable(): Boolean = true

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        ProcedureBodyNodeWrapper(this, probe)

    override fun execute(outerFrame: VirtualFrame): NoReturnVal {
        val block: BlockNode<StatementNode> =
            BlockNode.create(statements) { frame: VirtualFrame, node: StatementNode, _: Int, _: Int ->
                node.execute(frame)
            }
        block.executeVoid(outerFrame, BlockNode.NO_ARGUMENT)
        return NoReturnVal
    }

    override fun hasTag(tag: Class<out Tag>): Boolean =
        tag.kotlin == StandardTags.RootBodyTag::class || super.hasTag(tag)
}
