package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.CompilerDirectives.ValueType
import com.oracle.truffle.api.TruffleException
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import com.oracle.truffle.api.nodes.Node
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExportLibrary(InteropLibrary::class)
@ValueType
@OptIn(ExperimentalContracts::class)
data class SylviaExceptionVal(
    @JvmField val inner: SylviaException,
) : SylviaVal(), TruffleException {
    override fun getLocation(): Node? = inner.location

    @ExportMessage
    fun isException(): Boolean {
        contract {
            returns(true)
        }
        return true
    }

    @ExportMessage
    fun throwException(): RuntimeException {
        contract {
            returns()
        }
        return inner
    }
}
