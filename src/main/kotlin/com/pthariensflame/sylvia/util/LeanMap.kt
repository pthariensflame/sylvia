package com.pthariensflame.sylvia.util

import org.graalvm.collections.EconomicMap
import org.graalvm.collections.UnmodifiableEconomicMap
import org.intellij.lang.annotations.Flow
import org.jetbrains.annotations.Contract

interface LeanMap<K : Any, out V : Any> : Map<K, V>, Cloneable {
    @JvmDefault
    @get:Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    @get:Contract(pure = true)
    val underlying: UnmodifiableEconomicMap<K, @UnsafeVariance V>

    @JvmDefault
    override val size: Int
        get() = underlying.size()

    @JvmDefault
    @get:Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    override val entries: Set<Map.Entry<K, V>>

    @JvmDefault
    @get:Flow(
        source = "this.keys",
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    override val keys: Set<K>

    @JvmDefault
    @get:Flow(
        source = "this.values",
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    override val values: Collection<V>

    @JvmDefault
    override fun containsKey(key: K): Boolean =
        underlying.containsKey(key)

    @JvmDefault
    override fun containsValue(value: @UnsafeVariance V): Boolean =
        underlying.values.contains(value)

    @JvmDefault
    @Flow(
        source = "this.values",
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = false
    )
    override operator fun get(key: K): V? =
        underlying[key]

    @JvmDefault
    override fun isEmpty(): Boolean =
        underlying.isEmpty

    @JvmDefault
    @Flow(
        source = "this.values",
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = false
    )
    fun getOrDefault(key: K, defaultValue: @UnsafeVariance V): V =
        underlying.get(key, defaultValue)

    @JvmDefault
    @Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    @Contract("-> new")
    public override fun clone(): LeanMap<K, V> =
        LeanMapImpl(this)

    @JvmDefault
    @Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
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
            @Flow(
                source = Flow.DEFAULT_SOURCE,
                sourceIsContainer = true,
                target = Flow.RETURN_METHOD_TARGET,
                targetIsContainer = true
            )
            inner: UnmodifiableEconomicMap<K, V>,
        ): LeanMap<K, V> = LeanMapImpl(inner)

        @Contract("_ -> new")
        inline fun <reified K : Any, reified V : Any> wrapping(
            @Flow(
                source = Flow.DEFAULT_SOURCE,
                sourceIsContainer = true,
                target = Flow.RETURN_METHOD_TARGET,
                targetIsContainer = true
            )
            inner: Map<K, V>,
        ): LeanMap<K, V> = LeanMapImpl(EconomicMap.wrapMap(inner))

        @Contract("_, _ -> new")
        inline fun <reified K : Any, reified V : Any> copyFrom(
            @Flow(
                source = Flow.DEFAULT_SOURCE,
                sourceIsContainer = true,
                target = Flow.RETURN_METHOD_TARGET,
                targetIsContainer = true
            )
            other: LeanMap<K, V>,
            keyEquivStyle: EquivStyle<K> = EquivStyle.objectMethodsAll(),
        ): LeanMap<K, V> = LeanMapImpl(keyEquivStyle, other)
    }
}
