package com.pthariensflame.sylvia.parser

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary
import com.oracle.truffle.api.CompilerDirectives.ValueType
import com.oracle.truffle.api.source.Source
import com.oracle.truffle.api.source.SourceSection
import org.antlr.v4.runtime.ParserRuleContext

@ValueType
data class SourceSpan(
    @JvmField val start: Int,
    @JvmField val len: Int
) : Cloneable {
    @TruffleBoundary(allowInlining = true)
    fun asSectionOf(src: Source): SourceSection =
        src.createSection(start, len)

    @TruffleBoundary(allowInlining = true)
    fun asSubsectionOf(srcSec: SourceSection): SourceSection =
        asSectionOf(srcSec.source)

    override fun clone(): SourceSpan = copy()
}

@TruffleBoundary(allowInlining = true)
fun Source.createSection(span: SourceSpan): SourceSection = span.asSectionOf(this)

@TruffleBoundary(allowInlining = true)
fun ParserRuleContext.sourceSpan(): SourceSpan {
    val startIx: Int = getStart().startIndex
    return SourceSpan(
        startIx,
        getStop().stopIndex - startIx + 1
    )
}
