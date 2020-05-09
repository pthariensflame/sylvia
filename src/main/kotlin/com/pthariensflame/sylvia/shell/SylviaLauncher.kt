package com.pthariensflame.sylvia.shell

import org.graalvm.launcher.AbstractLanguageLauncher
import org.graalvm.options.OptionCategory
import org.graalvm.options.OptionDescriptor
import org.graalvm.polyglot.Context
import org.jetbrains.annotations.Contract

class SylviaLauncher : AbstractLanguageLauncher() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SylviaLauncher().launch(args)
        }
    }

    @Contract(pure = true)
    override fun getLanguageId(): String =
        "sylvia"

    override fun preprocessArguments(
        arguments: List<String>,
        polyglotOptions: Map<String, String>
    ): List<String> {
        TODO("Not yet implemented")
    }

    override fun printHelp(maxCategory: OptionCategory) {
        TODO("Not yet implemented")
    }

    override fun launch(contextBuilder: Context.Builder) {
        val ctx: Context = contextBuilder
            .allowAllAccess(true)
            .build()
        ctx.eval(
            languageId,
            TODO()
        )
    }

    override fun validateArguments(polyglotOptions: Map<String, String>) {
        TODO("Not yet implemented")
    }
}
