package com.pthariensflame.sylvia.shell

import com.oracle.truffle.api.interop.InteropException
import com.oracle.truffle.api.nodes.ExplodeLoop
import com.pthariensflame.sylvia.util.useAll
import org.graalvm.launcher.AbstractLanguageLauncher
import org.graalvm.launcher.Launcher
import org.graalvm.options.OptionCategory
import org.graalvm.polyglot.Context
import org.jetbrains.annotations.Contract
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import java.io.Closeable

class SylviaLauncher
@JvmOverloads constructor(
    @JvmField val term: Terminal = TerminalBuilder.terminal()
) : AbstractLanguageLauncher() {
    val outWriter = term.output().bufferedWriter()
    val logWriter = logFile?.let { Launcher.newLogStream(it).bufferedWriter() }

    companion object {
        @JvmStatic
        @ExplodeLoop
        fun main(args: Array<String>): Unit =
            SylviaLauncher().run {
                (sequenceOf<Closeable>(term, outWriter) + (logWriter?.let { sequenceOf(logWriter) }
                    ?: emptySequence())).asIterable().useAll { launch(args) }
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
        val lineReader: LineReader = LineReaderBuilder.builder()
            .appName("sylvia")
            .terminal(term)
            .option(LineReader.Option.AUTO_FRESH_LINE, true)
            .option(LineReader.Option.DELAY_LINE_WRAP, true)
//            .parser(TODO())
//            .completer(TODO())
//            .highlighter(TODO())
//            .expander(TODO())
//            .history(TODO())
//            .variables(TODO())
            .build()
        lineReader.history.load()
        val ctx: Context = contextBuilder.build()
        val prompt = "sylvia> "
        do {
            var line: String = ""
            try {
                logWriter?.write("sylvia> ")
                line = lineReader.readLine(prompt)
                logWriter?.write(line)
                logWriter?.newLine()
                val res = ctx.eval("sylvia", line).toString()
                outWriter.write(res)
                outWriter.newLine()
                logWriter?.write(res)
                logWriter?.newLine()
            } catch (e: EndOfFileException) {
                break
            } catch (e: UserInterruptException) {
                continue
            } catch (e: InteropException) {
                outWriter.write(e.localizedMessage)
                logWriter?.write(e.localizedMessage)
            }
        } while (line != "!quit")
        lineReader.history.save()
    }

    override fun validateArguments(polyglotOptions: Map<String, String>) {
        TODO("Not yet implemented")
    }

    override fun getDefaultLanguages(): Array<String> =
        arrayOf("sylvia", "nfi")
}
