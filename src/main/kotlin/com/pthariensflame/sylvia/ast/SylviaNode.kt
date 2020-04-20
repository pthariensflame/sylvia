package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.nodes.NodeInterface

@NodeInfo(
    language = "Sylvia",
    description = "An arbitrary node for the Sylvia language"
)
interface SylviaNode : NodeInterface