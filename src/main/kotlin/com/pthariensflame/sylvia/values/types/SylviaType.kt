package com.pthariensflame.sylvia.values.types

import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.library.ExportLibrary
import com.pthariensflame.sylvia.values.SylviaVal
import org.graalvm.tools.api.lsp.LSPLibrary

@ExportLibrary.Repeat(
    ExportLibrary(InteropLibrary::class),
//    ExportLibrary(LSPLibrary::class),
)
abstract class SylviaType : SylviaVal()
