package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal
import com.oracle.truffle.api.CompilerDirectives.ValueType
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
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
data class BoolVal(@JvmField val value: Boolean) : SylviaVal(), Comparable<BoolVal>, Cloneable {
    @CompilationFinal
    override var originatingNode: ExpressionNode? = null

    @ExportMessage
    @Contract("-> true", pure = true)
    fun isBoolean(): Boolean {
        contract {
            returns(true)
        }
        return true
    }

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    @Contract(pure = true)
    fun asBoolean(): Boolean = value

    @Contract(pure = true)
    override fun compareTo(other: BoolVal): Int = value.compareTo(other.value)

    @Contract("-> new", pure = true)
    override fun clone(): BoolVal = BoolVal(value)
}