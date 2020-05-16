package com.pthariensflame.sylvia.ast.expressions

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.frame.VirtualFrame
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.values.BoolVal
import com.pthariensflame.sylvia.values.SylviaVal
import kotlin.properties.Delegates

class BooleanLiteralExpressionNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null,
    @JvmField val value: Boolean = false,
) : LiteralExpressionNode(srcSpan) {
    override fun executeVal(frame: VirtualFrame): BoolVal =
        BoolVal(executeBoolean(frame)).apply {
            originatingNode = this@BooleanLiteralExpressionNode
        }

    override fun executeBoolean(frame: VirtualFrame): Boolean =
        value
}