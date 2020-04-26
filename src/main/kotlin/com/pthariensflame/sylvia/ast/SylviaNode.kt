package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.dsl.Introspectable
import com.oracle.truffle.api.dsl.ReportPolymorphism
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.nodes.NodeInterface

@NodeInfo(
    language = "Sylvia",
    shortName = "sylv",
    description = "An arbitrary node for the Sylvia language"
)
@ReportPolymorphism
@Introspectable
interface SylviaNode : NodeInterface
