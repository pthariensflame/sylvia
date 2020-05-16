package com.pthariensflame.sylvia.util

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.CompilerDirectives.*
import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.TruffleRuntime
import com.oracle.truffle.api.nodes.Node
import org.jetbrains.annotations.Contract
import java.io.Closeable
import java.util.concurrent.locks.Lock
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
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
inline fun <R> Lock.locking(fn: () -> R): R {
    contract {
        callsInPlace(fn, InvocationKind.EXACTLY_ONCE)
    }
    try {
        lock()
        return fn()
    } finally {
        unlock()
    }
}

@OptIn(ExperimentalContracts::class)
inline fun Lock.locking(fn: () -> Unit) {
    contract {
        callsInPlace(fn, InvocationKind.EXACTLY_ONCE)
    }
    try {
        lock()
        fn()
    } finally {
        unlock()
    }
}

@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
inline fun <R> Node.runAtomic(noinline fn: () -> R): R {
    contract {
        callsInPlace(fn, InvocationKind.EXACTLY_ONCE)
    }
    return atomic(fn)
}

@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
inline fun Node.runAtomic(noinline fn: () -> Unit) {
    contract {
        callsInPlace(fn, InvocationKind.EXACTLY_ONCE)
    }
    return atomic(fn)
}

class CloseableIterable<C : Closeable?>(
    private val inner: Iterable<C>,
) : Iterable<C> by inner, Closeable {
    override fun close(): Unit =
        inner.forEach { it?.close() }
}

@OptIn(ExperimentalContracts::class)
fun <C : Closeable?> Iterable<C>.asCloseable(): CloseableIterable<C> {
    contract {
        returns()
    }
    return CloseableIterable(this)
}

@OptIn(ExperimentalContracts::class)
inline fun <C : Closeable?, R> Iterable<C>.useAll(block: (Iterable<C>) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return asCloseable().use { block(it) }
}

class CloseableSequence<C : Closeable?>(
    private val inner: Sequence<C>,
) : Sequence<C> by inner, Closeable {
    override fun close(): Unit =
        inner.forEach { it?.close() }
}

@OptIn(ExperimentalContracts::class)
fun <C : Closeable?> Sequence<C>.asCloseable(): CloseableSequence<C> {
    contract {
        returns()
    }
    return CloseableSequence(this)
}

@OptIn(ExperimentalContracts::class)
inline fun <C : Closeable?, R> Sequence<C>.useAll(block: (Sequence<C>) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return asCloseable().use { block(it) }
}

@Contract("_ -> param1", pure = true)
@OptIn(ExperimentalContracts::class)
fun <T> identityFunction(v: T): T {
    contract {
        returns()
    }
    return v
}

@OptIn(ExperimentalContracts::class)
object TruffleUtil {
    const val ALMOST_LIKELY_PROBABILITY: Double = LIKELY_PROBABILITY - SLOWPATH_PROBABILITY

    const val ALMOST_UNLIKELY_PROBABILITY: Double = UNLIKELY_PROBABILITY - SLOWPATH_PROBABILITY

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

    @Suppress("NOTHING_TO_INLINE")
    @JvmStatic
    @Contract("_ -> true", pure = true)
    inline fun otherwise(probability: Double): Boolean {
        contract {
            returns(true)
        }
        return TruffleUtil.injectBranchProbability(probability, true)
    }
}
