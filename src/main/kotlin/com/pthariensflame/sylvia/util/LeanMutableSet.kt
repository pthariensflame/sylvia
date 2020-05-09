package com.pthariensflame.sylvia.util

import org.graalvm.collections.EconomicSet
import org.jetbrains.annotations.Contract

interface LeanMutableSet<E : Any> : LeanSet<E>, MutableSet<E> {
    @get:Contract(pure = true)
    @JvmDefault
    override val underlying: EconomicSet<E>

    @JvmDefault
    override fun iterator(): MutableIterator<E> =
        underlying.iterator()

    @JvmDefault
    @Contract(mutates = "this")
    override fun add(element: E): Boolean =
        underlying.add(element)

    @JvmDefault
    @Contract(mutates = "this")
    override fun addAll(elements: Collection<E>): Boolean {
        if (elements is LeanMutableSet<E>) return addAll(elements)
        val r = !containsAll(elements)
        underlying.addAll(elements.asIterable())
        return r
    }

    @JvmDefault
    @Contract(mutates = "this")
    fun addAll(elements: Iterable<E>): Boolean {
        val r = elements.any { !contains(it) }
        underlying.addAll(elements)
        return r
    }

    @JvmDefault
    @Contract(mutates = "this,param1")
    fun addAllOf(elements: Iterator<E>): Unit =
        underlying.addAll(elements)

    @JvmDefault
    @Contract(mutates = "this")
    fun addAll(elements: LeanMutableSet<E>): Boolean {
        val r = !containsAll(elements)
        underlying.addAll(elements.underlying)
        return r
    }

    @JvmDefault
    @Contract(mutates = "this")
    override fun clear() =
        underlying.clear()

    @JvmDefault
    @Contract(mutates = "this")
    override fun remove(element: E): Boolean {
        val r = contains(element)
        underlying.remove(element)
        return r
    }

    @JvmDefault
    @Contract(mutates = "this")
    override fun removeAll(elements: Collection<E>): Boolean {
        if (elements is LeanMutableSet<E>) return removeAll(elements)
        val r = containsAll(elements)
        underlying.removeAll(elements.asIterable())
        return r
    }

    @JvmDefault
    @Contract(mutates = "this")
    fun removeAll(elements: Iterable<E>): Boolean {
        val r = elements.any { !contains(it) }
        underlying.removeAll(elements)
        return r
    }

    @JvmDefault
    @Contract(mutates = "this")
    fun removeAll(elements: LeanMutableSet<E>): Boolean {
        val r = elements.any { !contains(it) }
        underlying.removeAll(elements)
        return r
    }

    @JvmDefault
    @Contract(mutates = "this,param1")
    fun removeAllOf(elements: Iterator<E>): Unit =
        underlying.removeAll(elements)

    @JvmDefault
    @Contract(mutates = "this")
    override fun retainAll(elements: Collection<E>): Boolean {
        if (elements is LeanMutableSet<E>) return retainAll(elements)
        val r = elements.containsAll(this)
        elements.forEach {
            if (!underlying.contains(it))
                underlying.remove(it)
        }
        return r
    }

    @JvmDefault
    @Contract(mutates = "this")
    fun retainAll(elements: LeanMutableSet<E>): Boolean {
        val r = elements.containsAll(this)
        underlying.retainAll(elements.underlying)
        return r
    }

    @JvmDefault
    @Contract("-> new")
    override fun clone(): LeanMutableSet<E> =
        LeanMutableSetImpl(this)

    @JvmDefault
    @Contract("-> new")
    override fun cloneWithStyle(equivStyle: EquivStyle<E>): LeanMutableSet<E> =
        LeanMutableSetImpl(equivStyle, this)

    companion object {
        @Contract("_, _ -> new")
        inline fun <reified E : Any> create(
            equivStyle: EquivStyle<E> = EquivStyle.objectMethodsAll(),
            capacity: Int? = null,
        ): LeanMutableSet<E> = capacity?.let { c ->
            LeanMutableSetImpl(equivStyle, c)
        } ?: LeanMutableSetImpl(equivStyle)

        @Contract("_, _ -> new")
        inline fun <reified E : Any> copyFrom(
            other: LeanSet<E>,
            equivStyle: EquivStyle<E> = EquivStyle.objectMethodsAll(),
        ): LeanMutableSet<E> = LeanMutableSetImpl(equivStyle, other)
    }
}
