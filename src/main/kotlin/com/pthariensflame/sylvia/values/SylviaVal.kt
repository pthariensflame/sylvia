package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.TruffleObject
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.source.SourceSection
import com.pthariensflame.sylvia.ast.expressions.ExpressionNode
import org.graalvm.tools.api.lsp.LSPLibrary
import org.jetbrains.annotations.Contract

@ExportLibrary.Repeat(
    ExportLibrary(InteropLibrary::class),
//    ExportLibrary(LSPLibrary::class),
)
abstract class SylviaVal internal constructor() : TruffleObject, Cloneable {
    @get:Contract(pure = true)
    abstract val originatingNode: ExpressionNode?

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
        null == originatingNode

    //    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    open fun getSourceLocation(): SourceSection =
        originatingNode?.sourceSection ?: throw UnsupportedMessageException.create()

    abstract override fun clone(): SylviaVal
}
