package com.pthariensflame.sylvia

import java.math.BigInteger

internal object TypeLimits {
    @JvmStatic
    val MIN_BYTE by lazy { BigInteger.valueOf(Byte.MIN_VALUE.toLong()) }

    @JvmStatic
    val MAX_BYTE by lazy { BigInteger.valueOf(Byte.MAX_VALUE.toLong()) }

    @JvmStatic
    val MIN_SHORT by lazy { BigInteger.valueOf(Short.MIN_VALUE.toLong()) }

    @JvmStatic
    val MAX_SHORT by lazy { BigInteger.valueOf(Short.MAX_VALUE.toLong()) }

    @JvmStatic
    val MIN_INT by lazy { BigInteger.valueOf(Int.MIN_VALUE.toLong()) }

    @JvmStatic
    val MAX_INT by lazy { BigInteger.valueOf(Int.MAX_VALUE.toLong()) }

    @JvmStatic
    val MIN_LONG by lazy { BigInteger.valueOf(Long.MIN_VALUE) }

    @JvmStatic
    val MAX_LONG by lazy { BigInteger.valueOf(Long.MAX_VALUE) }
}