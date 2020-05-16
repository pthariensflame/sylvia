package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.CompilerDirectives.LIKELY_PROBABILITY
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.dsl.TypeSystemReference
import com.oracle.truffle.api.frame.FrameDescriptor
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.GenerateWrapper
import com.oracle.truffle.api.instrumentation.InstrumentableNode
import com.oracle.truffle.api.instrumentation.ProbeNode
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.source.Source
import com.pthariensflame.sylvia.SylviaLanguage
import com.pthariensflame.sylvia.SylviaTruffleTypeSystem
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.util.TruffleUtil
import com.pthariensflame.sylvia.util.runAtomic
import org.jetbrains.annotations.Contract

@NodeInfo(
    shortName = "proc",
    description = "A procedure",
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
@TypeSystemReference(SylviaTruffleTypeSystem::class)
open class ProcedureNode
@JvmOverloads constructor(
    langInstance: SylviaLanguage? = null,
    frameDescriptor: FrameDescriptor? = null,
    originalSrc: Source? = null,
    srcSpan: SourceSpan? = null,
) : SylviaTopNode(langInstance, frameDescriptor, originalSrc, srcSpan) {
    @field:Node.Child
    private var _bodyNode: ProcedureBodyNode? = null

    var bodyNode: ProcedureBodyNode
        get() = _bodyNode!!
        set(newInner) = runAtomic {
            if (TruffleUtil.injectBranchProbability(LIKELY_PROBABILITY, _bodyNode == null)) {
                _bodyNode = insert(newInner)
                notifyInserted(newInner)
            } else {
                _bodyNode!!.replace(newInner)
            }
        }

    @Contract("-> new")
    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        ProcedureNodeWrapper(this, probe)

    override fun execute(frame: VirtualFrame): Any? {
        bodyNode.executeVoid(frame)
        return null
    }

    @GenerateWrapper.OutgoingConverter
    protected fun outConv(@Suppress("UNUSED_PARAMETER") v: Any?): Any? = null
}
