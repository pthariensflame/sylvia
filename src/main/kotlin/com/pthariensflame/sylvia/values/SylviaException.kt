package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.TruffleException
import com.oracle.truffle.api.nodes.Node
import com.oracle.truffle.api.source.SourceSection
import org.jetbrains.annotations.Contract

abstract class SylviaException internal constructor() : RuntimeException(), TruffleException {
    @Contract("-> this", pure = true)
    abstract override fun fillInStackTrace(): SylviaException

    abstract override fun getLocation(): Node?

    override fun getSourceLocation(): SourceSection? =
        super<TruffleException>.getSourceLocation() ?: location?.sourceSection?.source?.createUnavailableSection()
}
