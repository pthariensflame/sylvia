package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


@ExportLibrary(InteropLibrary::class)
@CompilerDirectives.ValueType
@OptIn(ExperimentalContracts::class)
data class StringVal(@JvmField val value: String) : SylviaVal() {
    @ExportMessage
    fun isString(): Boolean {
        contract {
            returns(true)
        }
        return true
    }

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    fun asString(): String = value
}