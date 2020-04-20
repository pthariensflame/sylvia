package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.TruffleRuntime
import com.oracle.truffle.api.nodes.NodeInfo
import com.oracle.truffle.api.nodes.NodeInterface

val truffleRuntime: TruffleRuntime
    get() = Truffle.getRuntime()

@NodeInfo(
    language = "Sylvia",
    description = "An arbitrary node for the Sylvia language"
)
interface SylviaNode : NodeInterface
