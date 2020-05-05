package com.pthariensflame.sylvia.shell

import com.oracle.truffle.api.TruffleFile
import com.oracle.truffle.api.TruffleFile.FileTypeDetector
import org.jetbrains.annotations.Contract
import java.io.IOException
import java.nio.charset.Charset

class SylviaFileDetector : FileTypeDetector {
    @Throws(IOException::class)
    @Contract(pure = true)
    override fun findMimeType(file: TruffleFile): String? =
        if (file.name.endsWith(".sylvia", true)) {
            "text/x-sylvia"
        } else {
            null
        }

    @Throws(IOException::class)
    @Contract(pure = true)
    override fun findEncoding(@Suppress("UNUSED_PARAMETER") file: TruffleFile): Charset =
        Charsets.UTF_8
}
