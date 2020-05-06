package com.pthariensflame.sylvia.parser.antlr

import com.pthariensflame.sylvia.util.assertCompilationConstant
import com.pthariensflame.sylvia.util.assertPartialEvaluationConstant
import org.jetbrains.annotations.Contract
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@PublishedApi
internal object PredicatesImpl {
    @JvmStatic
    @PublishedApi
    internal val commentCheckRegex: Regex by lazy {
        Regex(
            """^#([^\p{Space}()⦅⦆]*)(?:\(.*\)|⦅.*⦆)([^\p{Space}()⦅⦆]*)#$""",
            setOf(RegexOption.CANON_EQ, RegexOption.DOT_MATCHES_ALL)
        )
    }
}

inline val commentCheckRegex: Regex
    get() = PredicatesImpl.commentCheckRegex.assertPartialEvaluationConstant()


@Contract(pure = true)
@OptIn(ExperimentalContracts::class)
fun checkMatchedComment(txt: CharSequence): Boolean {
    contract {
        returns()
    }
    return commentCheckRegex.matchEntire(txt)?.run {
        groups[1] == groups[2]
    } ?: false
}