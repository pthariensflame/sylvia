package com.pthariensflame.sylvia.util

import org.graalvm.collections.UnmodifiableEconomicSet
import org.intellij.lang.annotations.Flow
import org.jetbrains.annotations.Contract
import java.util.*
import java.util.function.Consumer

interface LeanSet<out E : Any> : Set<E>, Cloneable {
    @JvmDefault
    @get:Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    @get:Contract(pure = true)
    val underlying: UnmodifiableEconomicSet<@UnsafeVariance E>

    @JvmDefault
    override val size: Int
        get() = underlying.size()

    @JvmDefault
    override fun isEmpty(): Boolean =
        underlying.isEmpty

    @JvmDefault
    @Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    override fun iterator(): Iterator<E> =
        underlying.iterator()

    @JvmDefault
    @Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    override fun spliterator(): Spliterator<@UnsafeVariance E> =
        underlying.spliterator()

    @JvmDefault
    @Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = "action",
        targetIsContainer = false
    )
    override fun forEach(action: Consumer<in E>) =
        underlying.forEach(action)

    @JvmDefault
    override fun contains(element: @UnsafeVariance E): Boolean =
        underlying.contains(element)

    @JvmDefault
    override fun containsAll(elements: Collection<@UnsafeVariance E>): Boolean =
        elements.all { underlying.contains(it) }

    @JvmDefault
    @Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = "arr",
        targetIsContainer = true
    )
    @Contract("_ -> param1", mutates = "param1")
    fun toArray(arr: Array<@UnsafeVariance E>): Array<@UnsafeVariance E> =
        underlying.toArray(arr)

    @JvmDefault
    @Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    @Contract("-> new")
    override fun clone(): LeanSet<E> =
        LeanSetImpl(this)

    @JvmDefault
    @Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = true
    )
    @Contract("-> new")
    fun cloneWithStyle(equivStyle: EquivStyle<E>): LeanSet<E> =
        LeanSetImpl(equivStyle, this)

    companion object {
        @Contract("_, _ -> new")
        inline fun <reified E : Any> create(
            equivStyle: EquivStyle<E> = EquivStyle.objectMethodsAll(),
            capacity: Int? = null,
        ): LeanSet<E> = capacity?.let { c ->
            LeanSetImpl(equivStyle, c)
        } ?: LeanSetImpl(equivStyle)

        @Contract("_, _ -> new")
        inline fun <reified E : Any> copyFrom(
            @Flow(
                source = Flow.DEFAULT_SOURCE,
                sourceIsContainer = true,
                target = Flow.RETURN_METHOD_TARGET,
                targetIsContainer = true
            )
            other: LeanSet<E>,
            equivStyle: EquivStyle<E> = EquivStyle.objectMethodsAll(),
        ): LeanSet<E> = LeanSetImpl(equivStyle, other)
    }
}
