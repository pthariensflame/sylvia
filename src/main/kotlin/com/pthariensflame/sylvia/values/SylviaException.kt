package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.TruffleException
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.TruffleObject
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage

@ExportLibrary(InteropLibrary::class)
abstract class SylviaException internal constructor() : RuntimeException(), TruffleObject, TruffleException {

    @ExportMessage
    open fun isException(): Boolean = true

    @ExportMessage
    open fun throwException(): RuntimeException = this
}
