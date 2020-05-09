package com.pthariensflame.sylvia.util

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.TruffleRuntime
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Range
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

@OptIn(ExperimentalContracts::class)
object TruffleUtil {
    @JvmStatic
    inline val runtime: TruffleRuntime
        get() = Truffle.getRuntime()

    @Suppress("NOTHING_TO_INLINE")
    @JvmStatic
    @Contract("_, _ -> param2", pure = true)
    inline fun injectBranchProbability(probability: Double, condition: Boolean): Boolean {
        contract {
            returns()
            returns(true) implies condition
            returns(false) implies !condition
        }
        return CompilerDirectives.injectBranchProbability(probability, condition)
    }
}
