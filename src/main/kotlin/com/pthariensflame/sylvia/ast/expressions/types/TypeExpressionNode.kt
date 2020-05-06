package com.pthariensflame.sylvia.ast.expressions.types

import com.oracle.truffle.api.frame.VirtualFrame
import com.pthariensflame.sylvia.ast.expressions.ExpressionNode
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.values.types.SylviaType

abstract class TypeExpressionNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null,
) : ExpressionNode(srcSpan) {
    abstract override fun executeVal(frame: VirtualFrame): SylviaType
}