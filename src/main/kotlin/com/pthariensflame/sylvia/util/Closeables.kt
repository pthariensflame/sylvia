package com.pthariensflame.sylvia.util

import org.jline.reader.History
import java.io.Closeable
import java.io.IOException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@Throws(IOException::class)
@OptIn(ExperimentalContracts::class)
inline fun <H : History, R> H.loadingAndSaving(block: (H) -> R) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    load()
    (Closeable { save() }).use {
        block(this)
    }
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
