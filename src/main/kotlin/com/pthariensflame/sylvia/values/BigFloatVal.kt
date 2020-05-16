package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.CompilerAsserts
import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import com.pthariensflame.sylvia.ast.expressions.ExpressionNode
import org.jetbrains.annotations.Contract
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExportLibrary.Repeat(
    ExportLibrary(InteropLibrary::class),
//    ExportLibrary(LSPLibrary::class),
)
@CompilerDirectives.ValueType
@OptIn(ExperimentalContracts::class)
@Suppress("ProtectedInFinal")
data class BigFloatVal protected constructor(@JvmField val value: BigDecimal) : SylviaVal(), Comparable<BigFloatVal>,
    Cloneable {
    override var originatingNode: ExpressionNode?
        get() = TODO("Not yet implemented")
        set(newNode) = TODO("Not yet implemented")

    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke(v: BigDecimal): BigFloatVal = BigFloatVal(v.setScale(0, RoundingMode.UNNECESSARY))
    }

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
    fun fitsInByte(): Boolean {
        return try {
            value.byteValueExact() // return ignored
            true
        } catch (ex: ArithmeticException) {
            false
        }
    }

    @ExportMessage
    @Contract(pure = true)
    fun fitsInShort(): Boolean {
        return try {
            value.shortValueExact() // return ignored
            true
        } catch (ex: ArithmeticException) {
            false
        }
    }

    @ExportMessage
    @Contract(pure = true)
    fun fitsInInt(): Boolean {
        return try {
            value.intValueExact() // return ignored
            true
        } catch (ex: ArithmeticException) {
            false
        }
    }

    @ExportMessage
    @Contract(pure = true)
    fun fitsInLong(): Boolean {
        return try {
            value.longValueExact() // return ignored
            true
        } catch (ex: ArithmeticException) {
            false
        }
    }

    @ExportMessage
    @Contract(pure = true)
    fun fitsInFloat(): Boolean {
        val v = value.toFloat()
        return v.toBigDecimal(MathContext.UNLIMITED) == value
    }

    @ExportMessage
    @Contract(pure = true)
    fun fitsInDouble(): Boolean {
        val v = value.toDouble()
        return v.toBigDecimal(MathContext.UNLIMITED) == value
    }

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    @Contract(pure = true)
    fun asByte(): Byte {
        try {
            return value.byteValueExact()
        } catch (ex: ArithmeticException) {
            CompilerAsserts.neverPartOfCompilation("Exceptional case")
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
    fun asInt(): Int {
        try {
            return value.intValueExact()
        } catch (ex: ArithmeticException) {
            CompilerAsserts.neverPartOfCompilation("Exceptional case")
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
            CompilerAsserts.neverPartOfCompilation("Exceptional case")
            throw UnsupportedMessageException.create().initCause(ex)
        }
    }

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    @Contract(pure = true)
    fun asFloat(): Float {
        val v = value.toFloat()
        if (v.toBigDecimal(MathContext.UNLIMITED) == value) {
            return v
        } else {
            CompilerAsserts.neverPartOfCompilation("Exceptional case")
            throw UnsupportedMessageException.create()
        }
    }

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    @Contract(pure = true)
    fun asDouble(): Double {
        val v = value.toDouble()
        if (v.toBigDecimal(MathContext.UNLIMITED) == value) {
            return v
        } else {
            CompilerAsserts.neverPartOfCompilation("Exceptional case")
            throw UnsupportedMessageException.create()
        }
    }

    @Contract(pure = true)
    override fun compareTo(other: BigFloatVal): Int = value.compareTo(other.value)

    @Contract("-> new", pure = true)
    override fun clone(): BigFloatVal = BigFloatVal(value)
}