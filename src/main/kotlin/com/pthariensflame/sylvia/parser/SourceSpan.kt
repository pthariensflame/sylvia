package com.pthariensflame.sylvia.parser

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary
import com.oracle.truffle.api.CompilerDirectives.ValueType
import com.oracle.truffle.api.source.Source
import com.oracle.truffle.api.source.SourceSection
import org.antlr.v4.runtime.ParserRuleContext
import org.jetbrains.annotations.Range

@ValueType
data class SourceSpan(
    @JvmField val start: @Range(from = 0, to = Int.MAX_VALUE.toLong()) Int,
    @JvmField val len: @Range(from = 0, to = Int.MAX_VALUE.toLong()) Int
) : Cloneable {
    @TruffleBoundary(allowInlining = true)
    fun asSectionOf(src: Source): SourceSection =
        src.createSection(start, len)

    @TruffleBoundary(allowInlining = true)
    fun asSubsectionOf(srcSec: SourceSection): SourceSection =
        asSectionOf(srcSec.source)

    override fun clone(): SourceSpan = copy()

    val end: @Range(from = 0, to = Int.MAX_VALUE.toLong()) Int get() = start + len - 1

    constructor(ir: IntRange) : this(ir.start, ir.endInclusive - ir.start + 1)

    companion object {
        @JvmStatic
        @TruffleBoundary(allowInlining = true)
        fun Source.createSection(span: SourceSpan): SourceSection = span.asSectionOf(this)

        @JvmStatic
        @TruffleBoundary(allowInlining = true)
        fun ParserRuleContext.sourceSpan(): SourceSpan {
            val startIx: Int = getStart().startIndex
            return SourceSpan(
                startIx,
                getStop().stopIndex - startIx + 1
            )
        }
    }
}
