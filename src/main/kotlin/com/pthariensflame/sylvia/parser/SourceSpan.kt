package com.pthariensflame.sylvia.parser

import com.oracle.truffle.api.CompilerDirectives

@CompilerDirectives.ValueType
data class SourceSpan(@JvmField val start: Int, @JvmField val len: Int)
