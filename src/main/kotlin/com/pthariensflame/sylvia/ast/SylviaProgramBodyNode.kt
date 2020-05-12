package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.CompilerDirectives.*
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.*
import com.oracle.truffle.api.nodes.BlockNode
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.nodes.UnexpectedResultException
import com.pthariensflame.sylvia.UnicodeCodepoint
import com.pthariensflame.sylvia.ast.expressions.ExpressionNode
import com.pthariensflame.sylvia.ast.statements.StatementNode
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.util.LazyConstant
import com.pthariensflame.sylvia.util.TruffleUtil
import com.pthariensflame.sylvia.util.TruffleUtil.ALMOST_LIKELY_PROBABILITY
import com.pthariensflame.sylvia.util.runAtomic
import com.pthariensflame.sylvia.values.StringVal
import com.pthariensflame.sylvia.values.SylviaVal
import org.intellij.lang.annotations.Flow
import org.jetbrains.annotations.Contract
import java.util.*

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
) : Node(), SylviaNode, InstrumentableNode {
    @get:Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    @field:Node.Children
    var statements: Array<StatementNode> = emptyArray()
        private set

    val block: BlockNode<Node> by LazyConstant {
        val allStatements: MutableList<Node> = statements.copyOf().toMutableList()
        endExpr?.let { allStatements.add(it) }
        BlockNode.create(allStatements.toTypedArray(), ProgramValueBlockExecutor)
    }

    @Contract(mutates = "this,param2")
    fun addStatement(
        @Flow(
            source = Flow.DEFAULT_SOURCE,
            sourceIsContainer = false,
            target = Flow.THIS_TARGET,
            targetIsContainer = true
        )
        newStatement: StatementNode,
    ): Unit = runAtomic {
        val oldSize = statements.size
        statements += insert(newStatement)
        notifyInserted(statements[oldSize])
    }


    @Contract(mutates = "this,param2")
    fun addStatements(
        @Flow(
            source = Flow.DEFAULT_SOURCE,
            sourceIsContainer = true,
            target = Flow.THIS_TARGET,
            targetIsContainer = true
        )
        newStatements: Iterable<StatementNode>,
    ): Unit = runAtomic {
        val oldSize = statements.size
        statements = Arrays.copyOf(statements, oldSize + newStatements.count())
        newStatements.forEachIndexed { index, newStatement ->
            val adjustedIndex = oldSize + index
            statements[adjustedIndex] = insert(newStatement)
            notifyInserted(statements[adjustedIndex])
        }
    }

    @Contract(pure = true)
    @Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = false
    )
    fun getStatement(index: Int): StatementNode =
        statements[index]

    @Contract(mutates = "this,param2")
    fun replaceStatement(
        index: Int,
        @Flow(
            source = Flow.DEFAULT_SOURCE,
            sourceIsContainer = false,
            target = Flow.THIS_TARGET,
            targetIsContainer = true
        )
        newStatement: StatementNode,
    ): StatementNode = runAtomic<StatementNode> {
        statements[index].replace(newStatement)
    }

    @field:Node.Child
    @get:Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = false
    )
    var endExpr: ExpressionNode? = null
        set(
            @Flow(
                source = Flow.DEFAULT_SOURCE,
                sourceIsContainer = false,
                target = Flow.THIS_TARGET,
                targetIsContainer = true
            )
            newExpr,
        ) = runAtomic {
            if (newExpr == null) {
                field = null
            } else if (field == null) {
                field = insert(newExpr)
                notifyInserted(field)
            } else {
                field!!.replace(newExpr)
            }
        }

    override fun isInstrumentable(): Boolean = true

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        SylviaProgramBodyNodeWrapper(this, probe)

    override fun hasTag(tag: Class<out Tag>): Boolean =
        tag.kotlin == StandardTags.RootBodyTag::class || super.hasTag(tag)

    open fun executeVoid(outerFrame: VirtualFrame): Unit =
        block.executeVoid(outerFrame, BlockNode.NO_ARGUMENT)

    open fun executeVal(outerFrame: VirtualFrame): SylviaVal? =
        block.executeGeneric(outerFrame, BlockNode.NO_ARGUMENT) as SylviaVal

    @Throws(UnexpectedResultException::class)
    inline fun <reified T : SylviaVal> executeTyped(frame: VirtualFrame): T {
        val r = executeVal(frame)
        return if (TruffleUtil.injectBranchProbability(SLOWPATH_PROBABILITY, r is T)) {
            r
        } else {
            throw UnexpectedResultException(r)
        }
    }

    @Throws(UnexpectedResultException::class)
    open fun executeBoolean(frame: VirtualFrame): Boolean =
        block.executeBoolean(frame, BlockNode.NO_ARGUMENT)

    @Throws(UnexpectedResultException::class)
    open fun executeByte(frame: VirtualFrame): Byte =
        block.executeByte(frame, BlockNode.NO_ARGUMENT)

    @Throws(UnexpectedResultException::class)
    open fun executeShort(frame: VirtualFrame): Short =
        block.executeShort(frame, BlockNode.NO_ARGUMENT)

    @Throws(UnexpectedResultException::class)
    open fun executeInt(frame: VirtualFrame): Int =
        block.executeInt(frame, BlockNode.NO_ARGUMENT)

    @Throws(UnexpectedResultException::class)
    open fun executeLong(frame: VirtualFrame): Long =
        block.executeLong(frame, BlockNode.NO_ARGUMENT)

    @Throws(UnexpectedResultException::class)
    open fun executeFloat(frame: VirtualFrame): Float =
        block.executeFloat(frame, BlockNode.NO_ARGUMENT)

    @Throws(UnexpectedResultException::class)
    open fun executeDouble(frame: VirtualFrame): Double =
        block.executeDouble(frame, BlockNode.NO_ARGUMENT)

    @Throws(UnexpectedResultException::class)
    open fun executeString(frame: VirtualFrame): String = executeTyped<StringVal>(frame).value

    @Throws(UnexpectedResultException::class)
    open fun executeChar(frame: VirtualFrame): Char =
        block.executeChar(frame, BlockNode.NO_ARGUMENT)

    @Throws(UnexpectedResultException::class)
    open fun executeUnicodeCodepoint(frame: VirtualFrame): UnicodeCodepoint =
        executeString(frame).run {
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, length == 1)) {
                UnicodeCodepoint(get(0))
            } else if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, length == 2)) {
                UnicodeCodepoint(get(0), get(1))
            } else { // SLOWPATH_PROBABILITY
                throw UnexpectedResultException(this)
            }
        }

    object ProgramValueBlockExecutor : BlockNode.ElementExecutor<Node> {
        private const val MSG: String = "Can't execute Sylvia node at end of block: not expression node"

        override fun executeVoid(frame: VirtualFrame, node: Node, index: Int, argument: Int): Unit = when {
            TruffleUtil.injectBranchProbability(
                UNLIKELY_PROBABILITY,
                node is ExpressionNode
            ) -> {
                node.executeVoid(frame)
            }
            TruffleUtil.injectBranchProbability(
                ALMOST_LIKELY_PROBABILITY,
                node is StatementNode
            ) -> {
                node.executeVoid(frame)
            }
            else -> { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(
                    "Can't execute Sylvia node in block: neither expression node nor statement node"
                )
            }
        }

        override fun executeGeneric(frame: VirtualFrame, node: Node, index: Int, argument: Int): SylviaVal =
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                node.executeVal(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }

        @Throws(UnexpectedResultException::class)
        override fun executeBoolean(frame: VirtualFrame, node: Node, index: Int, argument: Int): Boolean =
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                node.executeBoolean(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }

        @Throws(UnexpectedResultException::class)
        override fun executeByte(frame: VirtualFrame, node: Node, index: Int, argument: Int): Byte =
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                node.executeByte(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }

        @Throws(UnexpectedResultException::class)
        override fun executeDouble(frame: VirtualFrame, node: Node, index: Int, argument: Int): Double =
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                node.executeDouble(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }

        @Throws(UnexpectedResultException::class)
        override fun executeFloat(frame: VirtualFrame, node: Node, index: Int, argument: Int): Float =
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                node.executeFloat(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }

        @Throws(UnexpectedResultException::class)
        override fun executeInt(frame: VirtualFrame, node: Node, index: Int, argument: Int): Int =
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                node.executeInt(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }

        @Throws(UnexpectedResultException::class)
        override fun executeLong(frame: VirtualFrame, node: Node, index: Int, argument: Int): Long =
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                node.executeLong(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }

        @Throws(UnexpectedResultException::class)
        override fun executeShort(frame: VirtualFrame, node: Node, index: Int, argument: Int): Short =
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                node.executeShort(frame)
            } else { // SLOWPATH_PROBABILITY
                throw IllegalArgumentException(MSG)
            }

        @Throws(UnexpectedResultException::class)
        override fun executeChar(frame: VirtualFrame, node: Node, index: Int, argument: Int): Char =
            if (TruffleUtil.injectBranchProbability(FASTPATH_PROBABILITY, node is ExpressionNode)) {
                node.executeString(frame).run {
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
