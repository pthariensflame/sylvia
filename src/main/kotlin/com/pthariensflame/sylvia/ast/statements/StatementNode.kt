package com.pthariensflame.sylvia.ast.statements

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.*
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.source.Source
import com.oracle.truffle.api.source.SourceSection
import com.pthariensflame.sylvia.ast.SylviaNode
import com.pthariensflame.sylvia.parser.SourceSpan

@NodeInfo(
    shortName = "stmt",
    description = "A statement"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
abstract class StatementNode
@JvmOverloads constructor(
    @JvmField final override val srcSpan: SourceSpan? = null,
) : Node(), SylviaNode, InstrumentableNode {
    abstract override fun isInstrumentable(): Boolean

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        StatementNodeWrapper(this, probe)

    abstract fun executeVoid(frame: VirtualFrame)

    override fun hasTag(tag: Class<out Tag>): Boolean =
        tag.kotlin == StandardTags.StatementTag::class || super.hasTag(tag)

    @GenerateWrapper.OutgoingConverter
    protected fun outConv(@Suppress("UNUSED_PARAMETER") v: Any?): Any? = null

    @TruffleBoundary
    override fun getSourceSection(): SourceSection? =
        encapsulatingSourceSection?.source?.let { src ->
            srcSpan?.asSectionOf(src) ?: src.createUnavailableSection()
        }
}
