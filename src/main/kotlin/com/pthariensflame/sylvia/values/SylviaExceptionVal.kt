package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.CompilerDirectives.ValueType
import com.oracle.truffle.api.TruffleException
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import com.oracle.truffle.api.nodes.Node
import org.jetbrains.annotations.Contract
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExportLibrary(InteropLibrary::class)
@ValueType
@OptIn(ExperimentalContracts::class)
data class SylviaExceptionVal(
    @JvmField val inner: SylviaException,
) : SylviaVal(), TruffleException, Cloneable {
    override fun getLocation(): Node? = inner.location

    @ExportMessage
    @Contract("-> true", pure = true)
    fun isException(): Boolean {
        contract {
            returns(true)
        }
        return true
    }

    @ExportMessage
    @Contract(pure = true)
    fun throwException(): RuntimeException {
        contract {
            returns()
        }
        return inner
    }

    @Contract("-> new", pure = true)
    override fun clone(): SylviaExceptionVal = SylviaExceptionVal(inner)
}
