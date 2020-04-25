package com.pthariensflame.sylvia

import com.ibm.icu.lang.UCharacter
import com.oracle.truffle.api.CompilerDirectives.ValueType
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.TruffleObject
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExportLibrary(InteropLibrary::class)
@ValueType
@OptIn(ExperimentalContracts::class)
data class UnicodeCodepoint(
    @JvmField val value: Int
) : Comparable<UnicodeCodepoint>, TruffleObject {
    constructor(c: Char) :
            this(c.toInt())

    constructor(lead: Char, trail: Char) :
            this(UCharacter.getCodePoint(lead, trail))

    override fun compareTo(other: UnicodeCodepoint): Int =
        value.compareTo(other.value)

    @ExportMessage
    fun isString(): Boolean = UCharacter.toString(value) != null

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    fun asString(): String =
        UCharacter.toString(value) ?: throw UnsupportedMessageException.create()

    internal fun asStringChecked(): String? =
        UCharacter.toString(value)
}
