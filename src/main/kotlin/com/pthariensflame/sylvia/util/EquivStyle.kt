package com.pthariensflame.sylvia.util

import com.oracle.truffle.api.CompilerDirectives.ValueType
import org.graalvm.collections.Equivalence
import org.jetbrains.annotations.Contract

@ValueType
data class EquivStyle<in T : Any>(
    @JvmField val underlying: Equivalence,
) : Cloneable {
    fun equalityOf(a: T, b: T): Boolean =
        underlying.equals(a, b)

    fun hashCodeOf(o: T): Int =
        underlying.hashCode(o)

    override fun clone(): Any = copy()

    companion object {
        @Contract("-> new", pure = true)
        inline fun <reified T : Any> objectMethodsAll(): EquivStyle<T> =
            EquivStyle<T>(Equivalence.DEFAULT)

        @Contract("-> new", pure = true)
        inline fun <reified T : Any> systemEqualityObjectHashCode(): EquivStyle<T> =
            EquivStyle<T>(Equivalence.IDENTITY)

        @Contract("-> new", pure = true)
        inline fun <reified T : Any> systemOperatorsAll(): EquivStyle<T> =
            EquivStyle<T>(Equivalence.IDENTITY_WITH_SYSTEM_HASHCODE)

        @Contract("_ -> new", pure = true)
        inline fun <reified T : Any> fromUnderlying(e: Equivalence): EquivStyle<T> =
            EquivStyle<T>(e)

        @Contract("_, _ -> new", pure = true)
        inline fun <reified T : Any> of(
            crossinline eqFn: (T, T) -> Boolean,
            crossinline hashFn: (T) -> Int
        ): EquivStyle<T> =
            EquivStyle<T>(object : Equivalence() {
                override fun hashCode(o: Any): Int = hashFn(o as T)

                override fun equals(a: Any, b: Any): Boolean = eqFn(a as T, b as T)
            })
    }
}