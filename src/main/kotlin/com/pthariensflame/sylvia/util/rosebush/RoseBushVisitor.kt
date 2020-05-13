package com.pthariensflame.sylvia.util.rosebush

interface RoseBushValueVisitor<in Elem, in Gap, Val> {
    @JvmDefault
    fun visitEmpty(): Val

    @JvmDefault
    fun visitLeaf(elem: Elem): Val

    @JvmDefault
    fun visitGap(gap: Gap): Val

    @JvmDefault
    fun enterNested(): Val

    @JvmDefault
    fun exitNested(): Val

    @JvmDefault
    fun concatValues(values: Sequence<Val>): Val
}

interface RoseBushVisitor<in Elem, in Gap> : RoseBushValueVisitor<Elem, Gap, Unit> {
    @JvmDefault
    override fun visitEmpty(): Unit = Unit

    @JvmDefault
    override fun visitLeaf(elem: Elem): Unit = Unit

    @JvmDefault
    override fun visitGap(gap: Gap): Unit = Unit

    @JvmDefault
    override fun enterNested(): Unit = Unit

    @JvmDefault
    override fun exitNested(): Unit = Unit

    @JvmDefault
    override fun concatValues(values: Sequence<Unit>): Unit = Unit
}

@Suppress("NOTHING_TO_INLINE")
inline fun <Elem, Gap, Val> RoseBushValueVisitor<Elem, Gap, Val>.visit(
    tree: RoseBush<Elem, Gap>,
): Val = tree.acceptVisitor(this)