package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.frame.FrameDescriptor
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.GenerateWrapper
import com.oracle.truffle.api.instrumentation.InstrumentableNode
import com.oracle.truffle.api.instrumentation.ProbeNode
import com.oracle.truffle.api.nodes.BlockNode
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.nodes.RootNode
import com.pthariensflame.sylvia.SylviaLanguage

@NodeInfo(
    shortName = "proc",
    description = "A procedure"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached
open class ProcedureNode
@JvmOverloads constructor(
    @Node.Child @JvmField var blockNode: BlockNode<StatementNode>? = null,
    langInstance: SylviaLanguage? = null,
    frameDescriptor: FrameDescriptor? = null,
) : RootNode(langInstance, frameDescriptor), SylviaNode, InstrumentableNode {
    override fun isInstrumentable(): Boolean = super.isInstrumentable()

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        ProcedureNodeWrapper(this, probe)

    override fun execute(frame: VirtualFrame): Any {
        TODO("Not yet implemented")
    }
}
