package com.pthariensflame.sylvia

import com.oracle.truffle.api.Truffle
import com.oracle.truffle.api.TruffleLanguage
import com.oracle.truffle.api.TruffleRuntime
import com.oracle.truffle.api.instrumentation.ProvidedTags
import com.oracle.truffle.api.instrumentation.StandardTags
import com.pthariensflame.sylvia.ast.ProcedureNode
import com.pthariensflame.sylvia.shell.SylviaFileDetector
import com.pthariensflame.sylvia.values.SylviaVal
import org.graalvm.collections.EconomicSet
import org.graalvm.collections.Equivalence

internal val truffleRuntime: TruffleRuntime
    inline get() = Truffle.getRuntime()

@TruffleLanguage.Registration(
    id = "sylvia",
    name = "Sylvia",
    implementationName = "Reference Truffle Sylvia",
    version = "0.0.1",
    characterMimeTypes = ["text/x-sylvia"],
    byteMimeTypes = [],
    defaultMimeType = "text/x-sylvia",
    interactive = true,
    internal = false,
    dependentLanguages = [],
    contextPolicy = TruffleLanguage.ContextPolicy.EXCLUSIVE,
    services = [],
    fileTypeDetectors = [SylviaFileDetector::class],
)
@ProvidedTags(
    StandardTags.CallTag::class,
    StandardTags.ExpressionTag::class,
    StandardTags.ReadVariableTag::class,
    StandardTags.RootTag::class,
    StandardTags.RootBodyTag::class,
    StandardTags.StatementTag::class,
    StandardTags.WriteVariableTag::class,
//        StandardTags.TryBlockTag::class,
//        DebuggerTags.AlwaysHalt::class,
)
final class SylviaLanguage : TruffleLanguage<SylviaLanguage.SylviaLangCxt>() {

    data class SylviaLangCxt
    @JvmOverloads constructor(
        val env: Env,
        val procedures: EconomicSet<ProcedureNode> =
            EconomicSet.create(Equivalence.DEFAULT),
    )

    override fun createContext(env: Env?): SylviaLangCxt? = env?.let { SylviaLangCxt(it) }

    override fun isObjectOfLanguage(obj: Any?): Boolean =
        when (obj) {
            is Boolean,
            is Byte,
            is Short,
            is Int,
            is Long,
            is Float,
            is Double,
            is String,
            is SylviaVal ->
                true
            else ->
                false
        }
}
