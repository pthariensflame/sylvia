package com.pthariensflame.sylvia.util

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.CompilerDirectives.ValueType
import org.graalvm.collections.EconomicMap
import org.graalvm.collections.UnmodifiableEconomicMap
import org.graalvm.collections.UnmodifiableMapCursor
import java.util.Spliterator
import java.util.function.Consumer

@ValueType
open class LeanMap<K : Any, out V : Any>
@JvmOverloads constructor(
    open val underlying: UnmodifiableEconomicMap<K, @UnsafeVariance V> = EconomicMap.create(),
) : Map<K, V>, Cloneable {
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

    override val size: Int
        get() = underlying.size()

    override val entries: Set<Map.Entry<K, V>> by lazy {
        object : AbstractSet<Map.Entry<K, V>>() {
            override val size: Int
                get() = this@LeanMap.size

            override fun isEmpty(): Boolean =
                this@LeanMap.isEmpty()

            override fun iterator(): Iterator<Map.Entry<K, V>> {
                val cursor: UnmodifiableMapCursor<K, V> = underlying.entries
                return object : Iterator<Map.Entry<K, V>> {
                    private var hasMore: Boolean = CompilerDirectives.injectBranchProbability(
                        CompilerDirectives.LIKELY_PROBABILITY,
                        !this@LeanMap.isEmpty()
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
    }

    override val keys: Set<K> by lazy {
        object : AbstractSet<K>() {
            private val myKeys: Iterable<K> by lazy {
                underlying.keys
            }

            override val size: Int
                get() = this@LeanMap.size

            override fun isEmpty(): Boolean =
                this@LeanMap.isEmpty()

            override fun contains(element: K): Boolean = myKeys.assertPartialEvaluationConstant().contains(element)

            override fun forEach(action: Consumer<in K>?) = myKeys.assertPartialEvaluationConstant().forEach(action)

            override fun iterator(): Iterator<K> = myKeys.assertPartialEvaluationConstant().iterator()

            override fun spliterator(): Spliterator<K> = myKeys.assertPartialEvaluationConstant().spliterator()
        }
    }

    override val values: Collection<V> by lazy {
        object : AbstractCollection<V>() {
            private val myValues: Iterable<V> by lazy {
                underlying.values
            }

            override val size: Int
                get() = this@LeanMap.size

            override fun isEmpty(): Boolean =
                this@LeanMap.isEmpty()

            override fun contains(element: @UnsafeVariance V): Boolean =
                myValues.assertPartialEvaluationConstant().contains(element)

            override fun forEach(action: Consumer<in V>?) = myValues.assertPartialEvaluationConstant().forEach(action)

            override fun iterator(): Iterator<V> = myValues.assertPartialEvaluationConstant().iterator()

            override fun spliterator(): Spliterator<@UnsafeVariance V> =
                myValues.assertPartialEvaluationConstant().spliterator()
        }
    }


    override fun containsKey(key: K): Boolean =
        underlying.containsKey(key)

    override fun containsValue(value: @UnsafeVariance V): Boolean =
        underlying.values.contains(value)

    override operator fun get(key: K): V? =
        underlying[key]

    override fun isEmpty(): Boolean =
        underlying.isEmpty

    override fun getOrDefault(key: K, defaultValue: @UnsafeVariance V): V =
        underlying.get(key, defaultValue)

    override fun clone(): LeanMap<K, V> =
        LeanMap(this)

    open fun cloneWithStyle(keyEquivStyle: EquivStyle<K>): LeanMap<K, V> =
        LeanMap(keyEquivStyle, this)

    constructor(other: LeanMap<K, V>) :
            this(EconomicMap.create(other.underlying))

    constructor(keyEquivStyle: EquivStyle<K>, other: LeanMap<K, V>) :
            this(EconomicMap.create(keyEquivStyle.underlying, other.underlying))
}