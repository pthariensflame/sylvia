package com.pthariensflame.sylvia.values

import com.pthariensflame.sylvia.util.CFLazy

internal object TypeLimits {
    @JvmStatic
    val MIN_BYTE by CFLazy { Byte.MIN_VALUE.toLong().toBigInteger() }

    @JvmStatic
    val MAX_BYTE by CFLazy { Byte.MAX_VALUE.toLong().toBigInteger() }

    @JvmStatic
    val MIN_SHORT by CFLazy { Short.MIN_VALUE.toLong().toBigInteger() }

    @JvmStatic
    val MAX_SHORT by CFLazy { Short.MAX_VALUE.toLong().toBigInteger() }

    @JvmStatic
    val MIN_INT by CFLazy { Int.MIN_VALUE.toBigInteger() }

    @JvmStatic
    val MAX_INT by CFLazy { Int.MAX_VALUE.toBigInteger() }

    @JvmStatic
    val MIN_LONG by CFLazy { Long.MIN_VALUE.toBigInteger() }

    @JvmStatic
    val MAX_LONG by CFLazy { Long.MAX_VALUE.toBigInteger() }

//    @JvmStatic
//    val MIN_BYTE_DEC by CFLazy { Byte.MIN_VALUE.toLong().toBigDecimal() }
//
//    @JvmStatic
//    val MAX_BYTE_DEC by CFLazy { Byte.MAX_VALUE.toLong().toBigDecimal() }
//
//    @JvmStatic
//    val MIN_SHORT_DEC by CFLazy { Short.MIN_VALUE.toLong().toBigDecimal() }
//
//    @JvmStatic
//    val MAX_SHORT_DEC by CFLazy { Short.MAX_VALUE.toLong().toBigDecimal() }
//
//    @JvmStatic
//    val MIN_INT_DEC by CFLazy { Int.MIN_VALUE.toBigDecimal() }
//
//    @JvmStatic
//    val MAX_INT_DEC by CFLazy { Int.MAX_VALUE.toBigDecimal() }
//
//    @JvmStatic
//    val MIN_LONG_DEC by CFLazy { Long.MIN_VALUE.toBigDecimal() }
//
//    @JvmStatic
//    val MAX_LONG_DEC by CFLazy { Long.MAX_VALUE.toBigDecimal() }
}