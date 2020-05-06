package com.pthariensflame.sylvia.util

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.CompilerDirectives.LIKELY_PROBABILITY
import org.graalvm.collections.EconomicMap
import org.graalvm.collections.UnmodifiableEconomicMap
import org.graalvm.collections.UnmodifiableMapCursor
import java.util.Spliterator
import java.util.function.Consumer

@PublishedApi
internal inline class LeanMapImpl<K : Any, out V : Any>
constructor(
    override val underlying: UnmodifiableEconomicMap<K, @UnsafeVariance V> = EconomicMap.create(),
) : LeanMap<K, V> {
    constructor(
        keyEquivStyle: EquivStyle<K>,
        capacity: Int,
    ) : this(EconomicMap.create(keyEquivStyle.underlying, capacity))

    constructor(
        keyEquivStyle: EquivStyle<K>,
    ) : this(EconomicMap.create(keyEquivStyle.underlying))

    constructor(
        capacity: Int,
    ) : this(EconomicMap.create(capacity))

    override val entries: Set<Map.Entry<K, V>>
        get() = object : AbstractSet<Map.Entry<K, V>>() {
            override val size: Int
                get() = this@LeanMapImpl.size

            override fun isEmpty(): Boolean =
                this@LeanMapImpl.isEmpty()

            override fun iterator(): Iterator<Map.Entry<K, V>> {
                val cursor: UnmodifiableMapCursor<K, V> = underlying.entries
                return object : Iterator<Map.Entry<K, V>> {
                    private var hasMore: Boolean = CompilerDirectives.injectBranchProbability(
                        LIKELY_PROBABILITY,
                        !this@LeanMapImpl.isEmpty()
                    )

                    override fun hasNext(): Boolean = !hasMore

                    override fun next(): Map.Entry<K, V> {
                        assert(hasMore) { "next called on empty iterator" }
                        hasMore = CompilerDirectives.injectBranchProbability(CompilerDirectives.LIKELY_PROBABILITY,
                            cursor.advance())
                        return object : Map.Entry<K, V> {
                            override val key: K
                                get() = cursor.key

                            override val value: V
                                get() = cursor.value
                        }
                    }
                }
            }

        }

    override val keys: Set<K>
        get() = object : AbstractSet<K>() {
            private val myKeys: Iterable<K> by lazy {
                underlying.keys
            }

            override val size: Int
                get() = this@LeanMapImpl.size

            override fun isEmpty(): Boolean =
                this@LeanMapImpl.isEmpty()

            override fun contains(element: K): Boolean = myKeys.assertPartialEvaluationConstant().contains(element)

            override fun forEach(action: Consumer<in K>?) = myKeys.assertPartialEvaluationConstant().forEach(action)

            override fun iterator(): Iterator<K> = myKeys.assertPartialEvaluationConstant().iterator()

            override fun spliterator(): Spliterator<K> = myKeys.assertPartialEvaluationConstant().spliterator()
        }

    override val values: Collection<V>
        get() = object : AbstractCollection<V>() {
            private val myValues: Iterable<V> by lazy {
                underlying.values
            }

            override val size: Int
                get() = this@LeanMapImpl.size

            override fun isEmpty(): Boolean =
                this@LeanMapImpl.isEmpty()

            override fun contains(element: @UnsafeVariance V): Boolean =
                myValues.assertPartialEvaluationConstant().contains(element)

            override fun forEach(action: Consumer<in V>?) =
                myValues.assertPartialEvaluationConstant().forEach(action)

            override fun iterator(): Iterator<V> = myValues.assertPartialEvaluationConstant().iterator()

            override fun spliterator(): Spliterator<@UnsafeVariance V> =
                myValues.assertPartialEvaluationConstant().spliterator()
        }


    constructor(other: LeanMap<K, V>) :
            this(EconomicMap.create(other.underlying))

    constructor(keyEquivStyle: EquivStyle<K>, other: LeanMap<K, V>) :
            this(EconomicMap.create(keyEquivStyle.underlying, other.underlying))
}