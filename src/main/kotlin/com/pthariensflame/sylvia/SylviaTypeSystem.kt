package com.pthariensflame.sylvia

import com.oracle.truffle.api.dsl.ImplicitCast
import com.oracle.truffle.api.dsl.TypeCast
import com.oracle.truffle.api.dsl.TypeCheck
import com.oracle.truffle.api.dsl.TypeSystem
import com.pthariensflame.sylvia.values.*
import java.math.BigInteger
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@TypeSystem(
    NoReturnVal::class,
    Boolean::class,
    BoolVal::class,
    Int::class,
    Long::class,
    BigIntVal::class,
    Double::class,
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
        fun asNoReturnVal(obj: Any?): NoReturnVal {
            return NoReturnVal
        }

        @ImplicitCast
        @JvmStatic
        fun booleanToBoolVal(v: Boolean): BoolVal = BoolVal(v)

        @ImplicitCast
        @JvmStatic
        fun intToLong(v: Int): Long = v.toLong()

        @ImplicitCast
        @JvmStatic
        fun intToBigIntVal(v: Int): BigIntVal = BigIntVal(BigInteger.valueOf(v.toLong()))

        @ImplicitCast
        @JvmStatic
        fun longToBigIntVal(v: Long): BigIntVal = BigIntVal(BigInteger.valueOf(v))

        @ImplicitCast
        @JvmStatic
        fun stringToStringVal(v: String): StringVal = StringVal(v)
    }
}


