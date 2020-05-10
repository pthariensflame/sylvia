package com.pthariensflame.sylvia.shell

import org.graalvm.launcher.AbstractLanguageLauncher
import org.graalvm.options.OptionCategory
import org.graalvm.polyglot.Context
import org.jetbrains.annotations.Contract
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder

class SylviaLauncher
@JvmOverloads constructor(
    @JvmField val term: Terminal = TerminalBuilder.terminal()
) : AbstractLanguageLauncher() {
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
        try {
            val lineReader: LineReader = LineReaderBuilder.builder()
                .appName("sylvia")
                .terminal(term)
                .parser(TODO())
                .completer(TODO())
                .highlighter(TODO())
                .variables(TODO())
                .expander(TODO())
                .history(TODO())
                .option(LineReader.Option.AUTO_FRESH_LINE, true)
                .option(LineReader.Option.DELAY_LINE_WRAP, true)
                .build()
            val ctx: Context = contextBuilder.build()
            lineReader.readLine("")
        } finally {
            term.close()
        }
    }

    override fun validateArguments(polyglotOptions: Map<String, String>) {
        TODO("Not yet implemented")
    }

    override fun getDefaultLanguages(): Array<String> =
        arrayOf("sylvia", "nfi")
}
