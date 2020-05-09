package com.pthariensflame.sylvia.util

import org.graalvm.collections.EconomicMap
import org.intellij.lang.annotations.Flow
import org.jetbrains.annotations.Contract
import java.util.function.BiFunction

interface LeanMutableMap<K : Any, V : Any> : LeanMap<K, V>, MutableMap<K, V> {
    @JvmDefault
    @get:Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    @get:Contract(pure = true)
    override val underlying: EconomicMap<K, V>

    @JvmDefault
    @get:Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>

    @JvmDefault
    @get:Flow(
        source = "this.keys",
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    override val keys: MutableSet<K>

    @JvmDefault
    @get:Flow(
        source = "this.values",
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    override val values: MutableCollection<V>

    @JvmDefault
    @Contract(mutates = "this")
    override fun clear() = underlying.clear()

    @JvmDefault
    @Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = "this.values",
        targetIsContainer = false
    )
    @Contract(mutates = "this")
    override fun put(
        @Flow(
            source = Flow.DEFAULT_SOURCE,
            sourceIsContainer = false,
            target = "this.keys",
            targetIsContainer = true
        )
        key: K,
        @Flow(
            source = Flow.DEFAULT_SOURCE,
            sourceIsContainer = false,
            target = "this.values",
            targetIsContainer = true
        )
        value: V
    ): V? = underlying.put(key, value)

    @JvmDefault
    @Contract(mutates = "this")
    override fun putAll(
        @Flow(
            source = Flow.DEFAULT_SOURCE,
            sourceIsContainer = true,
            target = Flow.THIS_TARGET,
            targetIsContainer = true
        )
        from: Map<out K, V>
    ) = underlying.putAll(EconomicMap.wrapMap(from))

    @JvmDefault
    @Contract(mutates = "this")
    override fun replaceAll(fn: BiFunction<in K, in V, out V>) =
        underlying.replaceAll(fn)

    @JvmDefault
    @Flow(
        source = "this.values",
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = false
    )
    @Contract(mutates = "this")
    override fun remove(key: K): V? = underlying.removeKey(key)

    @JvmDefault
    @Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    @Contract("-> new")
    override fun clone(): LeanMutableMap<K, V> =
        LeanMutableMapImpl(this)

    @JvmDefault
    @Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
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
            @Flow(
                source = Flow.DEFAULT_SOURCE,
                sourceIsContainer = true,
                target = Flow.RETURN_METHOD_TARGET,
                targetIsContainer = true
            )
            inner: EconomicMap<K, V>,
        ): LeanMutableMap<K, V> = LeanMutableMapImpl(inner)

        @Contract("_ -> new")
        inline fun <reified K : Any, reified V : Any> wrapping(
            @Flow(
                source = Flow.DEFAULT_SOURCE,
                sourceIsContainer = true,
                target = Flow.RETURN_METHOD_TARGET,
                targetIsContainer = true
            )
            inner: MutableMap<K, V>,
        ): LeanMutableMap<K, V> = LeanMutableMapImpl(EconomicMap.wrapMap(inner))

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
        ): LeanMutableMap<K, V> = LeanMutableMapImpl(keyEquivStyle, other)
    }
}