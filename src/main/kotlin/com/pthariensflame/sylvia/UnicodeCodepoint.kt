package com.pthariensflame.sylvia

import com.ibm.icu.lang.UCharacter
import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.CompilerDirectives.SLOWPATH_PROBABILITY
import com.oracle.truffle.api.CompilerDirectives.ValueType
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.TruffleObject
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import org.jetbrains.annotations.Contract
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExportLibrary(InteropLibrary::class)
@ValueType
@OptIn(ExperimentalContracts::class)
data class UnicodeCodepoint(
    @JvmField val value: Int
) : Comparable<UnicodeCodepoint>, TruffleObject, Cloneable {
    init {
        if (CompilerDirectives.injectBranchProbability(SLOWPATH_PROBABILITY, !UCharacter.isLegal(value))) {
            throw IllegalArgumentException(
                "Could not construct UnicodeCodepoint value:" +
                        "U+${value.toString(16)} is not a legal Unicode codepoint"
            )
        }
    }

    companion object {
        @JvmStatic
        @Contract(pure = true)
        fun fromInt(v: Int): UnicodeCodepoint? {
            contract {
                returns()
            }
            return if (UCharacter.isLegal(v)) {
                UnicodeCodepoint(v)
            } else {
                null
            }
        }
    }

    constructor(c: Char) :
            this(c.toInt())

    constructor(lead: Char, trail: Char) :
            this(UCharacter.getCodePoint(lead, trail))

    @Contract(pure = true)
    override fun compareTo(other: UnicodeCodepoint): Int =
        value.compareTo(other.value)

    @ExportMessage
    @Contract(pure = true)
    fun isString(): Boolean = asStringChecked() != null

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    @Contract(pure = true)
    fun asString(): String =
        asStringChecked() ?: throw UnsupportedMessageException.create()

    @Contract("-> new", pure = true)
    internal fun asStringChecked(): String? =
        UCharacter.toString(value)

    @Contract("-> new", pure = true)
    override fun clone(): UnicodeCodepoint {
        return copy()
    }
}
