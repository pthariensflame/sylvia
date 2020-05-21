package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.CompilerDirectives.ValueType
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.source.SourceSection
import com.pthariensflame.sylvia.ast.expressions.ExpressionNode
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
) : SylviaVal() {
    @get:Contract(pure = true)
    override val originatingNode: ExpressionNode?
        get() = location as? ExpressionNode

    @get:Contract(pure = true)
    val location: Node?
        get() = inner.location

    @ExportMessage
    @Contract("-> true", pure = true)
    fun isException(): Boolean {
        contract {
            returns(true)
        }
        return true
    }

    @ExportMessage
    override fun hasSourceLocation(): Boolean =
        null != inner.sourceLocation || super<SylviaVal>.hasSourceLocation()

    @ExportMessage
    override fun getSourceLocation(): SourceSection =
        inner.sourceLocation ?: super<SylviaVal>.getSourceLocation()

    @ExportMessage
    @Contract(pure = true)
    fun throwException(): RuntimeException {
        contract {
            returns() implies false
        }
        throw inner
    }

    @Contract("-> new", pure = true)
    override fun clone(): SylviaExceptionVal = SylviaExceptionVal(inner)
}
