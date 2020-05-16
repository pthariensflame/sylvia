package com.pthariensflame.sylvia.ast.declarations

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
import com.pthariensflame.sylvia.ast.Identifier
import com.pthariensflame.sylvia.ast.SylviaNode
import com.pthariensflame.sylvia.ast.statements.StatementNode
import com.pthariensflame.sylvia.parser.SourceSpan
import org.jetbrains.annotations.Contract
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@NodeInfo(
    shortName = "decl",
    description = "A declaration"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
@OptIn(ExperimentalContracts::class)
abstract class DeclarationNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null,
) : StatementNode(srcSpan), SylviaNode {
    @ExportLibrary.Repeat(
        ExportLibrary(InteropLibrary::class),
//    ExportLibrary(LSPLibrary::class),
    )
    enum class DeclarationVisibility : TruffleObject {
        Visible {
            override val isVisible: Boolean = true
        },
        Hidden {
            override val isVisible: Boolean = false
        };

        @get:Contract(pure = true)
        abstract val isVisible: Boolean

        @get:Contract(pure = true)
        inline val isHidden: Boolean
            get() = !isVisible

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
                "isVisible",
                "isHidden",
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
        fun readMember(member: String): Boolean = when (member) {
            "isVisible" -> isVisible
            "isHidden" -> isHidden
            else -> throw UnknownIdentifierException.create(member)
        }
    }

    @field:CompilationFinal(dimensions = 1)
    var enclosingNamedScope: Array<Identifier> = emptyArray()

    @field:CompilationFinal
    var visibility: DeclarationVisibility = DeclarationVisibility.Hidden

    @field:CompilationFinal(dimensions = 1)
    var documentation: Array<String> = emptyArray()

    abstract override fun isInstrumentable(): Boolean

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        DeclarationNodeWrapper(this, probe)

    abstract override fun executeVoid(frame: VirtualFrame)

    // outConv handled in StatementNode
}
