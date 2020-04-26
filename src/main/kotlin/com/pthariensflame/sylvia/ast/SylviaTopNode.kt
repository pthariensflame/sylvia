package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.dsl.GenerateNodeFactory
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.frame.FrameDescriptor
import com.oracle.truffle.api.instrumentation.GenerateWrapper
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.nodes.RootNode
import com.pthariensflame.sylvia.SylviaLanguage

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
) : RootNode(langInstance, frameDescriptor), SylviaNode
