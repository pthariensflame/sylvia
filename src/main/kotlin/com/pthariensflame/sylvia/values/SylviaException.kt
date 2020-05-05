package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.TruffleException
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.TruffleObject
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import org.jetbrains.annotations.Contract

@ExportLibrary(InteropLibrary::class)
abstract class SylviaException internal constructor() : RuntimeException(), TruffleObject, TruffleException {

    @ExportMessage
    @Contract("-> true")
    open fun isException(): Boolean = true

    @ExportMessage
    @Contract("-> this")
    open fun throwException(): RuntimeException = this

    @Contract("-> this", pure = true)
    abstract override fun fillInStackTrace(): Throwable
}
