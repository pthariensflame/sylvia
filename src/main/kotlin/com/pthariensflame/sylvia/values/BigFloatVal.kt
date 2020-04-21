package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import com.pthariensflame.sylvia.values.TypeLimits.MAX_BYTE_DEC
import com.pthariensflame.sylvia.values.TypeLimits.MAX_INT_DEC
import com.pthariensflame.sylvia.values.TypeLimits.MAX_LONG_DEC
import com.pthariensflame.sylvia.values.TypeLimits.MAX_SHORT_DEC
import com.pthariensflame.sylvia.values.TypeLimits.MIN_BYTE_DEC
import com.pthariensflame.sylvia.values.TypeLimits.MIN_INT_DEC
import com.pthariensflame.sylvia.values.TypeLimits.MIN_LONG_DEC
import com.pthariensflame.sylvia.values.TypeLimits.MIN_SHORT_DEC
import java.math.BigDecimal
import java.math.RoundingMode

@ExportLibrary(InteropLibrary::class)
@CompilerDirectives.ValueType
data class BigFloatVal private constructor(@JvmField val value: BigDecimal) : SylviaVal(), Comparable<BigFloatVal> {
    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke(v: BigDecimal): BigFloatVal = BigFloatVal(v.setScale(0, RoundingMode.UNNECESSARY))
    }

    @ExportMessage
    fun isNumber(): Boolean = true

    @ExportMessage
    fun fitsInByte(): Boolean = value >= MIN_BYTE_DEC && value <= MAX_BYTE_DEC

    @ExportMessage
    fun fitsInShort(): Boolean = value >= MIN_SHORT_DEC && value <= MAX_SHORT_DEC

    @ExportMessage
    fun fitsInInt(): Boolean = value >= MIN_INT_DEC && value <= MAX_INT_DEC

    @ExportMessage
    fun fitsInLong(): Boolean = value >= MIN_LONG_DEC && value <= MAX_LONG_DEC

    @ExportMessage
    fun fitsInFloat(): Boolean = fitsInShort()

    @ExportMessage
    fun fitsInDouble(): Boolean = fitsInInt()

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    fun asByte(): Byte {
        try {
            return value.byteValueExact()
        } catch (ex: ArithmeticException) {
            throw UnsupportedMessageException.create().initCause(ex)
        }
    }

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    fun asShort(): Short {
        try {
            return value.shortValueExact()
        } catch (ex: ArithmeticException) {
            throw UnsupportedMessageException.create().initCause(ex)
        }
    }

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    fun asInt(): Int {
        try {
            return value.intValueExact()
        } catch (ex: ArithmeticException) {
            throw UnsupportedMessageException.create().initCause(ex)
        }
    }

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    fun asLong(): Long {
        try {
            return value.longValueExact()
        } catch (ex: ArithmeticException) {
            throw UnsupportedMessageException.create().initCause(ex)
        }
    }

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    fun asFloat(): Float = asShort().toFloat()

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    fun asDouble(): Double = asInt().toDouble()

    override fun compareTo(other: BigFloatVal): Int = value.compareTo(other.value)
}