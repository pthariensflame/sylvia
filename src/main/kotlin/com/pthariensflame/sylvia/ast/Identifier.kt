package com.pthariensflame.sylvia.ast

import org.jetbrains.annotations.Contract

inline class Identifier(
    @JvmField val text: String,
) : Cloneable {
    @Contract("_ -> new", pure = true)
    override fun clone(): Identifier = Identifier(text)
}
