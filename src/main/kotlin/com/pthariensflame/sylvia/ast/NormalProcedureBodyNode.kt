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
    shortName = "norm-proc-body",
    description = "A normal procedure body"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
open class NormalProcedureBodyNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null,
    @Node.Children @JvmField var statements: Array<StatementNode> = emptyArray(),
) : ProcedureBodyNode(srcSpan) {
    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        NormalProcedureBodyNodeWrapper(this, probe)

    override fun executeVoid(outerFrame: VirtualFrame) {
        val block: BlockNode<StatementNode> =
            BlockNode.create(statements) { frame: VirtualFrame, node: StatementNode, _: Int, _: Int ->
                node.executeVoid(frame)
            }
        block.executeVoid(outerFrame, BlockNode.NO_ARGUMENT)
    }
}
