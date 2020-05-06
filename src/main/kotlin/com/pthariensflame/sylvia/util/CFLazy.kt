package com.pthariensflame.sylvia.util

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.CompilerDirectives.*
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.updateAndGet
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@ValueType
class CFLazy<out T : Any>(
    private val fn: () -> T,
) : ReadOnlyProperty<Any?, T> {
    @CompilationFinal
    private val underlying: AtomicRef<T?> = atomic(null)

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        underlying.updateAndGet { v ->
            if (CompilerDirectives.injectBranchProbability(SLOWPATH_PROBABILITY, null == v)) {
                CompilerDirectives.transferToInterpreter()
                fn()
            } else {
                v
            }
        }!!.assertPartialEvaluationConstant()
}