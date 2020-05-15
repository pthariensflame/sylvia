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

interface RoseBushVisitor<in Elem, in Gap> {
    @JvmDefault
    fun visitEmpty(): Unit = Unit

    @JvmDefault
    fun visitLeaf(elem: Elem): Unit = Unit

    @JvmDefault
    fun visitGap(gap: Gap): Unit = Unit

    @JvmDefault
    fun enterNested(): Unit = Unit

    @JvmDefault
    fun exitNested(): Unit = Unit
}

@Suppress("NOTHING_TO_INLINE")
inline fun <Elem, Gap, Val> RoseBushValueVisitor<Elem, Gap, Val>.visit(
    tree: RoseBush<Elem, Gap>,
): Val = tree.acceptVisitor(this)

@Suppress("NOTHING_TO_INLINE")
inline fun <Elem, Gap> RoseBushValueVisitor<Elem, Gap, Unit>.asVisitor(): RoseBushVisitor<Elem, Gap> =
    object : RoseBushVisitor<Elem, Gap> {
        override fun enterNested() {
            this@asVisitor.enterNested()
        }

        override fun exitNested() {
            this@asVisitor.exitNested()
        }

        override fun visitEmpty() {
            this@asVisitor.visitEmpty()
        }

        override fun visitGap(gap: Gap) {
            this@asVisitor.visitGap(gap)
        }

        override fun visitLeaf(elem: Elem) {
            this@asVisitor.visitLeaf(elem)
        }
    }

@Suppress("NOTHING_TO_INLINE")
inline fun <Elem, Gap, Val> RoseBushValueVisitor<Elem, Gap, Val>.asValueVisitor(): RoseBushValueVisitor<Elem, Gap, Val> =
    this

@Suppress("NOTHING_TO_INLINE")
inline fun <Elem, Gap> RoseBushVisitor<Elem, Gap>.visit(
    tree: RoseBush<Elem, Gap>,
): Unit = tree.acceptVisitor(this)

@Suppress("NOTHING_TO_INLINE")
inline fun <Elem, Gap> RoseBushVisitor<Elem, Gap>.asValueVisitor(): RoseBushValueVisitor<Elem, Gap, Unit> =
    object : RoseBushValueVisitor<Elem, Gap, Unit> {
        override fun visitEmpty() {
            this@asValueVisitor.visitEmpty()
        }

        override fun visitLeaf(elem: Elem) {
            this@asValueVisitor.visitLeaf(elem)
        }

        override fun visitGap(gap: Gap) {
            this@asValueVisitor.visitGap(gap)
        }

        override fun enterNested() {
            this@asValueVisitor.enterNested()
        }

        override fun exitNested() {
            this@asValueVisitor.exitNested()
        }

        override fun concatValues(values: Sequence<Unit>): Unit = Unit
    }

@Suppress("NOTHING_TO_INLINE")
inline fun <Elem, Gap> RoseBushVisitor<Elem, Gap>.asVisitor(): RoseBushVisitor<Elem, Gap> =
    this
