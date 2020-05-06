package com.pthariensflame.sylvia.util

import org.graalvm.collections.Pair as GraalSDKPair
import org.antlr.v4.runtime.misc.Pair as AntlrPair

inline val <reified A : Any, reified B : Any> GraalSDKPair<A, B>.kotlinPair: Pair<A?, B?>
    get() = Pair(left, right)

inline val <reified A, reified B> AntlrPair<A, B>.kotlinPair: Pair<A, B>
    get() = Pair(a, b)

inline val <reified A : Any, reified B : Any> Pair<A?, B?>.graalSDKPair: GraalSDKPair<A, B>
    get() = GraalSDKPair.create(first, second)

inline val <reified A : Any, reified B : Any> AntlrPair<A?, B?>.graalSDKPair: GraalSDKPair<A, B>
    get() = GraalSDKPair.create(a, b)

inline val <reified A, reified B> Pair<A, B>.antlrPair: AntlrPair<A, B>
    get() = AntlrPair(first, second)

inline val <reified A : Any, reified B : Any> GraalSDKPair<A, B>.antlrPair: AntlrPair<A?, B?>
    get() = AntlrPair(left, right)
