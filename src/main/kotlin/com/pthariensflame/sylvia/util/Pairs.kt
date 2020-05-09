package com.pthariensflame.sylvia.util

import org.intellij.lang.annotations.Flow
import org.graalvm.collections.Pair as GraalSDKPair
import org.antlr.v4.runtime.misc.Pair as AntlrPair

@get:Flow(
    source = Flow.THIS_SOURCE,
    sourceIsContainer = true,
    target = Flow.RETURN_METHOD_TARGET,
    targetIsContainer = true
)
inline val <A : Any, B : Any> GraalSDKPair<A, B>.kotlinPair: Pair<A?, B?>
    get() = Pair(left, right)

@get:Flow(
    source = Flow.THIS_SOURCE,
    sourceIsContainer = true,
    target = Flow.RETURN_METHOD_TARGET,
    targetIsContainer = true
)
inline val <A, B> AntlrPair<A, B>.kotlinPair: Pair<A, B>
    get() = Pair(a, b)

@get:Flow(
    source = Flow.THIS_SOURCE,
    sourceIsContainer = true,
    target = Flow.RETURN_METHOD_TARGET,
    targetIsContainer = true
)
inline val <A : Any, B : Any> Pair<A?, B?>.graalSDKPair: GraalSDKPair<A, B>
    get() = GraalSDKPair.create(first, second)

@get:Flow(
    source = Flow.THIS_SOURCE,
    sourceIsContainer = true,
    target = Flow.RETURN_METHOD_TARGET,
    targetIsContainer = true
)
inline val <A : Any, B : Any> AntlrPair<A?, B?>.graalSDKPair: GraalSDKPair<A, B>
    get() = GraalSDKPair.create(a, b)

@get:Flow(
    source = Flow.THIS_SOURCE,
    sourceIsContainer = true,
    target = Flow.RETURN_METHOD_TARGET,
    targetIsContainer = true
)
inline val <A, B> Pair<A, B>.antlrPair: AntlrPair<A, B>
    get() = AntlrPair(first, second)

@get:Flow(
    source = Flow.THIS_SOURCE,
    sourceIsContainer = true,
    target = Flow.RETURN_METHOD_TARGET,
    targetIsContainer = true
)
inline val <A : Any, B : Any> GraalSDKPair<A, B>.antlrPair: AntlrPair<A?, B?>
    get() = AntlrPair(left, right)
