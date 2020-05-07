package com.pthariensflame.sylvia.values

import com.pthariensflame.sylvia.util.LazyConstant

internal object TypeLimits {
    @JvmStatic
    val MIN_BYTE by LazyConstant { Byte.MIN_VALUE.toLong().toBigInteger() }

    @JvmStatic
    val MAX_BYTE by LazyConstant { Byte.MAX_VALUE.toLong().toBigInteger() }

    @JvmStatic
    val MIN_SHORT by LazyConstant { Short.MIN_VALUE.toLong().toBigInteger() }

    @JvmStatic
    val MAX_SHORT by LazyConstant { Short.MAX_VALUE.toLong().toBigInteger() }

    @JvmStatic
    val MIN_INT by LazyConstant { Int.MIN_VALUE.toBigInteger() }

    @JvmStatic
    val MAX_INT by LazyConstant { Int.MAX_VALUE.toBigInteger() }

    @JvmStatic
    val MIN_LONG by LazyConstant { Long.MIN_VALUE.toBigInteger() }

    @JvmStatic
    val MAX_LONG by LazyConstant { Long.MAX_VALUE.toBigInteger() }

//    @JvmStatic
//    val MIN_BYTE_DEC by LazyConstant { Byte.MIN_VALUE.toLong().toBigDecimal() }
//
//    @JvmStatic
//    val MAX_BYTE_DEC by LazyConstant { Byte.MAX_VALUE.toLong().toBigDecimal() }
//
//    @JvmStatic
//    val MIN_SHORT_DEC by LazyConstant { Short.MIN_VALUE.toLong().toBigDecimal() }
//
//    @JvmStatic
//    val MAX_SHORT_DEC by LazyConstant { Short.MAX_VALUE.toLong().toBigDecimal() }
//
//    @JvmStatic
//    val MIN_INT_DEC by LazyConstant { Int.MIN_VALUE.toBigDecimal() }
//
//    @JvmStatic
//    val MAX_INT_DEC by LazyConstant { Int.MAX_VALUE.toBigDecimal() }
//
//    @JvmStatic
//    val MIN_LONG_DEC by LazyConstant { Long.MIN_VALUE.toBigDecimal() }
//
//    @JvmStatic
//    val MAX_LONG_DEC by LazyConstant { Long.MAX_VALUE.toBigDecimal() }
}