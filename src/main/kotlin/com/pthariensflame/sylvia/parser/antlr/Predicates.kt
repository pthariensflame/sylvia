package com.pthariensflame.sylvia.parser.antlr

import com.pthariensflame.sylvia.util.assertPartialEvaluationConstant
import org.jetbrains.annotations.Contract
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


private val commentCheckRegex: Regex by lazy {
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
    return commentCheckRegex.assertPartialEvaluationConstant().matchEntire(txt)?.run {
        groups[1] == groups[2]
    } ?: false
}