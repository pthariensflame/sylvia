grammar Sylvia;

@header {
    package com.pthariensflame.sylvia.parser.antlr;
    import java.util.ArrayDeque;
    import java.util.Deque;
    import org.jetbrains.annotations.NotNull;
    import org.jetbrains.annotations.Nullable;
    import org.jetbrains.annotations.Contract;
    import com.pthariensflame.sylvia.parser.SourceSpan;
}

@lexer::members {
    @NotNull Deque<String> lexicalCommentMarks = new ArrayDeque<>();
}

@parser::members {
    @Contract(value = "null -> false", pure = true)
    static boolean checkNotKeyword(@Nullable String idn) {
        return !(null == idn
            || idn.equals("proc")
            || idn.equals("get")
            || idn.equals("from")
            || idn.equals("convention")
            || idn.equals("effect")
            || idn.equals("typeAlias")
            || idn.equals("newType")
            || idn.equals("module")
            || idn.equals("doc")
            || idn.equals("export")
            || idn.equals("import")
            || idn.equals("true")
            || idn.equals("false")
            || idn.equals("of"));
    }
}

// Keywords and symbols

PROC : 'proc';
GET : 'get';
FROM : 'from';
CONVENTION : 'convention';
EFFECT : 'effect';
TYPEALIAS : 'typeAlias';
NEWTYPE : 'newType';
MODULE : 'module';
DOC : 'doc';
EXPORT : 'export';
IMPORT : 'import';
TRUE : 'true';
FALSE : 'false';
OF : 'of';
OPEN_PAREN : '(';
CLOSE_PAREN : ')';
COLON : ':';
AT_SYM : '@';
OPEN_BRACE : '{';
CLOSE_BRACE : '}';
OPEN_BRACK : '[';
CLOSE_BRACK : ']';
COMMA : ',';
SEMICOLON : ';';
UNDERSCORE : '_';
DOT : '.';
HASH_SYM : '#';

keyword : PROC
        | GET
        | FROM
        | CONVENTION
        | EFFECT
        | TYPEALIAS
        | NEWTYPE
        | MODULE
        | DOC
        | EXPORT
        | IMPORT
        | TRUE
        | FALSE
        | OF;

// identifiers

fragment IDENT_START : [\p{XID_START}];
fragment IDENT_CONT : IDENT_START | [\p{XID_CONTINUE}];
IDENT : IDENT_START IDENT_CONT*;
identifier : IDENT {checkNotKeyword($IDENT.text)}?;
path : identifier (DOT identifier)*;

// numbers

fragment DIGIT : [\p{Numeric_Type=Decimal}];
fragment NUMERIC_SUBSEQUENCE : (UNDERSCORE | DOT | DIGIT)*;
NUMBER: NUMERIC_SUBSEQUENCE DIGIT NUMERIC_SUBSEQUENCE; // at least one digit
numeric_literal : NUMBER;

// booleans

boolean_literal : TRUE | FALSE;

// strings

STRAIGHT_DOUBLE_STRING : '"' STRAIGHT_DOUBLE_STRING_INNER* '"';
fragment STRAIGHT_DOUBLE_STRING_INNER : ~'"' | '\\"';
straight_double_string : STRAIGHT_DOUBLE_STRING;

STRAIGHT_SINGLE_STRING : '\'' STRAIGHT_SINGLE_STRING_INNER* '\'';
fragment STRAIGHT_SINGLE_STRING_INNER : ~'\'' | '\\\'';
straight_single_string : STRAIGHT_SINGLE_STRING;

BACKTICK_STRING : '`' BACKTICK_STRING_INNER* '`';
fragment BACKTICK_STRING_INNER : ~'`' | '\\`';
backtick_string : BACKTICK_STRING;

SMART_DOUBLE_STRING : '“' SMART_DOUBLE_STRING_INNER* '”';
fragment SMART_DOUBLE_STRING_INNER : ~[“”] | '\\' [“”] | SMART_DOUBLE_STRING;
smart_double_string : SMART_DOUBLE_STRING;

SMART_SINGLE_STRING : '‘' SMART_SINGLE_STRING_INNER* '’';
fragment SMART_SINGLE_STRING_INNER : ~[‘’] | '\\' [‘’] | SMART_SINGLE_STRING;
smart_single_string : SMART_SINGLE_STRING;

string_literal : straight_double_string
               | straight_single_string
               | backtick_string
               | smart_double_string
               | smart_single_string;

// procedure declarations

procedure_decl : PROC identifier parameter_list;
anon_procedure_decl : PROC parameter_list;
parameter_list : OPEN_PAREN parameter (COMMA parameter_list)* CLOSE_PAREN;
parameter : (AT_SYM expression OF)? (identifier COLON)? expression;

// procedure calls

procedure_call : path argument_list;
argument_list : OPEN_PAREN argument (COMMA argument_list)* CLOSE_PAREN;
argument : expression | UNDERSCORE;

// collections

collection_literal : OPEN_BRACK argument_list CLOSE_BRACK;

// statements

statement : procedure_call
          | get_phrase
          | procedure_decl
          | module_statement
          | comment_statement
          | OPEN_BRACE statement* CLOSE_BRACE;
comment_statement : syntactic_comment_statement | semantic_comment_statement;

module_statement : MODULE identifier OF statement;

// expressions

literal : boolean_literal
        | numeric_literal
        | string_literal
        | collection_literal;

get_phrase : get_identifier_list statement;
get_identifier_list : GET identifier (COMMA identifier) FROM;

expression : procedure_call
           | literal
           | path
           | get_phrase
           | anon_procedure_decl
           | module_expression
           | comment_expression expression
           | expression comment_expression
           | OPEN_PAREN expression CLOSE_PAREN;
comment_expression : syntactic_comment_expression | semantic_comment_expression;

module_expression : MODULE identifier OF expression;

// comments

SEMANTIC_COMMENT_START : HASH_SYM (IDENT_CONT | NUMERIC_SUBSEQUENCE | HASH_SYM)* OPEN_BRACE;
SEMANTIC_COMMENT_END : CLOSE_BRACE (IDENT_CONT | NUMERIC_SUBSEQUENCE | HASH_SYM)* HASH_SYM;
semantic_comment_start : SEMANTIC_COMMENT_START {true}? {
};
semantic_comment_end : SEMANTIC_COMMENT_END {true}? {
};
semantic_comment_statement : semantic_comment_start statement semantic_comment_end;
semantic_comment_expression : semantic_comment_start expression semantic_comment_end;

SYNTACTIC_COMMENT_START : HASH_SYM (IDENT_CONT | NUMERIC_SUBSEQUENCE | HASH_SYM)* OPEN_BRACK;
SYNTACTIC_COMMENT_END : CLOSE_BRACK (IDENT_CONT | NUMERIC_SUBSEQUENCE | HASH_SYM)* HASH_SYM;
syntactic_comment_start : SYNTACTIC_COMMENT_START {true}? {
};
syntactic_comment_end : SYNTACTIC_COMMENT_END {true}? {
};
syntactic_comment_statement : syntactic_comment_start statement syntactic_comment_end;
syntactic_comment_expression : syntactic_comment_start expression syntactic_comment_end;


LEXICAL_COMMENT_START : HASH_SYM (IDENT_CONT | NUMERIC_SUBSEQUENCE | HASH_SYM)* OPEN_PAREN {
    setChannel(HIDDEN);
    lexicalCommentMarks.push(getText().substring(1, getText().length() - 2));
};
LEXICAL_COMMENT_END : CLOSE_PAREN (IDENT_CONT | NUMERIC_SUBSEQUENCE | HASH_SYM)* HASH_SYM {
    setChannel(HIDDEN);
    if (lexicalCommentMarks.peek().equals(getText().substring(1, getText().length() - 2))) {
        lexicalCommentMarks.pop();
    } else {
        more();
    }
};
LEXICAL_COMMENT : LEXICAL_COMMENT_START .*? LEXICAL_COMMENT_END -> channel(HIDDEN);

WHITESPACE : [\p{WHITE_SPACE}]
           -> skip;
