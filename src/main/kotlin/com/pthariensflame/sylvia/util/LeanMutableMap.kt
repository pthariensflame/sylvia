package com.pthariensflame.sylvia.util

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.CompilerDirectives.*
import org.graalvm.collections.EconomicMap
import org.graalvm.collections.MapCursor
import java.util.Spliterator
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Predicate

@ValueType
class LeanMutableMap<K : Any, V : Any>
@JvmOverloads constructor(
    override val underlying: EconomicMap<K, V> = EconomicMap.create(),
) : LeanMap<K, V>(underlying), MutableMap<K, V>, Cloneable {
    constructor(
        keyEquivStyle: EquivStyle<K>,
        initialCapacity: Int,
    ) : this(EconomicMap.create(keyEquivStyle.underlying, initialCapacity))

    constructor(
        keyEquivStyle: EquivStyle<K>,
    ) : this(EconomicMap.create(keyEquivStyle.underlying))

    constructor(
        initialCapacity: Int,
    ) : this(EconomicMap.create(initialCapacity))

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> by lazy {
        object : AbstractMutableSet<MutableMap.MutableEntry<K, V>>() {
            override val size: Int
                get() = this@LeanMutableMap.size

            override fun isEmpty(): Boolean =
                this@LeanMutableMap.isEmpty()

            override fun add(element: MutableMap.MutableEntry<K, V>): Boolean =
                underlying.put(element.key, element.value) == null

            override fun clear() = this@LeanMutableMap.clear()

            override fun contains(element: MutableMap.MutableEntry<K, V>): Boolean {
                val v: V? = underlying[element.key]
                return v != null && v == element.value
            }

            override fun remove(element: MutableMap.MutableEntry<K, V>): Boolean {
                val k = element.key
                val v: V? = underlying[k]
                if (v != null && v == element.value) {
                    this@LeanMutableMap.remove(k)
                    return true
                } else {
                    return false
                }
            }

            override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V>> {
                val cursor: MapCursor<K, V> = underlying.entries
                return object : MutableIterator<MutableMap.MutableEntry<K, V>> {
                    private var hasMore: Boolean = CompilerDirectives.injectBranchProbability(
                        LIKELY_PROBABILITY,
                        !this@LeanMutableMap.isEmpty()
                    )

                    private var present: Boolean = true;

                    override fun hasNext(): Boolean = !hasMore

                    override fun next(): MutableMap.MutableEntry<K, V> {
                        assert(hasMore) { "next called on empty iterator" }
                        hasMore = CompilerDirectives.injectBranchProbability(LIKELY_PROBABILITY, cursor.advance())
                        present = true
                        return object : MutableMap.MutableEntry<K, V> {
                            override val key: K
                                get() = cursor.key

                            override val value: V
                                get() = cursor.value

                            override fun setValue(newValue: V): V = underlying.put(key, newValue)
                        }
                    }

                    override fun remove() {
                        if (CompilerDirectives.injectBranchProbability(FASTPATH_PROBABILITY, present)) {
                            cursor.remove()
                            present = false
                        }
                    }
                }
            }
        }
    }

    override val keys: MutableSet<K> by lazy {
        object : AbstractMutableSet<K>() {
            private val myKeys: MutableIterable<K> by lazy {
                underlying.keys
            }

            override val size: Int
                get() = this@LeanMutableMap.size

            override fun isEmpty(): Boolean =
                this@LeanMutableMap.isEmpty()

            override fun add(element: K): Boolean =
                throw UnsupportedOperationException("mutable map view error: cannot insert a key without a value")

            override fun removeIf(filter: Predicate<in K>): Boolean = myKeys.assertPartialEvaluationConstant().removeAll(filter::test)

            override fun forEach(action: Consumer<in K>?) = myKeys.assertPartialEvaluationConstant().forEach(action)

            override fun clear() = this@LeanMutableMap.clear()

            override fun contains(element: K): Boolean = myKeys.assertPartialEvaluationConstant().contains(element)

            override fun iterator(): MutableIterator<K> = myKeys.assertPartialEvaluationConstant().iterator()

            override fun spliterator(): Spliterator<K> = myKeys.assertPartialEvaluationConstant().spliterator()
        }
    }

    override val values: MutableCollection<V> by lazy {
        object : AbstractMutableCollection<V>() {
            private val myValues: MutableIterable<V> by lazy {
                underlying.values
            }

            override val size: Int
                get() = this@LeanMutableMap.size

            override fun isEmpty(): Boolean =
                this@LeanMutableMap.isEmpty()

            override fun add(element: V): Boolean =
                throw UnsupportedOperationException("mutable map view error: cannot insert a value without a key")

            override fun removeIf(filter: Predicate<in V>): Boolean = myValues.assertPartialEvaluationConstant().removeAll(filter::test)

            override fun forEach(action: Consumer<in V>?) = myValues.assertPartialEvaluationConstant().forEach(action)

            override fun clear() = this@LeanMutableMap.clear()

            override fun contains(element: V): Boolean = myValues.assertPartialEvaluationConstant().contains(element)

            override fun iterator(): MutableIterator<V> = myValues.assertPartialEvaluationConstant().iterator()

            override fun spliterator(): Spliterator<V> = myValues.assertPartialEvaluationConstant().spliterator()
        }
    }

    override fun clear() = underlying.clear()

    override fun put(key: K, value: V): V? = underlying.put(key, value)

    override fun putAll(from: Map<out K, V>) = underlying.putAll(EconomicMap.wrapMap(from))

    override fun replaceAll(fn: BiFunction<in K, in V, out V>) =
        underlying.replaceAll(fn)

    override fun remove(key: K): V? = underlying.removeKey(key)

    override fun clone(): LeanMutableMap<K, V> =
        LeanMutableMap(this)

    override fun cloneWithStyle(keyEquivStyle: EquivStyle<K>): LeanMutableMap<K, V> =
        LeanMutableMap(keyEquivStyle, this)

    constructor(other: LeanMap<K, V>) : this(EconomicMap.create(other.underlying))

    constructor(keyEquivStyle: EquivStyle<K>, other: LeanMap<K, V>) : this(
        EconomicMap.create(
            keyEquivStyle.underlying,
            other.underlying
        )
    )
}