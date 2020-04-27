package com.pthariensflame.sylvia.ast.declarations

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
import com.pthariensflame.sylvia.ast.statements.StatementNode
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.parser.createSection

@NodeInfo(
    shortName = "stmt",
    description = "A statement"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
abstract class DeclarationNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null,
) : StatementNode(srcSpan), SylviaNode, InstrumentableNode {
    abstract override fun isInstrumentable(): Boolean

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        DeclarationNodeWrapper(this, probe)

    abstract override fun executeVoid(frame: VirtualFrame)

    override fun hasTag(tag: Class<out Tag>): Boolean =
        tag.kotlin == StandardTags.StatementTag::class || super.hasTag(tag)

    @GenerateWrapper.OutgoingConverter
    protected fun outConvDecl(@Suppress("UNUSED_PARAMETER") v: Any?): Any? = null

    @TruffleBoundary
    override fun getSourceSection(): SourceSection {
        val src: Source = encapsulatingSourceSection.source
        return srcSpan?.run {
            src.createSection(this)
        } ?: src.createUnavailableSection()
    }
}
