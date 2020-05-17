package com.pthariensflame.sylvia.ast.expressions.literals

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.GenerateWrapper
import com.oracle.truffle.api.instrumentation.InstrumentableNode
import com.oracle.truffle.api.instrumentation.ProbeNode
import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.TruffleObject
import com.oracle.truffle.api.interop.UnknownIdentifierException
import com.oracle.truffle.api.interop.UnsupportedMessageException
import com.oracle.truffle.api.library.ExportLibrary
import com.oracle.truffle.api.library.ExportMessage
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.UnicodeCodepoint
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.values.StringVal
import org.intellij.lang.annotations.MagicConstant
import org.jetbrains.annotations.Contract
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@NodeInfo(
    shortName = "str-lit",
    description = "A string literal expression"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
@OptIn(ExperimentalContracts::class)
open class StringLiteralExpressionNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null,
) : LiteralExpressionNode(srcSpan) {
    @ExportLibrary.Repeat(
        ExportLibrary(InteropLibrary::class),
//    ExportLibrary(LSPLibrary::class),
    )
    enum class StringLiteralDelimiterKind : TruffleObject {
        StraightSingleQuotes {
            override val isStraight: Boolean = true
            override val isSingleQuotes: Boolean = true
            override val openingDelimiter: UnicodeCodepoint = UnicodeCodepoint('\'')
            override val closingDelimiter: UnicodeCodepoint = UnicodeCodepoint('\'')
        },
        StraightDoubleQuotes {
            override val isStraight: Boolean = true
            override val isDoubleQuotes: Boolean = true
            override val openingDelimiter: UnicodeCodepoint = UnicodeCodepoint('"')
            override val closingDelimiter: UnicodeCodepoint = UnicodeCodepoint('"')
        },
        StraightBackticks {
            override val isStraight: Boolean = true
            override val isBackticks: Boolean = true
            override val openingDelimiter: UnicodeCodepoint = UnicodeCodepoint('`')
            override val closingDelimiter: UnicodeCodepoint = UnicodeCodepoint('`')
        },
        SmartSingleQuotes {
            override val isSmart: Boolean = true
            override val isSingleQuotes: Boolean = true
            override val openingDelimiter: UnicodeCodepoint = UnicodeCodepoint('‘')
            override val closingDelimiter: UnicodeCodepoint = UnicodeCodepoint('’')
        },
        SmartDoubleQuotes {
            override val isSmart: Boolean = true
            override val isDoubleQuotes: Boolean = true
            override val openingDelimiter: UnicodeCodepoint = UnicodeCodepoint('“')
            override val closingDelimiter: UnicodeCodepoint = UnicodeCodepoint('”')
        },
        SmartChevrons {
            override val isSmart: Boolean = true
            override val isChevrons: Boolean = true
            override val openingDelimiter: UnicodeCodepoint = UnicodeCodepoint('«')
            override val closingDelimiter: UnicodeCodepoint = UnicodeCodepoint('»')
        };

        open val isStraight: Boolean = false

        open val isSmart: Boolean = false

        open val isSingleQuotes: Boolean = false

        open val isDoubleQuotes: Boolean = false

        open val isBackticks: Boolean = false

        open val isChevrons: Boolean = false

        abstract val openingDelimiter: UnicodeCodepoint

        abstract val closingDelimiter: UnicodeCodepoint

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
                "isSmart",
                "isStraight",
                "isSingleQuotes",
                "isDoubleQuotes",
                "isBackticks",
                "isChevrons",
                "openingDelimeter",
                "closingDelimeter",
            )

        @ExportMessage
        fun isMemberReadable(
            @MagicConstant(
                stringValues = [
                    "isSmart",
                    "isStraight",
                    "isSingleQuotes",
                    "isDoubleQuotes",
                    "isBackticks",
                    "isChevrons",
                    "openingDelimeter",
                    "closingDelimeter",
                ]
            )
            member: String
        ): Boolean {
            return member in getMembers(false)
        }

        @ExportMessage
        @Throws(
            UnsupportedMessageException::class,
            UnknownIdentifierException::class,
        )
        fun readMember(
            @MagicConstant(
                stringValues = [
                    "isSmart",
                    "isStraight",
                    "isSingleQuotes",
                    "isDoubleQuotes",
                    "isBackticks",
                    "isChevrons",
                    "openingDelimeter",
                    "closingDelimeter",
                ]
            )
            member: String
        ): Any = when (member) {
            "isSmart" -> isSmart
            "isStraight" -> isStraight
            "isSingleQuotes" -> isSingleQuotes
            "isDoubleQuotes" -> isDoubleQuotes
            "isBackticks" -> isBackticks
            "isChevrons" -> isChevrons
            "openingDelimeter" -> openingDelimiter
            "closingDelimeter" -> closingDelimiter
            else -> throw UnknownIdentifierException.create(member)
        }
    }

    @field:CompilationFinal
    lateinit var delimiterKind: StringLiteralDelimiterKind

    @field:CompilationFinal
    lateinit var content: String

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        StringLiteralExpressionNodeWrapper(this, probe)

    override fun executeVal(frame: VirtualFrame): StringVal =
        StringVal(executeString(frame)).apply {
            originatingNode = this@StringLiteralExpressionNode
        }

    override fun executeString(frame: VirtualFrame): String =
        content

    companion object {
        @JvmStatic
        fun processStringLiteralContent(text: CharSequence): String {
            TODO()
        }
    }
}
