package com.pthariensflame.sylvia

import com.oracle.truffle.api.dsl.ImplicitCast
import com.oracle.truffle.api.dsl.TypeSystem
import com.pthariensflame.sylvia.values.*
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
    String::class,
    StringVal::class,
    SylviaException::class,
    SylviaExceptionVal::class,
    SylviaVal::class,
)
@OptIn(ExperimentalContracts::class)
open class SylviaTruffleTypeSystem internal constructor() {
    companion object {
        @ImplicitCast
        @JvmStatic
        fun booleanToBoolVal(v: Boolean): BoolVal = BoolVal(v)

        @ImplicitCast
        @JvmStatic
        fun boolValToBoolean(v: BoolVal): Boolean = v.value

        @ImplicitCast
        @JvmStatic
        fun byteToShort(v: Byte): Short = v.toShort()

        @ImplicitCast
        @JvmStatic
        fun byteToInt(v: Byte): Int = v.toInt()

        @ImplicitCast
        @JvmStatic
        fun byteToLong(v: Byte): Long = v.toLong()

        @ImplicitCast
        @JvmStatic
        fun byteToBigIntVal(v: Byte): BigIntVal = BigIntVal(v.toLong().toBigInteger())

        @ImplicitCast
        @JvmStatic
        fun byteToFloat(v: Byte): Float = v.toFloat()

        @ImplicitCast
        @JvmStatic
        fun byteToDouble(v: Byte): Double = v.toDouble()

        @ImplicitCast
        @JvmStatic
        fun byteToBigFloatVal(v: Byte): BigFloatVal = BigFloatVal(v.toLong().toBigDecimal(MathContext.UNLIMITED))

        @ImplicitCast
        @JvmStatic
        fun shortToInt(v: Short): Int = v.toInt()

        @ImplicitCast
        @JvmStatic
        fun shortToLong(v: Short): Long = v.toLong()

        @ImplicitCast
        @JvmStatic
        fun shortToBigIntVal(v: Short): BigIntVal = BigIntVal(v.toLong().toBigInteger())

        @ImplicitCast
        @JvmStatic
        fun shortToFloat(v: Short): Float = v.toFloat()

        @ImplicitCast
        @JvmStatic
        fun shortToDouble(v: Short): Double = v.toDouble()

        @ImplicitCast
        @JvmStatic
        fun shortToBigFloatVal(v: Short): BigFloatVal = BigFloatVal(v.toLong().toBigDecimal(MathContext.UNLIMITED))

        @ImplicitCast
        @JvmStatic
        fun intToLong(v: Int): Long = v.toLong()

        @ImplicitCast
        @JvmStatic
        fun intToDouble(v: Int): Double = v.toDouble()

        @ImplicitCast
        @JvmStatic
        fun intToBigIntVal(v: Int): BigIntVal = BigIntVal(v.toBigInteger())

        @ImplicitCast
        @JvmStatic
        fun intToBigFloatVal(v: Int): BigFloatVal = BigFloatVal(v.toBigDecimal(MathContext.UNLIMITED))

        @ImplicitCast
        @JvmStatic
        fun longToBigIntVal(v: Long): BigIntVal = BigIntVal(v.toBigInteger())

        @ImplicitCast
        @JvmStatic
        fun longToBigFloatVal(v: Long): BigFloatVal = BigFloatVal(v.toBigDecimal(MathContext.UNLIMITED))

        @ImplicitCast
        @JvmStatic
        fun bigIntValToBigFloatVal(v: BigIntVal): BigFloatVal =
            BigFloatVal(v.value.toBigDecimal(0, MathContext.UNLIMITED))

        @ImplicitCast
        @JvmStatic
        fun floatToDouble(v: Float): Double = v.toDouble()

        @ImplicitCast
        @JvmStatic
        fun floatToBigFloatVal(v: Float): BigFloatVal = BigFloatVal(v.toBigDecimal(MathContext.UNLIMITED))

        @ImplicitCast
        @JvmStatic
        fun doubleToBigFloatVal(v: Double): BigFloatVal = BigFloatVal(v.toBigDecimal(MathContext.UNLIMITED))

        @ImplicitCast
        @JvmStatic
        fun stringToStringVal(v: String): StringVal = StringVal(v)

        @ImplicitCast
        @JvmStatic
        fun stringValToString(v: StringVal): String = v.value

        @ImplicitCast
        @JvmStatic
        fun exceptionToSylvExVal(v: SylviaException): SylviaExceptionVal = SylviaExceptionVal(v)

        @ImplicitCast
        @JvmStatic
        fun sylvExValToException(v: SylviaExceptionVal): SylviaException = v.inner
    }
}


