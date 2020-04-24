package com.pthariensflame.sylvia.ast.expressions

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.nodes.NodeCost
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.values.SylviaVal

@NodeInfo(
    shortName = "⊥-expr",
    description = "An erroneous expression, which cannot appear in valid fully-parsed code",
    cost = NodeCost.NONE,
)
@GenerateNodeFactory
@GenerateUncached
@Introspectable
open class ImpossibleExpressionNode internal constructor() : ExpressionNode() {
    companion object {
        @JvmStatic
        private val MSG: String = "Impossible expression node; shouldn't be reached"
    }

    override fun isInstrumentable(): Boolean = false

    override fun executeVal(frame: VirtualFrame): SylviaVal {
        CompilerAsserts.neverPartOfCompilation(MSG)
        throw UnsupportedOperationException(MSG)
    }
}