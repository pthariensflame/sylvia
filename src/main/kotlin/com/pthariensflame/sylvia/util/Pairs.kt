package com.pthariensflame.sylvia.util

import org.graalvm.collections.Pair as GraalSDKPair

inline val <reified A : Any, reified B : Any> GraalSDKPair<A, B>.kotlinPair: Pair<A?, B?>
    get() = Pair(left, right)

inline val <reified A : Any, reified B : Any> Pair<A?, B?>.graalSDKPair: GraalSDKPair<A, B>
    get() = GraalSDKPair.create(first, second)
