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
import com.oracle.truffle.api.nodes.NodeCost
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.source.SourceSection
import com.pthariensflame.sylvia.SylviaLanguage
import com.pthariensflame.sylvia.SylviaTruffleTypeSystem
import com.pthariensflame.sylvia.ast.statements.TopStatementBodyNode
import com.pthariensflame.sylvia.parser.SourceSpan
import org.jetbrains.annotations.Contract

@NodeInfo(
    shortName = "⊤-stmt",
    description = "A top-level statment",
    cost = NodeCost.NONE
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
@TypeSystemReference(SylviaTruffleTypeSystem::class)
open class TopStatementNode
@JvmOverloads constructor(
    langInstance: SylviaLanguage? = null,
    frameDescriptor: FrameDescriptor? = null,
    @Node.Child @JvmField var bodyNode: TopStatementBodyNode = TopStatementBodyNode(),
) : SylviaTopNode(langInstance, frameDescriptor) {
    val srcSpan: SourceSpan?
        inline get() = bodyNode.srcSpan

    override fun isInstrumentable(): Boolean = true

    @Contract("-> new")
    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        TopStatementNodeWrapper(this, probe)

    override fun execute(frame: VirtualFrame): Any? {
        bodyNode.executeVoid(frame)
        return null
    }

    override fun hasTag(tag: Class<out Tag>): Boolean =
        tag.kotlin == StandardTags.RootTag::class || super.hasTag(tag)

    @TruffleBoundary(allowInlining = true)
    override fun getSourceSection(): SourceSection = bodyNode.sourceSection
}
