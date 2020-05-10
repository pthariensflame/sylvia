package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.TruffleObject
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import org.graalvm.tools.api.lsp.LSPLibrary

@ExportLibrary.Repeat(
    ExportLibrary(InteropLibrary::class),
//    ExportLibrary(LSPLibrary::class),
)
abstract class SylviaVal internal constructor() : TruffleObject {
//    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    open fun getDocumentation(): Any =
        LSPLibrary.getFactory().getUncached(this).getDocumentation(this)

//    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    open fun getSignature(): Any =
        LSPLibrary.getFactory().getUncached(this).getSignature(this)
}
