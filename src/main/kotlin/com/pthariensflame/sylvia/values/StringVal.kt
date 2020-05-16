package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal
import com.oracle.truffle.api.CompilerDirectives.ValueType
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import com.pthariensflame.sylvia.UnicodeCodepoint
import com.pthariensflame.sylvia.ast.expressions.StringLiteralExpressionNode
import org.jetbrains.annotations.Contract
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


@ExportLibrary.Repeat(
    ExportLibrary(InteropLibrary::class),
//    ExportLibrary(LSPLibrary::class),
)
@ValueType
@OptIn(ExperimentalContracts::class)
data class StringVal(@JvmField val value: String) : SylviaVal(), Comparable<StringVal>, Cloneable {
    @field:CompilationFinal
    override var originatingNode: StringLiteralExpressionNode? = null

    constructor(c: Char) : this(
        c.toString()
    )

    constructor(codepoint: UnicodeCodepoint) : this(
        codepoint.asStringChecked()
            ?: throw IllegalArgumentException(codepoint.toString())
    )

    @ExportMessage
    @Contract("-> true", pure = true)
    fun isString(): Boolean {
        contract {
            returns(true)
        }
        return true
    }

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    @Contract(pure = true)
    fun asString(): String = value

    @Contract(pure = true)
    override fun compareTo(other: StringVal): Int = value.compareTo(other.value)

    @Contract("-> new", pure = true)
    override fun clone(): StringVal = StringVal(value)
}