package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage


@ExportLibrary(InteropLibrary::class)
@CompilerDirectives.ValueType
data class StringVal(@JvmField val value: String) : SylviaVal() {
    @ExportMessage
    fun isString(): Boolean = true

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    fun asString(): String = value
}