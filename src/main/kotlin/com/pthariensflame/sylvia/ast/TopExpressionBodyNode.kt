package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.*
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeCost
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.source.Source
import com.oracle.truffle.api.source.SourceSection
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.values.SylviaVal


@NodeInfo(
        shortName = "⊤-expr-body",
        description = "A top-level expression's “body”",
        cost = NodeCost.NONE
         )
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached
@Introspectable
open class TopExpressionBodyNode
@JvmOverloads internal constructor(
    srcSpan: SourceSpan? = null,
    @JvmField @Node.Child var inner: ExpressionNode = ImpossibleExpressionNode(),
                                  ) : ExpressionNode(srcSpan) {
    override fun isInstrumentable(): Boolean = true

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode {
        return TopExpressionBodyNodeWrapper(this, probe)
    }

    override fun executeVal(frame: VirtualFrame): SylviaVal = inner.executeVal(frame)

    override fun executeBool(frame: VirtualFrame): Boolean = inner.executeBool(frame)

    override fun executeByte(frame: VirtualFrame): Byte = inner.executeByte(frame)

    override fun executeShort(frame: VirtualFrame): Short = inner.executeShort(frame)

    override fun executeInt(frame: VirtualFrame): Int = inner.executeInt(frame)

    override fun executeLong(frame: VirtualFrame): Long = inner.executeLong(frame)

    override fun executeFloat(frame: VirtualFrame): Float = inner.executeFloat(frame)

    override fun executeDouble(frame: VirtualFrame): Double = inner.executeDouble(frame)

    override fun executeString(frame: VirtualFrame): String = inner.executeString(frame)

    override fun hasTag(tag: Class<out Tag>): Boolean =
            tag.kotlin == StandardTags.RootBodyTag::class || super.hasTag(tag)

    @TruffleBoundary
    override fun getSourceSection(): SourceSection {
        val src: Source = encapsulatingSourceSection.source
        return srcSpan?.run {
            src.createSection(start, len)
        } ?: src.createUnavailableSection()
    }
}
