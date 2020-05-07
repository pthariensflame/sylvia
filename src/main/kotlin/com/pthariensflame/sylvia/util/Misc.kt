package com.pthariensflame.sylvia.util

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.TruffleRuntime
import org.jetbrains.annotations.Contract
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Suppress("NOTHING_TO_INLINE")
@Contract("-> this", pure = true)
inline fun <T> T.assertPartialEvaluationConstant(): T {
    CompilerAsserts.partialEvaluationConstant<T>(this)
    return this
}

@Suppress("NOTHING_TO_INLINE")
@Contract("-> this", pure = true)
inline fun <T> T.assertCompilationConstant(): T {
    CompilerAsserts.compilationConstant<T>(this)
    return this
}

object TruffleUtil {
    @JvmStatic
    inline val runtime: TruffleRuntime
        get() = Truffle.getRuntime()

    @Suppress("NOTHING_TO_INLINE")
    @OptIn(ExperimentalContracts::class)
    @JvmStatic
    @Contract("_, true -> true; _, false -> false", pure = true)
    inline fun injectBranchProbability(probability: Double, condition: Boolean): Boolean {
        contract {
            returns()
        }
        return CompilerDirectives.injectBranchProbability(probability, condition)
    }
}
