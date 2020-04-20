package com.pthariensflame.sylvia.values

import com.oracle.truffle.api.interop.InteropLibrary
import com.oracle.truffle.api.interop.TruffleObject
import com.oracle.truffle.api.library.ExportLibrary

@ExportLibrary(InteropLibrary::class)
abstract class SylviaVal internal constructor() : TruffleObject
