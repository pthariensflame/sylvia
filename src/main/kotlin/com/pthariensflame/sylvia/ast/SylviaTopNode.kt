package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary
import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.frame.FrameDescriptor
import com.oracle.truffle.api.instrumentation.*
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.nodes.RootNode
import com.oracle.truffle.api.source.Source
import com.oracle.truffle.api.source.SourceSection
import com.pthariensflame.sylvia.SylviaLanguage
import com.pthariensflame.sylvia.parser.SourceSpan
import com.pthariensflame.sylvia.parser.SourceSpan.Companion.createSection
import org.jetbrains.annotations.Contract

@NodeInfo(
    shortName = "⊤-sylv",
    description = "A top-level node for the Sylvia language"
)
@GenerateNodeFactory
@GenerateWrapper
@GenerateUncached(inherit = true)
abstract class SylviaTopNode
@JvmOverloads internal constructor(
    langInstance: SylviaLanguage? = null,
    frameDescriptor: FrameDescriptor? = null,
    @get:TruffleBoundary @field:CompilationFinal open val originalSrc: Source? = null,
    @field:CompilationFinal override val srcSpan: SourceSpan? = null,
) : RootNode(langInstance, frameDescriptor), SylviaNode, InstrumentableNode {
    override fun isInstrumentable(): Boolean = true

    @Contract("-> new")
    override fun createWrapper(probe: ProbeNode): InstrumentableNode.WrapperNode =
        SylviaTopNodeWrapper(this, probe)

    override fun hasTag(tag: Class<out Tag>): Boolean =
        tag.kotlin == StandardTags.RootTag::class || super.hasTag(tag)

    @TruffleBoundary
    override fun getSourceSection(): SourceSection? =
        originalSrc?.run { srcSpan?.let { createSection(it) } ?: createUnavailableSection() }
}
