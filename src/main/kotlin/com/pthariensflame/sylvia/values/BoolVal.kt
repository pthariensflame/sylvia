package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage


@ExportLibrary(InteropLibrary::class)
@CompilerDirectives.ValueType
data class BoolVal(@JvmField val value: Boolean) : SylviaVal() {
    @ExportMessage
    fun isBoolean(): Boolean = true

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    fun asBoolean(): Boolean = value
}