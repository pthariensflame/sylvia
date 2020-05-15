package com.pthariensflame.sylvia.parser

import com.pthariensflame.sylvia.util.LazyConstant
import org.jetbrains.annotations.Contract
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

private val commentCheckRegex: Regex by LazyConstant {
    Regex(
        """^#([^\p{Space}()⦅⦆]*)(?:\(.*\)|⦅.*⦆)([^\p{Space}()⦅⦆]*)#$""",
        setOf(RegexOption.CANON_EQ, RegexOption.DOT_MATCHES_ALL)
    )
}

@Contract(pure = true)
@OptIn(ExperimentalContracts::class)
fun checkMatchedComment(txt: CharSequence): Boolean {
    contract {
        returns()
    }
    return commentCheckRegex.matchEntire(txt).run {
        if (this != null) {
            groups[1] == groups[2]
        } else false
    }
}
