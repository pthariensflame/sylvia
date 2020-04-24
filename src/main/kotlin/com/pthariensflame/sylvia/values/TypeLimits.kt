package com.pthariensflame.sylvia.values

internal object TypeLimits {
    @JvmStatic
    val MIN_BYTE by lazy { Byte.MIN_VALUE.toLong().toBigInteger() }

    @JvmStatic
    val MAX_BYTE by lazy { Byte.MAX_VALUE.toLong().toBigInteger() }

    @JvmStatic
    val MIN_SHORT by lazy { Short.MIN_VALUE.toLong().toBigInteger() }

    @JvmStatic
    val MAX_SHORT by lazy { Short.MAX_VALUE.toLong().toBigInteger() }

    @JvmStatic
    val MIN_INT by lazy { Int.MIN_VALUE.toBigInteger() }

    @JvmStatic
    val MAX_INT by lazy { Int.MAX_VALUE.toBigInteger() }

    @JvmStatic
    val MIN_LONG by lazy { Long.MIN_VALUE.toBigInteger() }

    @JvmStatic
    val MAX_LONG by lazy { Long.MAX_VALUE.toBigInteger() }

//    @JvmStatic
//    val MIN_BYTE_DEC by lazy { Byte.MIN_VALUE.toLong().toBigDecimal() }
//
//    @JvmStatic
//    val MAX_BYTE_DEC by lazy { Byte.MAX_VALUE.toLong().toBigDecimal() }
//
//    @JvmStatic
//    val MIN_SHORT_DEC by lazy { Short.MIN_VALUE.toLong().toBigDecimal() }
//
//    @JvmStatic
//    val MAX_SHORT_DEC by lazy { Short.MAX_VALUE.toLong().toBigDecimal() }
//
//    @JvmStatic
//    val MIN_INT_DEC by lazy { Int.MIN_VALUE.toBigDecimal() }
//
//    @JvmStatic
//    val MAX_INT_DEC by lazy { Int.MAX_VALUE.toBigDecimal() }
//
//    @JvmStatic
//    val MIN_LONG_DEC by lazy { Long.MIN_VALUE.toBigDecimal() }
//
//    @JvmStatic
//    val MAX_LONG_DEC by lazy { Long.MAX_VALUE.toBigDecimal() }
}