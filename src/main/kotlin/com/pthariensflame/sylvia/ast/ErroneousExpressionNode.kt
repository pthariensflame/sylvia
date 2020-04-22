package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.GenerateWrapper
import com.oracle.truffle.api.instrumentation.InstrumentableNode
import com.oracle.truffle.api.instrumentation.ProbeNode
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
open class ErroneousExpressionNode internal constructor(): ExpressionNode() {
    companion object {
        @JvmStatic private val MSG: String = "Erroneous expression node; shouldn't be reached"
    }
    override fun isInstrumentable(): Boolean = false
    
    override fun executeVal(frame: VirtualFrame): SylviaVal {
        CompilerAsserts.neverPartOfCompilation(MSG)
        throw UnsupportedOperationException(MSG)
    }
}