package com.pthariensflame.sylvia

import com.oracle.truffle.api.dsl.ImplicitCast
import com.oracle.truffle.api.dsl.TypeSystem
import com.pthariensflame.sylvia.ast.expressions.StringLiteralExpressionNode
import com.pthariensflame.sylvia.values.*
import com.pthariensflame.sylvia.values.types.SylviaType
import org.jetbrains.annotations.Contract
import java.math.MathContext
import kotlin.contracts.ExperimentalContracts

@TypeSystem(
    Boolean::class,
    BoolVal::class,
    Byte::class,
    Short::class,
    Int::class,
    Long::class,
    BigIntVal::class,
    Float::class,
    Double::class,
    BigFloatVal::class,
    Char::class,
    UnicodeCodepoint::class,
    String::class,
    StringVal::class,
    SylviaException::class,
    SylviaExceptionVal::class,
    StringLiteralExpressionNode.Kind::class,
    SylviaType::class,
    SylviaVal::class,
)
@OptIn(ExperimentalContracts::class)
open class SylviaTruffleTypeSystem internal constructor() {
    companion object {
        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun booleanToBoolVal(v: Boolean): BoolVal = BoolVal(v)

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun boolValToBoolean(v: BoolVal): Boolean = v.value

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun byteToShort(v: Byte): Short = v.toShort()

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun byteToInt(v: Byte): Int = v.toInt()

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun byteToLong(v: Byte): Long = v.toLong()

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun byteToBigIntVal(v: Byte): BigIntVal = BigIntVal(v.toLong().toBigInteger())

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun byteToFloat(v: Byte): Float = v.toFloat()

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun byteToDouble(v: Byte): Double = v.toDouble()

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun byteToBigFloatVal(v: Byte): BigFloatVal = BigFloatVal(v.toLong().toBigDecimal(MathContext.UNLIMITED))

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun shortToInt(v: Short): Int = v.toInt()

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun shortToLong(v: Short): Long = v.toLong()

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun shortToBigIntVal(v: Short): BigIntVal = BigIntVal(v.toLong().toBigInteger())

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun shortToFloat(v: Short): Float = v.toFloat()

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun shortToDouble(v: Short): Double = v.toDouble()

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun shortToBigFloatVal(v: Short): BigFloatVal = BigFloatVal(v.toLong().toBigDecimal(MathContext.UNLIMITED))

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun intToLong(v: Int): Long = v.toLong()

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun intToDouble(v: Int): Double = v.toDouble()

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun intToBigIntVal(v: Int): BigIntVal = BigIntVal(v.toBigInteger())

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun intToBigFloatVal(v: Int): BigFloatVal = BigFloatVal(v.toBigDecimal(MathContext.UNLIMITED))

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun longToBigIntVal(v: Long): BigIntVal = BigIntVal(v.toBigInteger())

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun longToBigFloatVal(v: Long): BigFloatVal = BigFloatVal(v.toBigDecimal(MathContext.UNLIMITED))

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun bigIntValToBigFloatVal(v: BigIntVal): BigFloatVal =
            BigFloatVal(v.value.toBigDecimal(0, MathContext.UNLIMITED))

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun floatToDouble(v: Float): Double = v.toDouble()

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun floatToBigFloatVal(v: Float): BigFloatVal = BigFloatVal(v.toBigDecimal(MathContext.UNLIMITED))

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun doubleToBigFloatVal(v: Double): BigFloatVal = BigFloatVal(v.toBigDecimal(MathContext.UNLIMITED))

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun charToUnicodeCodepoint(v: Char): UnicodeCodepoint = UnicodeCodepoint(v)

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun charToString(v: Char): String = v.toString()

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun charToStringVal(v: Char): StringVal = StringVal(v.toString())

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun unicodeCodepointToString(v: UnicodeCodepoint): String = v.asString()

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun unicodeCodepointToStringVal(v: UnicodeCodepoint): StringVal = StringVal(v.asString())

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun stringToStringVal(v: String): StringVal = StringVal(v)

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun stringValToString(v: StringVal): String = v.value

        @ImplicitCast
        @JvmStatic
        @Contract("-> new", pure = true)
        fun exceptionToSylvExVal(v: SylviaException): SylviaExceptionVal = SylviaExceptionVal(v)

        @ImplicitCast
        @JvmStatic
        @Contract(pure = true)
        fun sylvExValToException(v: SylviaExceptionVal): SylviaException = v.inner
    }
}


