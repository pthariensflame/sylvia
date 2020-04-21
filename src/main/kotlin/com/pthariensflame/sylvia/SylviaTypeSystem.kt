package com.pthariensflame.sylvia

import com.oracle.truffle.api.dsl.ImplicitCast
import com.oracle.truffle.api.dsl.TypeCast
import com.oracle.truffle.api.dsl.TypeCheck
import com.oracle.truffle.api.dsl.TypeSystem
import com.pthariensflame.sylvia.values.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@TypeSystem(
    NoReturnVal::class,
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
    SylviaVal::class,
)
@OptIn(ExperimentalContracts::class)
open class SylviaTypeSystem internal constructor() {
    companion object {
        @TypeCheck(NoReturnVal::class)
        @JvmStatic
        fun isNoReturnVal(obj: Any?): Boolean {
            contract {
                returns(true) implies (obj is NoReturnVal)
                returns(false) implies (obj !is NoReturnVal)
            }
            return obj === NoReturnVal
        }

        @TypeCast(NoReturnVal::class)
        @JvmStatic
        fun asNoReturnVal(@Suppress("UNUSED_PARAMETER") obj: Any?): NoReturnVal {
            return NoReturnVal
        }

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
        fun shortToInt(v: Short): Int = v.toInt()

        @ImplicitCast
        @JvmStatic
        fun shortToLong(v: Short): Long = v.toLong()

        @ImplicitCast
        @JvmStatic
        fun intToLong(v: Int): Long = v.toLong()

        @ImplicitCast
        @JvmStatic
        fun intToBigIntVal(v: Int): BigIntVal = BigIntVal(v.toBigInteger())

        @ImplicitCast
        @JvmStatic
        fun intToBigFloatVal(v: Int): BigFloatVal = BigFloatVal(v.toBigDecimal())

        @ImplicitCast
        @JvmStatic
        fun longToBigIntVal(v: Long): BigIntVal = BigIntVal(v.toBigInteger())

        @ImplicitCast
        @JvmStatic
        fun longToBigFloatVal(v: Long): BigFloatVal = BigFloatVal(v.toBigDecimal())

        @ImplicitCast
        @JvmStatic
        fun bigIntValToBigFloatVal(v: BigIntVal): BigFloatVal = BigFloatVal(v.value.toBigDecimal())

        @ImplicitCast
        @JvmStatic
        fun floatToDouble(v: Float): Double = v.toDouble()

        @ImplicitCast
        @JvmStatic
        fun floatToBigFloatVal(v: Float): BigFloatVal = BigFloatVal(v.toBigDecimal())

        @ImplicitCast
        @JvmStatic
        fun doubleToBigFloatVal(v: Double): BigFloatVal = BigFloatVal(v.toBigDecimal())

        @ImplicitCast
        @JvmStatic
        fun stringToStringVal(v: String): StringVal = StringVal(v)

        @ImplicitCast
        @JvmStatic
        fun stringValToString(v: StringVal): String = v.value
    }
}


