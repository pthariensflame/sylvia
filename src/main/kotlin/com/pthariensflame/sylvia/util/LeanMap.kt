package com.pthariensflame.sylvia.util

import org.graalvm.collections.EconomicMap
import org.graalvm.collections.UnmodifiableEconomicMap
import org.jetbrains.annotations.Contract

interface LeanMap<K : Any, out V : Any> : Map<K, V>, Cloneable {
    @JvmDefault
    @get:Contract(pure = true)
    val underlying: UnmodifiableEconomicMap<K, @UnsafeVariance V>

    @JvmDefault
    override val size: Int
        get() = underlying.size()

    @JvmDefault
    override val entries: Set<Map.Entry<K, V>>

    @JvmDefault
    override val keys: Set<K>

    @JvmDefault
    override val values: Collection<V>

    @JvmDefault
    override fun containsKey(key: K): Boolean =
        underlying.containsKey(key)

    @JvmDefault
    override fun containsValue(value: @UnsafeVariance V): Boolean =
        underlying.values.contains(value)

    @JvmDefault
    override operator fun get(key: K): V? =
        underlying[key]

    @JvmDefault
    override fun isEmpty(): Boolean =
        underlying.isEmpty

    @JvmDefault
    fun getOrDefault(key: K, defaultValue: @UnsafeVariance V): V =
        underlying.get(key, defaultValue)

    @JvmDefault
    @Contract("-> new")
    public override fun clone(): LeanMap<K, V> =
        LeanMapImpl(this)

    @JvmDefault
    @Contract("-> new")
    fun cloneWithStyle(keyEquivStyle: EquivStyle<K>): LeanMap<K, V> =
        LeanMapImpl(keyEquivStyle, this)

    companion object {
        @Contract("_, _ -> new")
        inline fun <reified K : Any, reified V : Any> create(
            keyEquivStyle: EquivStyle<K> = EquivStyle.objectMethodsAll(),
            capacity: Int? = null,
        ): LeanMap<K, V> = capacity?.let { c ->
            LeanMapImpl(keyEquivStyle, c)
        } ?: LeanMapImpl(keyEquivStyle)

        @Contract("_ -> new")
        inline fun <reified K : Any, reified V : Any> wrapping(
            inner: UnmodifiableEconomicMap<K, V>,
        ): LeanMap<K, V> = LeanMapImpl(inner)

        @Contract("_ -> new")
        inline fun <reified K : Any, reified V : Any> wrapping(
            inner: Map<K, V>,
        ): LeanMap<K, V> = LeanMapImpl(EconomicMap.wrapMap(inner))

        @Contract("_, _ -> new")
        inline fun <reified K : Any, reified V : Any> copyFrom(
            other: LeanMap<K, V>,
            keyEquivStyle: EquivStyle<K> = EquivStyle.objectMethodsAll(),
        ): LeanMap<K, V> = LeanMapImpl(keyEquivStyle, other)
    }
}
