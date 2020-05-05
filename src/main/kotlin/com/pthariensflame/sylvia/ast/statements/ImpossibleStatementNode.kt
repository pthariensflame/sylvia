package com.pthariensflame.sylvia.ast.statements

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.nodes.NodeCost
import com.oracle.truffle.api.nodes.NodeInfo

@NodeInfo(
    shortName = "⊥-stmt",
    description = "An erroneous statement, which cannot appear in valid fully-parsed code",
    cost = NodeCost.NONE,
)
@GenerateNodeFactory
@GenerateUncached(inherit = true)
@Introspectable
open class ImpossibleStatementNode internal constructor() : StatementNode() {
    companion object {
        private const val MSG: String =
            "Impossible statement node; shouldn't be reached"
    }

    override fun isInstrumentable(): Boolean = false

    override fun executeVoid(frame: VirtualFrame) {
        CompilerAsserts.neverPartOfCompilation(MSG)
        throw UnsupportedOperationException(MSG)
    }
}