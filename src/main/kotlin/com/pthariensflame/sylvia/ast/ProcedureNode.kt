package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.dsl.TypeSystemReference
import com.oracle.truffle.api.frame.FrameDescriptor
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.*
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.source.Source
import com.oracle.truffle.api.source.SourceSection
import com.pthariensflame.sylvia.SylviaLanguage
import com.pthariensflame.sylvia.SylviaTruffleTypeSystem
import com.pthariensflame.sylvia.parser.SourceSpan
import org.jetbrains.annotations.Contract

@NodeInfo(
    shortName = "proc",
    description = "A procedure",
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
@TypeSystemReference(SylviaTruffleTypeSystem::class)
open class ProcedureNode
@JvmOverloads constructor(
    langInstance: SylviaLanguage? = null,
    frameDescriptor: FrameDescriptor? = null,
    @JvmField val srcSpan: SourceSpan? = null,
    @Node.Child @JvmField var bodyNode: ProcedureBodyNode = NormalProcedureBodyNode(),
) : SylviaTopNode(langInstance, frameDescriptor) {
    @Contract("-> new")
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

    @TruffleBoundary
    override fun getSourceSection(): SourceSection {
        val src: Source = encapsulatingSourceSection.source
        return srcSpan?.asSectionOf(src) ?: src.createUnavailableSection()
    }
}
