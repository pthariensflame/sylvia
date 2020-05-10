package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.CompilerDirectives.ValueType
import com.oracle.truffle.api.TruffleException
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.source.SourceSection
import org.graalvm.tools.api.lsp.LSPLibrary
import org.jetbrains.annotations.Contract
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExportLibrary.Repeat(
    ExportLibrary(InteropLibrary::class),
//    ExportLibrary(LSPLibrary::class),
)
@ValueType
@OptIn(ExperimentalContracts::class)
data class SylviaExceptionVal(
    @JvmField val inner: SylviaException,
) : SylviaVal(), TruffleException by inner, Cloneable {
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

//    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    override fun getDocumentation(): Any =
        inner.getDocumentation()

//    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    override fun getSignature(): Any =
        inner.getSignature()

    @Contract("-> new", pure = true)
    override fun clone(): SylviaExceptionVal = SylviaExceptionVal(inner)
}
