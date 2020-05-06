package com.pthariensflame.sylvia

import com.oracle.truffle.api.TruffleLanguage
import com.oracle.truffle.api.instrumentation.ProvidedTags
import com.oracle.truffle.api.instrumentation.StandardTags
import com.pthariensflame.sylvia.ast.SylviaTag
import com.pthariensflame.sylvia.ast.declarations.DeclarationNode
import com.pthariensflame.sylvia.shell.SylviaFileDetector
import com.pthariensflame.sylvia.util.EquivStyle
import com.pthariensflame.sylvia.util.LeanMutableMap
import com.pthariensflame.sylvia.values.SylviaVal

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
    dependentLanguages = ["nfi"],
    contextPolicy = TruffleLanguage.ContextPolicy.EXCLUSIVE,
    services = [],
    fileTypeDetectors = [SylviaFileDetector::class],
)
@ProvidedTags(
    StandardTags.RootTag::class,
    StandardTags.RootBodyTag::class,
    StandardTags.StatementTag::class,
    StandardTags.ExpressionTag::class,
    StandardTags.CallTag::class,
//    StandardTags.ReadVariableTag::class,
//    StandardTags.WriteVariableTag::class,
//    StandardTags.TryBlockTag::class,
//    DebuggerTags.AlwaysHalt::class,
    SylviaTag.TypeExpressionTag::class,
)
class SylviaLanguage : TruffleLanguage<SylviaLanguage.SylviaLangCxt>() {
    data class SylviaLangCxt
    @JvmOverloads constructor(
        val env: Env,
        val globalScope: LeanMutableMap<String, DeclarationNode> =
            LeanMutableMap.create(EquivStyle.objectMethodsAll()),
    )

    override fun createContext(env: Env?): SylviaLangCxt? = env?.let { SylviaLangCxt(it) }

    @Throws(Exception::class)
    override fun initializeContext(context: SylviaLangCxt) {
        super.initializeContext(context)
    }

    @Throws(Exception::class)
    override fun finalizeContext(context: SylviaLangCxt) {
        super.finalizeContext(context)
    }

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
            is SylviaVal,
            ->
                true
            else ->
                false
        }
}
