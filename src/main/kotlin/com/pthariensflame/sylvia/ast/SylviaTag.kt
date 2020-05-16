package com.pthariensflame.sylvia.ast

import com.oracle.truffle.api.instrumentation.Tag

sealed class SylviaTag : Tag() {
    @Identifier("TYPE_EXPRESSION")
    class TypeExpressionTag private constructor() : SylviaTag()

    @Identifier("NATURE_EXPRESSION")
    class NatureExpressionTag private constructor() : SylviaTag()

    @Identifier("EFFECT_EXPRESSION")
    class EffectExpressionTag private constructor() : SylviaTag()
}
