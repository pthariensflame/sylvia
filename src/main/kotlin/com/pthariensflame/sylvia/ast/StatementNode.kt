package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.GenerateWrapper
import com.oracle.truffle.api.instrumentation.InstrumentableNode
import com.oracle.truffle.api.instrumentation.ProbeNode
import com.oracle.truffle.api.nodes.ExecutableNode
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.SylviaLanguage

@NodeInfo(
    shortName = "stmt",
    description = "A statement"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached
abstract class StatementNode
@JvmOverloads constructor(
    langInstance: SylviaLanguage? = null,
) : ExecutableNode(langInstance), SylviaNode, InstrumentableNode {
    override fun isInstrumentable(): Boolean = true

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        StatementNodeWrapper(this, probe)

    abstract override fun execute(frame: VirtualFrame): Any?
}
