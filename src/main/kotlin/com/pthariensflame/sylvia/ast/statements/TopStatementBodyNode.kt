package com.pthariensflame.sylvia.ast.statements

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
import com.pthariensflame.sylvia.ast.expressions.ImpossibleExpressionNode
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.values.SylviaVal


@NodeInfo(
    shortName = "⊤-expr-body",
    description = "A top-level expression's “body”",
    cost = NodeCost.NONE
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
open class TopStatementBodyNode
@JvmOverloads internal constructor(
    @JvmField @Node.Child var inner: StatementNode = ImpossibleStatementNode(),
) : StatementNode(inner.srcSpan) {
    override fun isInstrumentable(): Boolean = true

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        TopStatementBodyNodeWrapper(this, probe)

    override fun executeVoid(frame: VirtualFrame) = inner.executeVoid(frame)

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
