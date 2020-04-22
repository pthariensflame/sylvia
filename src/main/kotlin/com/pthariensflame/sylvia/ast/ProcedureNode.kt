package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.TypeSystemReference
import com.oracle.truffle.api.frame.FrameDescriptor
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.*
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.nodes.RootNode
import com.pthariensflame.sylvia.SylviaLanguage
import com.pthariensflame.sylvia.SylviaTruffleTypeSystem
import com.pthariensflame.sylvia.parser.SourceSpan

@NodeInfo(
        shortName = "proc",
        description = "A procedure"
         )
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached
@TypeSystemReference(SylviaTruffleTypeSystem::class)
open class ProcedureNode
@JvmOverloads constructor(
        langInstance: SylviaLanguage? = null,
        frameDescriptor: FrameDescriptor? = null,
        @JvmField val srcSpan: SourceSpan? = null,
        @Node.Child @JvmField var bodyNode: ProcedureBodyNode = ProcedureBodyNode(),
                         ) : RootNode(langInstance, frameDescriptor), SylviaNode, InstrumentableNode {
    override fun isInstrumentable(): Boolean = super.isInstrumentable()

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
            ProcedureNodeWrapper(this, probe)

    override fun execute(frame: VirtualFrame): Any? {
        bodyNode.executeVoid(frame)
        return null
    }

    override fun hasTag(tag: Class<out Tag>): Boolean =
            tag.kotlin == StandardTags.RootTag::class || super.hasTag(tag)

    @GenerateWrapper.OutgoingConverter
    protected fun outConv(@Suppress("UNUSED_PARAMETER") v: Any?): Any? = null
}
