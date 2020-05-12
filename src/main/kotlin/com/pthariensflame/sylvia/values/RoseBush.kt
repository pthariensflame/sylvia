package com.pthariensflame.sylvia.values

sealed class RoseBush<Elem, Gap> {
    private data class Leaf<Elem, Gap>(
        @JvmField val value: Elem,
    ): RoseBush<Elem, Gap>()

    private data class Nested<Elem, Gap>(
        @JvmField val children: List<RoseBush<Elem, Gap>>,
        @JvmField val gaps: List<Gap>,
    ): RoseBush<Elem, Gap>()
}
