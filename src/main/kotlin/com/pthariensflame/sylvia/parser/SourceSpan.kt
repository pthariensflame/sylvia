package com.pthariensflame.sylvia.parser

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary
import com.oracle.truffle.api.CompilerDirectives.ValueType
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.TruffleObject
import com.oracle.truffle.api.interop.UnknownIdentifierException
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import com.oracle.truffle.api.source.Source
import com.oracle.truffle.api.source.SourceSection
import org.antlr.v4.runtime.ParserRuleContext
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Range
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExportLibrary.Repeat(
    ExportLibrary(InteropLibrary::class),
//    ExportLibrary(LSPLibrary::class),
)
@ValueType
@OptIn(ExperimentalContracts::class)
data class SourceSpan(
    @JvmField val start: @Range(from = 0, to = Int.MAX_VALUE.toLong()) Int,
    @JvmField val len: @Range(from = 0, to = Int.MAX_VALUE.toLong()) Int,
) : Cloneable, TruffleObject {
    @TruffleBoundary(allowInlining = true)
    fun asSectionOf(src: Source): SourceSection =
        src.createSection(start, len)

    @TruffleBoundary(allowInlining = true)
    fun asSubsectionOf(srcSec: SourceSection): SourceSection =
        asSectionOf(srcSec.source)

    override fun clone(): SourceSpan = copy()

    val end: @Range(from = 0, to = Int.MAX_VALUE.toLong()) Int get() = start + len - 1

    constructor(ir: IntRange) : this(ir.start, ir.endInclusive - ir.start + 1)

    @ExportMessage
    @Contract("-> true", pure = true)
    fun hasMembers(): Boolean {
        contract {
            returns(true)
        }
        return true
    }

    @ExportMessage
    @Throws(UnsupportedMessageException::class)
    fun getMembers(@Suppress("UNUSED_PARAMETER") includeInternal: Boolean): Array<String> =
        arrayOf(
            "start",
            "len",
            "end"
        )

    @ExportMessage
    fun isMemberReadable(member: String): Boolean {
        return member in getMembers(false)
    }

    @ExportMessage
    @Throws(
        UnsupportedMessageException::class,
        UnknownIdentifierException::class,
    )
    fun readMember(member: String): Int = when (member) {
        "start" -> start
        "len" -> len
        "end" -> end
        else -> throw UnknownIdentifierException.create(member)
    }

    companion object {
        @Suppress("NOTHING_TO_INLINE")
        @JvmStatic
        @TruffleBoundary(allowInlining = true)
        inline fun Source.createSection(span: SourceSpan): SourceSection =
            span.asSectionOf(this)

        @Suppress("NOTHING_TO_INLINE")
        @JvmStatic
        @TruffleBoundary(allowInlining = true)
        inline fun SourceSection.createSubsection(span: SourceSpan): SourceSection =
            span.asSubsectionOf(this)

        @Suppress("NOTHING_TO_INLINE")
        @JvmStatic
        @TruffleBoundary(allowInlining = true)
        inline fun SourceSection.createUnavailableSubsection(): SourceSection =
            source.createUnavailableSection()

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
