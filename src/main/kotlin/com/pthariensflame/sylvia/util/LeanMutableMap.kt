package com.pthariensflame.sylvia.util

import org.graalvm.collections.EconomicMap
import org.graalvm.collections.UnmodifiableEconomicMap
import org.jetbrains.annotations.Contract
import java.util.function.BiFunction

interface LeanMutableMap<K : Any, V : Any> : LeanMap<K, V>, MutableMap<K, V> {
    @get:Contract(pure = true)
    @JvmDefault
    override val underlying: EconomicMap<K, V>

    @JvmDefault
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>

    @JvmDefault
    override val keys: MutableSet<K>

    @JvmDefault
    override val values: MutableCollection<V>

    @JvmDefault
    @Contract(mutates = "this")
    override fun clear() = underlying.clear()

    @JvmDefault
    @Contract(mutates = "this")
    override fun put(key: K, value: V): V? = underlying.put(key, value)

    @JvmDefault
    @Contract(mutates = "this")
    override fun putAll(from: Map<out K, V>) = underlying.putAll(EconomicMap.wrapMap(from))

    @JvmDefault
    @Contract(mutates = "this")
    override fun replaceAll(fn: BiFunction<in K, in V, out V>) =
        underlying.replaceAll(fn)

    @JvmDefault
    @Contract(mutates = "this")
    override fun remove(key: K): V? = underlying.removeKey(key)

    @JvmDefault
    @Contract("-> new")
    override fun clone(): LeanMutableMap<K, V> =
        LeanMutableMapImpl(this)

    @JvmDefault
    @Contract("_ -> new")
    override fun cloneWithStyle(keyEquivStyle: EquivStyle<K>): LeanMutableMap<K, V> =
        LeanMutableMapImpl(keyEquivStyle, this)

    companion object {
        @Contract("_, _ -> new")
        inline fun <reified K : Any, reified V : Any> create(
            keyEquivStyle: EquivStyle<K> = EquivStyle.objectMethodsAll(),
            initialCapacity: Int? = null
        ): LeanMutableMap<K, V> = initialCapacity?.let { c ->
            LeanMutableMapImpl(keyEquivStyle, c)
        } ?: LeanMutableMapImpl(keyEquivStyle)

        @Contract("_ -> new")
        inline fun <reified K : Any, reified V : Any> wrapping(
            inner: EconomicMap<K, V>,
        ): LeanMutableMap<K, V> = LeanMutableMapImpl(inner)

        @Contract("_ -> new")
        inline fun <reified K : Any, reified V : Any> wrapping(
            inner: MutableMap<K, V>,
        ): LeanMutableMap<K, V> = LeanMutableMapImpl(EconomicMap.wrapMap(inner))

        @Contract("_, _ -> new")
        inline fun <reified K : Any, reified V : Any> copyFrom(
            other: LeanMap<K, V>,
            keyEquivStyle: EquivStyle<K> = EquivStyle.objectMethodsAll(),
        ): LeanMutableMap<K, V> = LeanMutableMapImpl(keyEquivStyle, other)
    }
}