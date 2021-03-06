package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.dsl.ReportPolymorphism
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.nodes.NodeInterface
import com.oracle.truffle.api.source.SourceSection
import com.pthariensflame.sylvia.parser.SourceSpan
import org.jetbrains.annotations.Contract

@NodeInfo(
    language = "Sylvia",
    shortName = "sylv",
    description = "An arbitrary node for the Sylvia language"
)
@ReportPolymorphism
@GenerateUncached(inherit = true)
@Introspectable
interface SylviaNode : NodeInterface {
    @get:Contract(pure = true)
    val srcSpan: SourceSpan?

    @TruffleBoundary
    fun getSourceSection(): SourceSection?
}
