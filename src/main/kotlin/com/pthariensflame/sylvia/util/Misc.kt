package com.pthariensflame.sylvia.util

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.TruffleRuntime
import org.jetbrains.annotations.Contract

@Contract("-> this", pure = true)
inline fun <reified T> T.assertPartialEvaluationConstant(): T {
    CompilerAsserts.partialEvaluationConstant<T>(this)
    return this
}

@Contract("-> this", pure = true)
inline fun <reified T> T.assertCompilationConstant(): T {
    CompilerAsserts.compilationConstant<T>(this)
    return this
}

val truffleRuntime: TruffleRuntime
    inline get() = Truffle.getRuntime()
