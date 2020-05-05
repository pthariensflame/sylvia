package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import com.pthariensflame.sylvia.values.TypeLimits.MAX_BYTE
import com.pthariensflame.sylvia.values.TypeLimits.MAX_INT
import com.pthariensflame.sylvia.values.TypeLimits.MAX_LONG
import com.pthariensflame.sylvia.values.TypeLimits.MAX_SHORT
import com.pthariensflame.sylvia.values.TypeLimits.MIN_BYTE
import com.pthariensflame.sylvia.values.TypeLimits.MIN_INT
import com.pthariensflame.sylvia.values.TypeLimits.MIN_LONG
import com.pthariensflame.sylvia.values.TypeLimits.MIN_SHORT
import org.jetbrains.annotations.Contract
import java.math.BigInteger
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExportLibrary(InteropLibrary::class)
@CompilerDirectives.ValueType
@OptIn(ExperimentalContracts::class)
data class BigIntVal(@JvmField val value: BigInteger) : SylviaVal(), Comparable<BigIntVal>, Cloneable {

    @ExportMessage
    @Contract("-> true", pure = true)
    fun isNumber(): Boolean {
        contract {
            returns(true)
        }
        return true
    }

    @ExportMessage
    @Contract(pure = true)
    fun fitsInByte(): Boolean = value >= MIN_BYTE && value <= MAX_BYTE

    @ExportMessage
    @Contract(pure = true)
    fun fitsInShort(): Boolean = value >= MIN_SHORT && value <= MAX_SHORT

    @ExportMessage
    @Contract(pure = true)
    fun fitsInInt(): Boolean = value >= MIN_INT && value <= MAX_INT

    @ExportMessage
    @Contract(pure = true)
    fun fitsInLong(): Boolean = value >= MIN_LONG && value <= MAX_LONG

    @ExportMessage
    @Contract(pure = true)
    fun fitsInFloat(): Boolean = fitsInShort()

    @ExportMessage
    @Contract(pure = true)
    fun fitsInDouble(): Boolean = fitsInInt()

    @ExportMessage
    @Contract(pure = true)
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
    @Contract(pure = true)
    fun asShort(): Short {
        try {
            return value.shortValueExact()
        } catch (ex: ArithmeticException) {
            throw UnsupportedMessageException.create().initCause(ex)
        }
    }

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    @Contract(pure = true)
    fun asInt(): Int {
        try {
            return value.intValueExact()
        } catch (ex: ArithmeticException) {
            throw UnsupportedMessageException.create().initCause(ex)
        }
    }

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    @Contract(pure = true)
    fun asLong(): Long {
        try {
            return value.longValueExact()
        } catch (ex: ArithmeticException) {
            throw UnsupportedMessageException.create().initCause(ex)
        }
    }

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    @Contract(pure = true)
    fun asFloat(): Float = asShort().toFloat()

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    @Contract(pure = true)
    fun asDouble(): Double = asInt().toDouble()

    @Contract(pure = true)
    override fun compareTo(other: BigIntVal): Int = value.compareTo(other.value)

    @Contract("-> new", pure = true)
    override fun clone(): BigIntVal = BigIntVal(value)
}