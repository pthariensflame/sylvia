package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.CompilerDirectives.*
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
import com.oracle.truffle.api.nodes.UnexpectedResultException
import com.pthariensflame.sylvia.ast.expressions.ExpressionNode
import com.pthariensflame.sylvia.ast.statements.ExpressionStatementNode
import com.pthariensflame.sylvia.ast.statements.StatementNode
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.util.TruffleUtil
import com.pthariensflame.sylvia.values.*

@NodeInfo(
    shortName = "sylv-prog-body",
    description = "A Sylvia program body"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
open class SylviaProgramBodyNode
@JvmOverloads constructor(
    @JvmField val srcSpan: SourceSpan? = null,
    @Node.Children @JvmField var statements: Array<StatementNode> = emptyArray(),
    @Node.Child @JvmField var endExpr: ExpressionNode? = null,
) : Node(), SylviaNode, InstrumentableNode {
    override fun isInstrumentable(): Boolean = true

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        SylviaProgramBodyNodeWrapper(this, probe)

    open fun executeVoid(outerFrame: VirtualFrame) {
        val allStatements =
            endExpr?.let {
                statements.plusElement(ExpressionStatementNode(it))
            } ?: statements
        val block: BlockNode<StatementNode> =
            BlockNode.create(allStatements) { frame: VirtualFrame, node: StatementNode, _: Int, _: Int ->
                node.executeVoid(frame)
            }
        block.executeVoid(outerFrame, BlockNode.NO_ARGUMENT)
    }

    open fun executeVal(outerFrame: VirtualFrame): SylviaVal? =
        endExpr?.let { expr ->
            val allStatements: MutableList<Node> = statements.copyOf().toMutableList()
            allStatements.add(expr)
            val block: BlockNode<Node> =
                BlockNode.create(allStatements.toTypedArray(), ProgramValueBlockExecutor)
            block.executeGeneric(outerFrame, BlockNode.NO_ARGUMENT) as SylviaVal
        } ?: run {
            executeVoid(outerFrame)
            null
        }

    @Throws(UnexpectedResultException::class)
    inline fun <reified T : Any> executeTyped(frame: VirtualFrame): T {
        val r = executeVal(frame)
        return if (TruffleUtil.injectBranchProbability(SLOWPATH_PROBABILITY, r is T)) {
            r as T
        } else {
            throw UnexpectedResultException(r)
        }
    }

    @Throws(UnexpectedResultException::class)
    open fun executeBool(frame: VirtualFrame): Boolean = executeTyped<BoolVal>(frame).value

    @Throws(UnexpectedResultException::class)
    open fun executeByte(frame: VirtualFrame): Byte = executeTyped<BigIntVal>(frame).apply {
        if (TruffleUtil.injectBranchProbability(SLOWPATH_PROBABILITY, !fitsInByte())) {
            throw UnexpectedResultException(this)
        }
    }.asByte()

    @Throws(UnexpectedResultException::class)
    open fun executeShort(frame: VirtualFrame): Short = executeTyped<BigIntVal>(frame).apply {
        if (TruffleUtil.injectBranchProbability(SLOWPATH_PROBABILITY, !fitsInShort())) {
            throw UnexpectedResultException(this)
        }
    }.asShort()

    @Throws(UnexpectedResultException::class)
    open fun executeInt(frame: VirtualFrame): Int = executeTyped<BigIntVal>(frame).apply {
        if (TruffleUtil.injectBranchProbability(SLOWPATH_PROBABILITY, !fitsInInt())) {
            throw UnexpectedResultException(this)
        }
    }.asInt()

    @Throws(UnexpectedResultException::class)
    open fun executeLong(frame: VirtualFrame): Long = executeTyped<BigIntVal>(frame).apply {
        if (TruffleUtil.injectBranchProbability(SLOWPATH_PROBABILITY, !fitsInLong())) {
            throw UnexpectedResultException(this)
        }
    }.asLong()

    @Throws(UnexpectedResultException::class)
    open fun executeFloat(frame: VirtualFrame): Float = executeTyped<BigFloatVal>(frame).apply {
        if (TruffleUtil.injectBranchProbability(SLOWPATH_PROBABILITY, !fitsInFloat())) {
            throw UnexpectedResultException(this)
        }
    }.asFloat()

    @Throws(UnexpectedResultException::class)
    open fun executeDouble(frame: VirtualFrame): Double = executeTyped<BigFloatVal>(frame).apply {
        if (TruffleUtil.injectBranchProbability(SLOWPATH_PROBABILITY, !fitsInDouble())) {
            throw UnexpectedResultException(this)
        }
    }.asDouble()

    @Throws(UnexpectedResultException::class)
    open fun executeString(frame: VirtualFrame): String = executeTyped<StringVal>(frame).value

    @Throws(UnexpectedResultException::class)
    open fun executeChar(frame: VirtualFrame): Char = executeString(frame).run {
        if (TruffleUtil.injectBranchProbability(CompilerDirectives.FASTPATH_PROBABILITY, length == 1)) {
            get(0)
        } else { // SLOWPATH_PROBABILITY
            throw UnexpectedResultException(this)
        }
    }

    object ProgramValueBlockExecutor : BlockNode.ElementExecutor<Node> {
        private const val MSG: String = "Can't execute Sylvia node at end of block: not expression node"

        private const val ALMOST_LIKELY_PROBABILITY: Double = LIKELY_PROBABILITY - SLOWPATH_PROBABILITY

        override fun executeVoid(frame: VirtualFrame, node: Node, index: Int, argument: Int) {
            if (TruffleUtil.injectBranchProbability(UNLIKELY_PROBABILITY, node is ExpressionNode)) {
                (node as ExpressionNode).executeVoid(frame)
            } else if (TruffleUtil.injectBranchProbability(ALMOST_LIKELY_PROBABILITY, node is StatementNode)
            ) {
                (node as StatementNode).executeVoid(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(
                    "Can't execute Sylvia node in block: neither expression node nor statement node"
                )
            }
        }

        override fun executeGeneric(frame: VirtualFrame, node: Node, index: Int, argument: Int): SylviaVal {
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                return (node as ExpressionNode).executeVal(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }
        }

        @Throws(UnexpectedResultException::class)
        override fun executeBoolean(frame: VirtualFrame, node: Node, index: Int, argument: Int): Boolean {
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                return (node as ExpressionNode).executeBool(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }
        }

        @Throws(UnexpectedResultException::class)
        override fun executeByte(frame: VirtualFrame, node: Node, index: Int, argument: Int): Byte {
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                return (node as ExpressionNode).executeByte(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }
        }

        @Throws(UnexpectedResultException::class)
        override fun executeDouble(frame: VirtualFrame, node: Node, index: Int, argument: Int): Double {
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                return (node as ExpressionNode).executeDouble(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }
        }

        @Throws(UnexpectedResultException::class)
        override fun executeFloat(frame: VirtualFrame, node: Node, index: Int, argument: Int): Float {
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                return (node as ExpressionNode).executeFloat(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }
        }

        @Throws(UnexpectedResultException::class)
        override fun executeInt(frame: VirtualFrame, node: Node, index: Int, argument: Int): Int {
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                return (node as ExpressionNode).executeInt(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }
        }

        @Throws(UnexpectedResultException::class)
        override fun executeLong(frame: VirtualFrame, node: Node, index: Int, argument: Int): Long {
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                return (node as ExpressionNode).executeLong(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }
        }

        @Throws(UnexpectedResultException::class)
        override fun executeShort(frame: VirtualFrame, node: Node, index: Int, argument: Int): Short {
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                return (node as ExpressionNode).executeShort(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }
        }

        @Throws(UnexpectedResultException::class)
        override fun executeChar(frame: VirtualFrame, node: Node, index: Int, argument: Int): Char {
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                return (node as ExpressionNode).executeString(frame).run {
                    if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, length == 1)) {
                        get(0)
                    } else { // SLOWPATH_PROBABILITY
                        throw UnexpectedResultException(this)
                    }
                }
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }
        }
    }
}
