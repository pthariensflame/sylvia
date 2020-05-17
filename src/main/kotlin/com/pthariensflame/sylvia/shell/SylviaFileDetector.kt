package com.pthariensflame.sylvia.shell

import com.oracle.truffle.api.TruffleFile
import com.oracle.truffle.api.TruffleFile.FileTypeDetector
import org.intellij.lang.annotations.MagicConstant
import org.jetbrains.annotations.Contract
import java.io.IOException
import java.nio.charset.Charset

class SylviaFileDetector : FileTypeDetector {
    companion object {
        const val SYLVIA_MIME_TYPE: String = "text/x-sylvia"
    }

    @Throws(IOException::class)
    @Contract(pure = true)
    @MagicConstant(stringValues = [SYLVIA_MIME_TYPE])
    override fun findMimeType(file: TruffleFile): String? = file.name
        .takeIf { it.endsWith(".sylvia", true) }
        ?.let { SYLVIA_MIME_TYPE }

    @Throws(IOException::class)
    @Contract(pure = true)
    override fun findEncoding(@Suppress("UNUSED_PARAMETER") file: TruffleFile): Charset =
        Charsets.UTF_8
}
