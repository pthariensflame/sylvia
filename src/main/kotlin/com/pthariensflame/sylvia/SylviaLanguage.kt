package com.pthariensflame.sylvia

import com.oracle.truffle.api.TruffleLanguage
import com.oracle.truffle.api.dsl.GenerateUncached
import com.oracle.truffle.api.instrumentation.ProvidedTags
import com.oracle.truffle.api.instrumentation.StandardTags
import com.pthariensflame.sylvia.ast.ProcedureNode
import com.pthariensflame.sylvia.values.SylviaVal
import org.graalvm.collections.EconomicSet
import org.graalvm.collections.Equivalence

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
        fileTypeDetectors = [],
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
@GenerateUncached(inherit = true)
final class SylviaLanguage : TruffleLanguage<SylviaLanguage.SylviaLangCxt>() {

    data class SylviaLangCxt
    @JvmOverloads constructor(
            val env: Env,
            val procedures: EconomicSet<ProcedureNode> =
                    EconomicSet.create(Equivalence.DEFAULT),
                             )

    override fun createContext(env: Env?): SylviaLangCxt? = env?.let { SylviaLangCxt(it) }

    override fun isObjectOfLanguage(obj: Any?): Boolean =
            obj is Int || obj is Long || obj is Double || obj is String || obj is SylviaVal
}
