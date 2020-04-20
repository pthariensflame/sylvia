package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.TruffleRuntime

internal val truffleRuntime: TruffleRuntime
    get() = Truffle.getRuntime()

