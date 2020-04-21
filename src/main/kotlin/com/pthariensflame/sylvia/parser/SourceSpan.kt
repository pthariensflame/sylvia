package com.pthariensflame.sylvia.parser

import com.oracle.truffle.api.CompilerDirectives

@CompilerDirectives.ValueType
data class SourceSpan(val start: Long, val len: Long)
