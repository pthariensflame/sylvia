package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.TruffleException
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.TruffleObject
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.source.Source
import com.oracle.truffle.api.source.SourceSection
import com.pthariensflame.sylvia.parser.SourceSpan
import org.graalvm.tools.api.lsp.LSPLibrary
import org.jetbrains.annotations.Contract

@ExportLibrary.Repeat(
    ExportLibrary(InteropLibrary::class),
//    ExportLibrary(LSPLibrary::class),
)
abstract class SylviaException internal constructor() : RuntimeException(), TruffleObject, TruffleException {
    @Contract("-> this", pure = true)
    abstract override fun getLocation(): Node?

    abstract override fun fillInStackTrace(): Throwable

    @ExportMessage
    @Contract("-> true")
    open fun isException(): Boolean = true

    @ExportMessage
    @Contract("-> this")
    open fun throwException(): RuntimeException = this

//    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    open fun getDocumentation(): Any =
        LSPLibrary.getFactory().getUncached(this).getDocumentation(this)

//    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    open fun getSignature(): Any =
        LSPLibrary.getFactory().getUncached(this).getSignature(this)

    //    @ExportMessage
    open fun hasSourceLocation(): Boolean =
        null == super<TruffleException>.getSourceLocation()

    //    @ExportMessage
    override fun getSourceLocation(): SourceSection? =
        super<TruffleException>.getSourceLocation() ?: location?.sourceSection?.source?.createUnavailableSection()
}
