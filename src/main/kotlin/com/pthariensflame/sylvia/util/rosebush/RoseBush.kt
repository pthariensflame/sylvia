package com.pthariensflame.sylvia.util.rosebush

import com.oracle.truffle.api.CompilerDirectives.ValueType
import com.oracle.truffle.api.nodes.ExplodeLoop
import com.pthariensflame.sylvia.util.identityFunction
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Range
import kotlin.experimental.ExperimentalTypeInference

@ValueType
@OptIn(
    ExperimentalStdlibApi::class,
    ExperimentalTypeInference::class,
)
sealed class RoseBush<out Elem, out Gap> {
    companion object {
        @Contract(pure = true)
        @JvmStatic
        fun empty(): RoseBush<Nothing, Nothing> =
            Empty

        @Contract("_ -> new", pure = true)
        @JvmStatic
        fun <Elem> singleton(value: Elem): RoseBush<Elem, Nothing> =
            Leaf(value)

        @Contract("_ -> new", pure = true)
        @JvmStatic
        fun <Elem, Gap> nest(inner: RoseBush<Elem, Gap>): RoseBush<Elem, Gap> =
            Nested(listOf(inner), emptyList())

        @PublishedApi
        @Contract(pure = true)
        @JvmStatic
        internal fun <Elem, Gap> concatImpl(
            left: RoseBush<Elem, Gap>,
            gap: Gap,
            right: RoseBush<Elem, Gap>,
        ): RoseBush<Elem, Gap> =
            when {
                left is Empty -> {
                    right
                }
                right is Empty -> {
                    left
                }
                left is Leaf && right is Leaf -> Nested(
                    listOf(left, right),
                    listOf(gap),
                )
                left is Leaf && right is Nested -> Nested(
                    right._children.asReversed().plusElement(left).asReversed(),
                    right._childGaps.asReversed().plusElement(gap).asReversed(),
                )
                left is Nested && right is Leaf -> Nested(
                    left._children.plusElement(right),
                    left._childGaps.plusElement(gap),
                )
                else -> Nested(
                    (left as Nested)._children.plus((right as Nested)._children),
                    left._childGaps.plusElement(gap).plus(right._childGaps),
                )
            }

        @Contract(pure = true)
        @Suppress("NOTHING_TO_INLINE")
        @JvmStatic
        inline fun <Elem, Gap> concat(
            left: RoseBush<Elem, Gap>,
            gap: Gap,
            right: RoseBush<Elem, Gap>,
        ): RoseBush<Elem, Gap> = concatImpl(left, gap, right)

        @Contract(pure = true)
        @Suppress("NOTHING_TO_INLINE")
        @JvmStatic
        inline fun <Elem, Gap> concat(
            left: NonEmptyRoseBush<Elem, Gap>,
            gap: Gap,
            right: RoseBush<Elem, Gap>,
        ): NonEmptyRoseBush<Elem, Gap> = concatImpl(left, gap, right) as NonEmptyRoseBush

        @Contract(pure = true)
        @Suppress("NOTHING_TO_INLINE")
        @JvmStatic
        inline fun <Elem, Gap> concat(
            left: RoseBush<Elem, Gap>,
            gap: Gap,
            right: NonEmptyRoseBush<Elem, Gap>,
        ): NonEmptyRoseBush<Elem, Gap> = concatImpl(left, gap, right) as NonEmptyRoseBush

        @Contract(pure = true)
        @Suppress("NOTHING_TO_INLINE")
        @JvmStatic
        inline fun <Elem, Gap> concat(
            left: NonEmptyRoseBush<Elem, Gap>,
            gap: Gap,
            right: NonEmptyRoseBush<Elem, Gap>,
        ): NonEmptyRoseBush<Elem, Gap> = concatImpl(left, gap, right) as NonEmptyRoseBush

        @Contract(pure = true)
        @JvmStatic
        @Suppress("NOTHING_TO_INLINE")
        inline fun <Elem, Gap> Triple<RoseBush<Elem, Gap>, Gap, RoseBush<Elem, Gap>>.concat(): RoseBush<Elem, Gap> =
            concat(first, second, third)

        @Contract(pure = true)
        @JvmStatic
        inline operator fun <Elem, Gap> invoke(
            estimatedSize: @Range(from = 0, to = Int.MAX_VALUE.toLong()) Int = 0,
            @BuilderInference builder: BuilderScope<Elem, Gap>.() -> Unit,
        ): RoseBush<Elem, Gap> {
            val builderScope =
                BuilderScope<Elem, Gap>(
                    estimatedSize)
            builderScope.builder()
            return builderScope.finish().compacted()
        }

        @Contract(pure = true)
        @JvmStatic
        @Suppress("NOTHING_TO_INLINE")
        inline fun <Elem, Gap> RoseBush<Elem, Gap>?.orEmpty(): RoseBush<Elem, Gap> =
            this ?: empty()

        @Contract(pure = true)
        @JvmStatic
        @Suppress("NOTHING_TO_INLINE")
        inline fun <Elem, Gap> Triple<RoseBush<Elem, Gap>?, Gap, RoseBush<Elem, Gap>?>?.concatOrEmpty(): RoseBush<Elem, Gap> =
            this?.let {
                concat(first.orEmpty(),
                    second,
                    third.orEmpty())
            } ?: empty()
    }

    @Contract(pure = true)
    final override fun equals(other: Any?): Boolean = when {
        other !is RoseBush<*, *> -> false
        this is Empty && other is Empty ->
            true
        this is Leaf && other is Leaf<*> ->
            this._value == other._value
        this is Nested && other is Nested<*, *> ->
            this._children == other._children && this._childGaps == other._childGaps
        else -> false
    }

    @Contract(pure = true)
    abstract override fun hashCode(): Int

    final override fun toString(): String =
        StringBuilder(totalSize * 2).append("RoseBush: ").also {
            ToStringVisitor<Elem, Gap>(it).visit(this)
        }.trim().toString()

    private class ToStringVisitor<in Elem, in Gap>(
        @JvmField val sb: StringBuilder,
    ) : RoseBushVisitor<Elem, Gap> {
        override fun visitEmpty() {} // do nothing

        override fun visitLeaf(elem: Elem) {
            sb.append(elem)
        }

        override fun visitGap(gap: Gap) {
            sb.append(" ", gap, " ")
        }

        override fun enterNested() {
            sb.append("[")
        }

        override fun exitNested() {
            sb.append("]")
        }
    }

    @Contract(pure = true)
    abstract fun isEmpty(): Boolean

    @get:Contract(pure = true)
    abstract val totalNumberOfElements: Int

    @Contract(pure = true)
    abstract fun allElements(): Iterator<Elem>

    @get:Contract(pure = true)
    abstract val totalNumberOfGaps: Int

    @Contract(pure = true)
    abstract fun allGaps(): Iterator<Gap>

    abstract val totalNumberOfNestings: Int

    @get:Contract(pure = true)
    abstract val totalSize: Int

    @get:Contract(pure = true)
    abstract val depth: Int

    @Contract(pure = true)
    open fun flatten(): RoseBush<Elem, Gap> = foldLeft<RoseBush<Elem, Gap>>(
        handleEmpty = { Empty },
        handleLeaf = { Leaf(it) },
        accumNested = { l, g, r ->
            concat(l, g, r)
        },
    ).compacted()

    abstract fun <NewElem> mapElements(fn: (value: Elem) -> NewElem): RoseBush<NewElem, Gap>

    abstract fun <NewGap> mapGaps(fn: (gap: Gap) -> NewGap): RoseBush<Elem, NewGap>

    class BuilderScope<Elem, Gap>
    @PublishedApi internal constructor(
        initialCapacity: @Range(from = 0, to = Int.MAX_VALUE.toLong()) Int,
    ) {
        @PublishedApi
        internal enum class AdditionKind { El, Ga, Ne }

        @PublishedApi
        internal var expectingGap: Boolean = false

        @PublishedApi
        internal val accum: ArrayDeque<Pair<AdditionKind, Any?>> = ArrayDeque(initialCapacity)

        fun elem(e: Elem) {
            if (expectingGap)
                throw IllegalStateException("RoseBush.build: attempted to add element while expecting gap")
            accum.addLast(AdditionKind.El to e)
            expectingGap = true
        }

        fun gap(g: Gap) {
            if (!expectingGap)
                throw IllegalStateException("RoseBush.build: attempted to add gap while expecting element or subtree")
            accum.addLast(AdditionKind.Ga to g)
            expectingGap = false
        }

        inline fun subtree(
            estimatedSize: @Range(from = 0, to = Int.MAX_VALUE.toLong()) Int = 0,
            builder: BuilderScope<Elem, Gap>.() -> Unit,
        ) {
            if (expectingGap)
                throw IllegalStateException("RoseBush.build: attempted to add subtree while expecting gap")
            val builderScope =
                BuilderScope<Elem, Gap>(
                    estimatedSize)
            builderScope.builder()
            accum.addLast(AdditionKind.Ne to builderScope.finish())
            expectingGap = true
        }

        @PublishedApi
        internal fun finish(): RoseBush<Elem, Gap> {
            if (accum.isEmpty())
                return Empty
            if (!expectingGap)
                throw IllegalStateException("RoseBush.build: attempted to finish while expecting element or subtree")
            val (cgs, crs) =
                accum.partition { it.first == AdditionKind.Ga }
            val children = crs.map {
                if (it.first == AdditionKind.El) {
                    @Suppress("UNCHECKED_CAST")
                    it.second as RoseBush<Elem, Gap>
                } else { // it.first == AdditionKind.Ne
                    @Suppress("UNCHECKED_CAST")
                    (Leaf(it.second as Elem))
                }
            }
            val childGaps = cgs.map {
                @Suppress("UNCHECKED_CAST")
                it.second as Gap
            }
            return Nested(children, childGaps)
        }
    }

    @Contract(pure = true)
    abstract fun <InnerResult, FinalResult> foldLeft(
        handleEmpty: () -> FinalResult,
        handleLeaf: (value: Elem) -> FinalResult,
        beginNested: (nestedResult: FinalResult) -> InnerResult,
        accumNested: (
            previousAccumulatedResult: InnerResult,
            nextGap: Gap,
            nextNestedResult: FinalResult,
        ) -> InnerResult,
        finishNested: (accumulatedResult: InnerResult) -> FinalResult,
    ): FinalResult

    @Contract(pure = true)
    @Suppress("NOTHING_TO_INLINE")
    inline fun <Res> foldLeft(
        noinline handleEmpty: () -> Res,
        noinline handleLeaf: (value: Elem) -> Res,
        noinline accumNested: (
            previousAccumulatedResult: Res,
            nextGap: Gap,
            nextNestedResult: Res,
        ) -> Res,
    ): Res =
        foldLeft(
            handleEmpty,
            handleLeaf,
            beginNested = ::identityFunction,
            accumNested,
            finishNested = ::identityFunction,
        )

    @Contract(pure = true)
    abstract fun <InnerResult, FinalResult> foldRight(
        handleEmpty: () -> FinalResult,
        handleLeaf: (value: Elem) -> FinalResult,
        beginNested: (nestedResult: FinalResult) -> InnerResult,
        accumNested: (
            previousNestedResult: FinalResult,
            previousGap: Gap,
            nextAccumulatedResult: InnerResult,
        ) -> InnerResult,
        finishNested: (accumulatedResult: InnerResult) -> FinalResult,
    ): FinalResult

    @Contract(pure = true)
    @Suppress("NOTHING_TO_INLINE")
    inline fun <Res> foldRight(
        noinline handleEmpty: () -> Res,
        noinline handleLeaf: (value: Elem) -> Res,
        noinline accumNested: (
            previousNestedResult: Res,
            previousGap: Gap,
            nextAccumulatedResult: Res,
        ) -> Res,
    ): Res =
        foldRight(
            handleEmpty,
            handleLeaf,
            beginNested = ::identityFunction,
            accumNested,
            finishNested = ::identityFunction,
        )

    @Contract(pure = true)
    abstract fun <Val> acceptVisitor(
        visitor: RoseBushValueVisitor<Elem, Gap, Val>,
    ): Val

    abstract fun acceptVisitor(
        visitor: RoseBushVisitor<Elem, Gap>,
    )

    @Contract(pure = true)
    @Throws(AssertionError::class)
    abstract fun validateStructure()

    @PublishedApi
    @Contract(pure = true)
    internal abstract fun compacted(): RoseBush<Elem, Gap>
}

@ValueType
private object Empty : RoseBush<Nothing, Nothing>() {
    override fun hashCode(): Int = Empty::class.hashCode()

    override fun isEmpty(): Boolean = true

    override val totalNumberOfElements: Int = 0

    override fun allElements(): Iterator<Nothing> =
        emptySequence<Nothing>().iterator()

    override val totalNumberOfGaps: Int = 0

    override fun allGaps(): Iterator<Nothing> =
        emptySequence<Nothing>().iterator()

    override val totalNumberOfNestings: Int = 0

    override val totalSize: Int = 0

    override val depth: Int = 0

    override fun flatten(): RoseBush<Nothing, Nothing> =
        this

    override fun <NewElem> mapElements(fn: (value: Nothing) -> NewElem): RoseBush<Nothing, Nothing> =
        this

    override fun <NewGap> mapGaps(fn: (gap: Nothing) -> NewGap): RoseBush<Nothing, Nothing> =
        this

    override fun <InnerResult, FinalResult> foldLeft(
        handleEmpty: () -> FinalResult,
        handleLeaf: (value: Nothing) -> FinalResult,
        beginNested: (nestedResult: FinalResult) -> InnerResult,
        accumNested: (previousAccumulatedResult: InnerResult, nextGap: Nothing, nextNestedResult: FinalResult) -> InnerResult,
        finishNested: (accumulatedResult: InnerResult) -> FinalResult,
    ): FinalResult =
        handleEmpty()

    override fun <InnerResult, FinalResult> foldRight(
        handleEmpty: () -> FinalResult,
        handleLeaf: (value: Nothing) -> FinalResult,
        beginNested: (nestedResult: FinalResult) -> InnerResult,
        accumNested: (previousNestedResult: FinalResult, previousGap: Nothing, nextAccumulatedResult: InnerResult) -> InnerResult,
        finishNested: (accumulatedResult: InnerResult) -> FinalResult,
    ): FinalResult =
        handleEmpty()

    override fun <Val> acceptVisitor(
        visitor: RoseBushValueVisitor<Nothing, Nothing, Val>,
    ): Val =
        visitor.visitEmpty()

    override fun acceptVisitor(
        visitor: RoseBushVisitor<Nothing, Nothing>,
    ) {
        visitor.visitEmpty()
    }

    override fun validateStructure() {} // do nothing

    override fun compacted(): RoseBush<Nothing, Nothing> = this
}

@ValueType
sealed class NonEmptyRoseBush<out Elem, out Gap> : RoseBush<Elem, Gap>() {
    final override fun isEmpty(): Boolean = false
}

@ValueType
private data class Leaf<out Elem>(
    @JvmField val _value: Elem,
) : NonEmptyRoseBush<Elem, Nothing>() {

    override val totalNumberOfElements: Int = 1

    override fun allElements(): Iterator<Elem> =
        sequenceOf(_value).iterator()

    override val totalNumberOfGaps: Int = 0

    override fun allGaps(): Iterator<Nothing> =
        emptySequence<Nothing>().iterator()

    override val totalNumberOfNestings: Int = 0

    override val totalSize: Int = 1

    override val depth: Int = 0

    override fun flatten(): RoseBush<Elem, Nothing> =
        this

    override fun <NewElem> mapElements(fn: (value: Elem) -> NewElem): RoseBush<NewElem, Nothing> =
        Leaf(fn(_value))

    override fun <NewGap> mapGaps(fn: (gap: Nothing) -> NewGap): RoseBush<Elem, Nothing> =
        this

    override fun <InnerResult, FinalResult> foldLeft(
        handleEmpty: () -> FinalResult,
        handleLeaf: (value: Elem) -> FinalResult,
        beginNested: (nestedResult: FinalResult) -> InnerResult,
        accumNested: (
            previousAccumulatedResult: InnerResult,
            nextGap: Nothing,
            nextNestedResult: FinalResult,
        ) -> InnerResult,
        finishNested: (accumulatedResult: InnerResult) -> FinalResult,
    ): FinalResult =
        handleLeaf(_value)

    override fun <InnerResult, FinalResult> foldRight(
        handleEmpty: () -> FinalResult,
        handleLeaf: (value: Elem) -> FinalResult,
        beginNested: (nestedResult: FinalResult) -> InnerResult,
        accumNested: (
            previousNestedResult: FinalResult,
            previousGap: Nothing,
            nextAccumulatedResult: InnerResult,
        ) -> InnerResult,
        finishNested: (accumulatedResult: InnerResult) -> FinalResult,
    ): FinalResult =
        handleLeaf(_value)

    override fun <Val> acceptVisitor(
        visitor: RoseBushValueVisitor<Elem, Nothing, Val>,
    ): Val =
        visitor.visitLeaf(_value)

    override fun acceptVisitor(
        visitor: RoseBushVisitor<Elem, Nothing>,
    ) {
        visitor.visitLeaf(_value)
    }

    override fun validateStructure() {} // do nothing

    override fun compacted(): RoseBush<Elem, Nothing> = this
}

@ValueType
private data class Nested<out Elem, out Gap>(
    @JvmField val _children: List<RoseBush<Elem, Gap>>,
    @JvmField val _childGaps: List<Gap>,
) : NonEmptyRoseBush<Elem, Gap>() {

    override val totalNumberOfElements: Int =
        _children.sumBy { it.totalNumberOfElements }

    override fun allElements(): Iterator<Elem> =
        _children.asSequence().flatMap {
            it.allElements().asSequence()
        }.iterator()

    override val totalNumberOfGaps: Int =
        _children.sumBy { it.totalNumberOfGaps } + _childGaps.size

    override fun allGaps(): Iterator<Gap> =
        (_children.asSequence()
            .zip(_childGaps.asSequence())
            .flatMap { (c, g) ->
                c.allGaps().asSequence().plusElement(g)
            } + _children.last().allGaps().asSequence())
            .iterator()

    override val totalNumberOfNestings: Int =
        _children.sumBy { it.totalNumberOfNestings } + 1

    override val totalSize: Int =
        totalNumberOfElements + totalNumberOfGaps

    override val depth: Int =
        _children.asSequence().map { it.depth }.max()!! + 1

    override fun <NewElem> mapElements(fn: (value: Elem) -> NewElem): RoseBush<NewElem, Gap> =
        Nested(
            _children.map { it.mapElements(fn) },
            _childGaps,
        )

    override fun <NewGap> mapGaps(fn: (gap: Gap) -> NewGap): RoseBush<Elem, NewGap> =
        Nested(
            _children.map { it.mapGaps(fn) },
            _childGaps.map(fn),
        )

    @ExplodeLoop
    override fun <InnerResult, FinalResult> foldLeft(
        handleEmpty: () -> FinalResult,
        handleLeaf: (value: Elem) -> FinalResult,
        beginNested: (nestedResult: FinalResult) -> InnerResult,
        accumNested: (
            previousAccumulatedResult: InnerResult,
            nextGap: Gap,
            nextNestedResult: FinalResult,
        ) -> InnerResult,
        finishNested: (accumulatedResult: InnerResult) -> FinalResult,
    ): FinalResult {
        fun RoseBush<Elem, Gap>.recurse(): FinalResult = this@recurse.foldLeft(
            handleEmpty, handleLeaf, beginNested, accumNested, finishNested
        )

        var acc = beginNested(_children.first().recurse())
        _childGaps.asSequence().zip(
            _children.listIterator(1).asSequence()
        ).forEach { (g, c) ->
            acc = accumNested(acc, g, c.recurse())
        }
        return finishNested(acc)
    }

    @ExplodeLoop
    override fun <InnerResult, FinalResult> foldRight(
        handleEmpty: () -> FinalResult,
        handleLeaf: (value: Elem) -> FinalResult,
        beginNested: (nestedResult: FinalResult) -> InnerResult,
        accumNested: (
            previousNestedResult: FinalResult,
            previousGap: Gap,
            nextAccumulatedResult: InnerResult,
        ) -> InnerResult,
        finishNested: (accumulatedResult: InnerResult) -> FinalResult,
    ): FinalResult {
        fun RoseBush<Elem, Gap>.recurse(): FinalResult = this@recurse.foldRight(
            handleEmpty, handleLeaf, beginNested, accumNested, finishNested
        )

        var acc = beginNested(_children.last().recurse())
        _childGaps.asReversed().asSequence().zip(
            _children.asReversed().listIterator(1).asSequence()
        ).forEach { (g, c) ->
            acc = accumNested(c.recurse(), g, acc)
        }
        return finishNested(acc)
    }

    override fun <Val> acceptVisitor(
        visitor: RoseBushValueVisitor<Elem, Gap, Val>,
    ): Val = visitor.concatValues(sequence {
        yield(visitor.enterNested())
        yieldAll(_children.asSequence().zip(_childGaps.asSequence()) { c, g ->
            sequenceOf(
                visitor.visit(c),
                visitor.visitGap(g),
            )
        }.flatten())
        yield(visitor.visit(_children.last()))
        yield(visitor.exitNested())
    })

    override fun acceptVisitor(
        visitor: RoseBushVisitor<Elem, Gap>,
    ) {
        visitor.enterNested()
        _children.asSequence().zip(_childGaps.asSequence()).forEach { (c, g) ->
            visitor.visit(c)
            visitor.visitGap(g)
        }
        visitor.visit(_children.last())
        visitor.exitNested()
    }

    @ExplodeLoop
    override fun validateStructure() {
        if (_children.size != _childGaps.size + 1)
            throw AssertionError("RoseBush: structural validation failed")
        _children.forEach { it.validateStructure() }
    }

    @ExplodeLoop
    override fun compacted(): RoseBush<Elem, Gap> = copy(
        _children = _children.map {
            it.compacted()
        },
        _childGaps = _childGaps.toList(),
    )
}
