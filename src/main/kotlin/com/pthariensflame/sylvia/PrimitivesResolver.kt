package com.pthariensflame.sylvia

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.CompilerDirectives.LIKELY_PROBABILITY
import com.oracle.truffle.api.CompilerDirectives.SLOWPATH_PROBABILITY
import com.oracle.truffle.api.dsl.NodeFactory
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.nodes.Node
import com.pthariensflame.sylvia.ast.SylviaNode
import com.pthariensflame.sylvia.values.SylviaException
import org.jetbrains.annotations.Contract

fun interface PrimitivesResolver {
    @ExportLibrary(InteropLibrary::class)
    class AmbiguousPrimitiveIDException
    @JvmOverloads constructor(
        @JvmField val primID: String = "",
        @JvmField private val requestingNode: Node? = null,
    ) : SylviaException() {
        @Contract("-> this", pure = true)
        override fun fillInStackTrace(): Throwable = this

        @Contract(pure = true)
        override fun getLocation(): Node? = requestingNode

        @Contract("-> true", pure = true)
        override fun isSyntaxError(): Boolean = true
    }

    @Throws(AmbiguousPrimitiveIDException::class)
    @JvmDefault
    fun resolvePrimitiveByID(
        primID: String,
        requestingNode: Node?,
    ): @JvmWildcard NodeFactory<out SylviaNode>?

    object DefaultPrimitivesResolver :
        PrimitivesResolver {
        @Throws(AmbiguousPrimitiveIDException::class)
        override fun resolvePrimitiveByID(
            primID: String,
            requestingNode: Node?,
        ): @JvmWildcard NodeFactory<out SylviaNode>? {
            return null // TODO
        }
    }

    @Throws(AmbiguousPrimitiveIDException::class)
    @JvmDefault
    fun resolvePrimitiveByID(primID: String): @JvmWildcard NodeFactory<out SylviaNode>? =
        resolvePrimitiveByID(primID, null)

    @JvmDefault
    fun plusFallback(fallback: PrimitivesResolver): PrimitivesResolver =
        PrimitivesResolver { primID, requestingNode ->
            resolvePrimitiveByID(primID, requestingNode)
                ?: fallback.resolvePrimitiveByID(primID, requestingNode)
        }

    @JvmDefault
    fun plusPreferred(preferred: PrimitivesResolver): PrimitivesResolver =
        PrimitivesResolver { primID, requestingNode ->
            preferred.resolvePrimitiveByID(primID, requestingNode)
                ?: resolvePrimitiveByID(primID, requestingNode)
        }

    @JvmDefault
    operator fun plus(other: PrimitivesResolver): PrimitivesResolver =
        PrimitivesResolver { primID, requestingNode ->
            val x = resolvePrimitiveByID(primID, requestingNode)
            val y = other.resolvePrimitiveByID(primID, requestingNode)
            if (CompilerDirectives.injectBranchProbability(0.5, null != x)
            ) {
                if (CompilerDirectives.injectBranchProbability(SLOWPATH_PROBABILITY, null != y)
                ) {
                    CompilerDirectives.transferToInterpreterAndInvalidate()
                    throw AmbiguousPrimitiveIDException(primID, requestingNode)
                } else {
                    x
                }
            } else {
                if (CompilerDirectives.injectBranchProbability(LIKELY_PROBABILITY, null != y)
                ) {
                    y
                } else {
                    null
                }
            }
        }
}