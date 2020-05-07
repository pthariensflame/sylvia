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
    shortName = "sylv-prog",
    description = "A Sylvia program",
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
@TypeSystemReference(SylviaTruffleTypeSystem::class)
open class SylviaProgramNode
@JvmOverloads constructor(
    langInstance: SylviaLanguage? = null,
    frameDescriptor: FrameDescriptor? = null,
    @Node.Child @JvmField var bodyNode: SylviaProgramBodyNode = SylviaProgramBodyNode(),
) : SylviaTopNode(langInstance, frameDescriptor, bodyNode.srcSpan) {
    @Contract("-> new")
    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        SylviaProgramNodeWrapper(this, probe)

    override fun execute(frame: VirtualFrame): Any? =
        bodyNode.executeVal(frame)
}
