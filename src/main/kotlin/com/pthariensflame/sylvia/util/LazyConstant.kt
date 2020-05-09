package com.pthariensflame.sylvia.util

import com.oracle.truffle.api.CompilerDirectives.SLOWPATH_PROBABILITY
import com.oracle.truffle.api.CompilerDirectives.ValueType
import com.oracle.truffle.api.utilities.AssumedValue
import org.intellij.lang.annotations.Flow
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@ValueType
class LazyConstant<out T : Any>(
    private val fn: () -> T,
) : ReadOnlyProperty<Any?, T> {
    private val underlying: AssumedValue<T?> =
        AssumedValue("underlying value of lazy constant", null)

    @Flow(
        source = Flow.THIS_SOURCE,
        sourceIsContainer = true,
        target = Flow.RETURN_METHOD_TARGET,
        targetIsContainer = false
    )
    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        force()
        return underlying.get()!!
    }

    fun force() {
        if (TruffleUtil.injectBranchProbability(SLOWPATH_PROBABILITY, null == underlying.get()))
            underlying.set(fn().let { underlying.get() ?: it })
    }
}