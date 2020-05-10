package com.pthariensflame.sylvia

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal
import com.oracle.truffle.api.Scope
import com.oracle.truffle.api.TruffleLanguage
import com.oracle.truffle.api.`object`.DynamicObject
import com.oracle.truffle.api.`object`.Layout
import com.oracle.truffle.api.`object`.Shape
import com.oracle.truffle.api.instrumentation.ProvidedTags
import com.oracle.truffle.api.instrumentation.StandardTags
import com.pthariensflame.sylvia.ast.SylviaTag
import com.pthariensflame.sylvia.shell.SylviaFileDetector
import com.pthariensflame.sylvia.util.LazyConstant
import com.pthariensflame.sylvia.values.SylviaException
import com.pthariensflame.sylvia.values.SylviaVal
import org.graalvm.options.OptionValues


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
@OptIn(ExperimentalStdlibApi::class)
class SylviaLanguage : TruffleLanguage<SylviaLanguage.SylviaLangCxt>() {
    companion object {
        @JvmStatic
        val scopeLayout: Layout by LazyConstant {
            Layout.newLayout()
                .addAllowedImplicitCast(Layout.ImplicitCast.IntToLong)
                .setPolymorphicUnboxing(true)
                .build()
        }

        @JvmStatic
        val scopeShape: Shape by LazyConstant {
            scopeLayout.createShape(SylviaScopeObjectType())
        }

        @JvmStatic
        fun newScopeObject(): DynamicObject =
            scopeLayout.newInstance(scopeShape)
    }

    class SylviaLangCxt(
        @JvmField @CompilationFinal var env: Env,
    ) {

        @JvmField
        @CompilationFinal
        var primitivesResolver: PrimitivesResolver =
            PrimitivesResolver.DefaultPrimitivesResolver

        @JvmField
        @CompilationFinal
        var globalScope: Scope = run {
            Scope.newBuilder(
                "Sylvia Top Scope",
                newScopeObject()
            ).node(
                null
            ).arguments(
                null
            ).rootInstance(
                null
            ).build()
        }
    }

    override fun createContext(env: Env?): SylviaLangCxt? = env?.let {
        SylviaLangCxt(it)
    }

    @Throws(Exception::class)
    override fun initializeContext(context: SylviaLangCxt) = context.run {
        super.initializeContext(this)
        // TODO
    }

    @Throws(Exception::class)
    override fun finalizeContext(context: SylviaLangCxt) = context.run {
        // TODO
        super.finalizeContext(this)
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
            is UnicodeCodepoint,
            is String,
            is SylviaVal,
            is SylviaException,
            -> true

            else
            -> false
        }

    override fun areOptionsCompatible(firstOptions: OptionValues, newOptions: OptionValues): Boolean {
        return true // TODO
    }

    override fun findTopScopes(context: SylviaLangCxt): Iterable<Scope> {
        return arrayListOf(context.globalScope)
    }
}
