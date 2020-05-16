package com.pthariensflame.sylvia.ast

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
import com.pthariensflame.sylvia.parser.SourceSpan

@NodeInfo(
    shortName = "proc-body",
    description = "A procedure body"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
abstract class ProcedureBodyNode
@JvmOverloads constructor(
    @JvmField final override val srcSpan: SourceSpan? = null,
) : Node(), SylviaNode, InstrumentableNode {
    override fun isInstrumentable(): Boolean = true

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        ProcedureBodyNodeWrapper(this, probe)

    abstract fun executeVoid(outerFrame: VirtualFrame)

    override fun hasTag(tag: Class<out Tag>): Boolean =
        tag.kotlin == StandardTags.RootBodyTag::class || super.hasTag(tag)

    @GenerateWrapper.OutgoingConverter
    protected fun outConv(@Suppress("UNUSED_PARAMETER") v: Any?): Any? = null

    @TruffleBoundary
    override fun getSourceSection(): SourceSection? =
        encapsulatingSourceSection?.source?.let { src ->
            srcSpan?.asSectionOf(src) ?: src.createUnavailableSection()
        }
}
