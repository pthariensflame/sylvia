package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.GenerateWrapper
import com.oracle.truffle.api.instrumentation.InstrumentableNode
import com.oracle.truffle.api.instrumentation.ProbeNode
import com.oracle.truffle.api.nodes.BlockNode
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.ast.statements.StatementNode
import com.pthariensflame.sylvia.parser.SourceSpan

@NodeInfo(
    shortName = "prim-proc-body",
    description = "A primitive procedure body"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
abstract class PrimitiveProcedureBodyNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null,
    @JvmField val primID: String = "",
) : ProcedureBodyNode(srcSpan) {
    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        PrimitiveProcedureBodyNodeWrapper(this, probe)
}
