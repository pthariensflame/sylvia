package com.pthariensflame.sylvia.values.types

import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.library.ExportLibrary
import com.pthariensflame.sylvia.values.SylviaVal

@ExportLibrary(InteropLibrary::class)
abstract class SylviaType : SylviaVal()
