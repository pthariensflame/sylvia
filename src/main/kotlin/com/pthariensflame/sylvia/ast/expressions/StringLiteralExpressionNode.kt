package com.pthariensflame.sylvia.ast.expressions

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.GenerateWrapper
import com.oracle.truffle.api.instrumentation.InstrumentableNode
import com.oracle.truffle.api.instrumentation.ProbeNode
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.UnicodeCodepoint
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.values.StringVal
import com.pthariensflame.sylvia.values.SylviaVal

@NodeInfo(
    shortName = "str-lit",
    description = "A string literal expression"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
open class StringLiteralExpressionNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null
): LiteralExpressionNode() {
    enum class Kind {
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

        open val isDoubleQuotes: Boolean  = false

        open val isBackticks: Boolean = false

        open val isChevrons: Boolean = false

        abstract val openingDelimiter: UnicodeCodepoint;

        abstract val closingDelimiter: UnicodeCodepoint;
    }

    @field:CompilationFinal
    lateinit var kind: Kind

    @field:CompilationFinal
    lateinit var content: String

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        StringLiteralExpressionNodeWrapper(this, probe)

    override fun executeVal(frame: VirtualFrame): SylviaVal =
        StringVal(executeString(frame))

    override fun executeString(frame: VirtualFrame): String =
        content

    companion object {
        @JvmStatic
        fun processStringLiteralContent(text: String): String {

        }
    }
}
