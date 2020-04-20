package com.pthariensflame.sylvia

import com.oracle.truffle.api.TruffleLanguage
import com.oracle.truffle.api.debug.DebuggerTags
import com.oracle.truffle.api.instrumentation.ProvidedTags
import com.oracle.truffle.api.instrumentation.StandardTags

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
    //StandardTags.TryBlockTag::class,
    //DebuggerTags.AlwaysHalt::class,
)
final class SylviaLanguage : TruffleLanguage<SylviaLanguage.SylviaLangCxt>() {

    final class SylviaLangCxt(val env: Env)

    override fun createContext(env: Env?): SylviaLangCxt? = env?.let { SylviaLangCxt(it) }

    override fun isObjectOfLanguage(`object`: Any?): Boolean {
        TODO("Not yet implemented")
    }
}
