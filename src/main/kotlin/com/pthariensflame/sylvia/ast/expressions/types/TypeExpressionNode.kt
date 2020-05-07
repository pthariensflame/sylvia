package com.pthariensflame.sylvia.ast.expressions.types

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.GenerateWrapper
import com.oracle.truffle.api.instrumentation.InstrumentableNode
import com.oracle.truffle.api.instrumentation.ProbeNode
import com.oracle.truffle.api.instrumentation.Tag
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.ast.SylviaTag
import com.pthariensflame.sylvia.ast.expressions.ExpressionNode
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.values.types.SylviaType

@NodeInfo(
    shortName = "ty-expr",
    description = "A type expression"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
abstract class TypeExpressionNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null,
) : ExpressionNode(srcSpan) {
    companion object {
        private const val MSG: String = "Type expression can't be executed as non-type"
    }

    abstract override fun executeVal(frame: VirtualFrame): SylviaType

    final override fun executeBool(frame: VirtualFrame): Boolean =
        throw UnsupportedOperationException(MSG)

    final override fun executeByte(frame: VirtualFrame): Byte =
        throw UnsupportedOperationException(MSG)

    final override fun executeShort(frame: VirtualFrame): Short =
        throw UnsupportedOperationException(MSG)

    final override fun executeInt(frame: VirtualFrame): Int =
        throw UnsupportedOperationException(MSG)

    final override fun executeLong(frame: VirtualFrame): Long =
        throw UnsupportedOperationException(MSG)

    final override fun executeFloat(frame: VirtualFrame): Float =
        throw UnsupportedOperationException(MSG)

    final override fun executeDouble(frame: VirtualFrame): Double =
        throw UnsupportedOperationException(MSG)

    final override fun executeString(frame: VirtualFrame): String =
        throw UnsupportedOperationException(MSG)

    final override fun executeChar(frame: VirtualFrame): Char =
        throw UnsupportedOperationException(MSG)

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        TypeExpressionNodeWrapper(this, probe)

    override fun hasTag(tag: Class<out Tag>): Boolean =
        tag.kotlin == SylviaTag.TypeExpressionTag::class || super.hasTag(tag)
}