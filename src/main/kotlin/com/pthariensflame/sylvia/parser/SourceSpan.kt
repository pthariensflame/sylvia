package com.pthariensflame.sylvia.parser

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.source.Source
import com.oracle.truffle.api.source.SourceSection

@CompilerDirectives.ValueType
data class SourceSpan(@JvmField val start: Int, @JvmField val len: Int) {
    @CompilerDirectives.TruffleBoundary(allowInlining = true)
    fun asSectionOf(src: Source) =
        src.createSection(start, len)

    @CompilerDirectives.TruffleBoundary(allowInlining = true)
    fun asSubsectionOf(srcSec: SourceSection) =
        asSectionOf(srcSec.source)
}

@CompilerDirectives.TruffleBoundary(allowInlining = true)
fun Source.createSection(span: SourceSpan) = span.asSectionOf(this)
