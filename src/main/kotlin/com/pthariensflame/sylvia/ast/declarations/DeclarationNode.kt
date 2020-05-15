package com.pthariensflame.sylvia.ast.declarations

import com.oracle.truffle.api.CompilerDirectives
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.frame.VirtualFrame
import com.oracle.truffle.api.instrumentation.*
import com.oracle.truffle.api.nodes.NodeInfo
import com.pthariensflame.sylvia.ast.Identifier
import com.pthariensflame.sylvia.ast.SylviaNode
import com.pthariensflame.sylvia.ast.statements.StatementNode
import com.pthariensflame.sylvia.parser.SourceSpan
import org.jetbrains.annotations.Contract

@NodeInfo(
    shortName = "decl",
    description = "A declaration"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
@Introspectable
abstract class DeclarationNode
@JvmOverloads constructor(
    srcSpan: SourceSpan? = null,
) : StatementNode(srcSpan), SylviaNode {
    enum class Visibility {
        Visible {
            override val isVisible: Boolean = true
        },
        Hidden {
            override val isVisible: Boolean = false
        };

        @get:Contract(pure = true)
        abstract val isVisible: Boolean
    }

    @field:CompilationFinal(dimensions = 1)
    var enclosingNamespace: Array<Identifier> = emptyArray()

    @field:CompilationFinal
    var visibility: Visibility = Visibility.Hidden

    @field:CompilationFinal(dimensions = 1)
    var documentation: Array<String> = emptyArray()

    abstract override fun isInstrumentable(): Boolean

    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        DeclarationNodeWrapper(this, probe)

    abstract override fun executeVoid(frame: VirtualFrame)

    // outConv handled in StatementNode
}
