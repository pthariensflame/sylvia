package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.*
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.nodes.UnexpectedResultException
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.values.SylviaVal

@NodeInfo(
        shortName = "expr",
        description = "An expression"
         )
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached
abstract class ExpressionNode
@JvmOverloads constructor(
        @JvmField val srcSpan: SourceSpan? = null,
                         ) : Node(), SylviaNode, InstrumentableNode {
    abstract override fun isInstrumentable(): Boolean

    abstract fun executeVal(frame: VirtualFrame): SylviaVal

    @Throws(UnexpectedResultException::class)
    inline fun <reified T : Any> executeTyped(frame: VirtualFrame): T {
        val r = executeVal(frame)
        if (r is T) {
            return r
        } else {
            throw UnexpectedResultException(r)
        }
    }

    @Throws(UnexpectedResultException::class)
    open fun executeByte(frame: VirtualFrame): Byte = executeTyped(frame)

    @Throws(UnexpectedResultException::class)
    open fun executeShort(frame: VirtualFrame): Short = executeTyped(frame)

    @Throws(UnexpectedResultException::class)
    open fun executeInt(frame: VirtualFrame): Int = executeTyped(frame)

    @Throws(UnexpectedResultException::class)
    open fun executeLong(frame: VirtualFrame): Long = executeTyped(frame)

    @Throws(UnexpectedResultException::class)
    open fun executeFloat(frame: VirtualFrame): Float = executeTyped(frame)

    @Throws(UnexpectedResultException::class)
    open fun executeDouble(frame: VirtualFrame): Double = executeTyped(frame)

    @Throws(UnexpectedResultException::class)
    open fun executeString(frame: VirtualFrame): String = executeTyped(frame)

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
            ExpressionNodeWrapper(this, probe)

    override fun hasTag(tag: Class<out Tag>): Boolean =
            tag.kotlin == StandardTags.ExpressionTag::class || super.hasTag(tag)
}