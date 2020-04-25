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
import com.oracle.truffle.api.nodes.RootNode
import com.oracle.truffle.api.source.Source
import com.oracle.truffle.api.source.SourceSection
import com.pthariensflame.sylvia.SylviaLanguage
import com.pthariensflame.sylvia.SylviaTruffleTypeSystem
import com.pthariensflame.sylvia.ast.expressions.TopExpressionBodyNode
import com.pthariensflame.sylvia.parser.SourceSpan

@NodeInfo(
    shortName = "⊤-expr",
    description = "A top-level expression"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
@TypeSystemReference(SylviaTruffleTypeSystem::class)
open class TopExpressionNode
@JvmOverloads constructor(
    langInstance: SylviaLanguage? = null,
    frameDescriptor: FrameDescriptor? = null,
    @Node.Child @JvmField var bodyNode: TopExpressionBodyNode = TopExpressionBodyNode(),
) : RootNode(langInstance, frameDescriptor), SylviaNode, InstrumentableNode {
    val srcSpan: SourceSpan?
        inline get() = bodyNode.srcSpan

    override fun isInstrumentable(): Boolean = true

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        TopExpressionNodeWrapper(this, probe)

    override fun execute(frame: VirtualFrame): Any? = bodyNode.executeVal(frame)

    override fun hasTag(tag: Class<out Tag>): Boolean =
        tag.kotlin == StandardTags.RootTag::class || super.hasTag(tag)

    @TruffleBoundary
    override fun getSourceSection(): SourceSection {
        val src: Source = encapsulatingSourceSection.source
        return srcSpan?.run {
            src.createSection(start, len)
        } ?: src.createUnavailableSection()
    }
}
