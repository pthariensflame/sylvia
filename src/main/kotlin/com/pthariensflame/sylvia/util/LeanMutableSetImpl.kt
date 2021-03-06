package com.pthariensflame.sylvia.util

import org.graalvm.collections.EconomicSet
import org.graalvm.collections.UnmodifiableEconomicSet
import org.jetbrains.annotations.Contract

inline class LeanMutableSetImpl<E : Any>(
    @get:Contract(pure = true)
    override val underlying: EconomicSet<E> = EconomicSet.create()
) : LeanMutableSet<E> {
    constructor(
        equivStyle: EquivStyle<E>,
        capacity: Int,
    ) : this(EconomicSet.create(equivStyle.underlying, capacity))

    constructor(
        equivStyle: EquivStyle<E>,
    ) : this(EconomicSet.create(equivStyle.underlying))

    constructor(
        capacity: Int,
    ) : this(EconomicSet.create(capacity))

    constructor(other: LeanSet<E>) :
            this(EconomicSet.create(other.underlying))

    constructor(equivStyle: EquivStyle<E>, other: LeanSet<E>) :
            this(EconomicSet.create(equivStyle.underlying, other.underlying))
}
