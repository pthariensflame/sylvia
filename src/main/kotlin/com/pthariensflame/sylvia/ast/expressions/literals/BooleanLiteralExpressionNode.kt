package com.pthariensflame.sylvia.ast.expressions.literals

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.GenerateWrapper
import com.oracle.truffle.api.instrumentation.InstrumentableNode
import com.oracle.truffle.api.instrumentation.ProbeNode
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.values.BoolVal

@NodeInfo(
    shortName = "bool-lit",
    description = "A boolean literal expression"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
abstract class BooleanLiteralExpressionNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null,
    @field:CompilationFinal val value: Boolean = false,
) : LiteralExpressionNode(srcSpan) {
    override fun executeVal(frame: VirtualFrame): BoolVal =
        BoolVal(executeBoolean(frame)).apply {
            originatingNode = this@BooleanLiteralExpressionNode
        }

    override fun executeBoolean(frame: VirtualFrame): Boolean =
        value

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        BooleanLiteralExpressionNodeWrapper(this, probe)
}