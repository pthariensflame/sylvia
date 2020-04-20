package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage

@ExportLibrary(InteropLibrary::class)
object NoReturnVal : SylviaVal() {
    @ExportMessage(limit = "1")
    fun isNull(): Boolean = true

    override fun equals(other: Any?): Boolean = other is NoReturnVal || other == this
}