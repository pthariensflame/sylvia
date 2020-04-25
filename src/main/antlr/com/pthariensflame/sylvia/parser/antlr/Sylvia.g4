grammar Sylvia;

@header {
    package com.pthariensflame.sylvia.parser.antlr;
    import java.util.ArrayDeque;
    import java.util.Deque;
    import org.jetbrains.annotations.NotNull;
    import org.jetbrains.annotations.Nullable;
    import com.pthariensflame.sylvia.parser.SourceSpan;
}

@lexer::members {
    @NotNull Deque<String> lexicalCommentMarks = new ArrayDeque<>();
}

@parser::members {
    @NotNull Deque<String> syntacticCommentMarks = new ArrayDeque<>();
    @NotNull Deque<String> semanticCommentMarks = new ArrayDeque<>();
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
fragment IDENT_CONT : [\p{XID_CONTINUE}];
NAME_PART : IDENT_START IDENT_CONT+;
fragment SUBNAME : (UNDERSCORE | NAME_PART)*;
NAME : SUBNAME NAME_PART SUBNAME; // at least one name part
identifier : NAME {true}?;
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

procedure_decl : PROC identifier OPEN_PAREN parameter_list CLOSE_PAREN;
parameter_list : parameter (COMMA parameter_list)*;
parameter : (AT_SYM expression OF)? (identifier COLON)? expression;

// procedure calls

procedure_call : path OPEN_PAREN argument_list CLOSE_PAREN;
argument_list : argument (COMMA argument_list)*;
argument : expression | UNDERSCORE;

// collections

collection_literal : OPEN_BRACK argument_list CLOSE_BRACK;

// statements

statement : procedure_call
          | block
          | module_statement
          | semantic_comment_statement;

block : OPEN_BRACE statement* CLOSE_BRACE;

module_statement : MODULE identifier OF statement;

// expressions

literal : boolean_literal
        | numeric_literal
        | string_literal
        | collection_literal;

get_expression : GET parameter_list FROM statement;

expression : procedure_call
           | literal
           | identifier
           | get_expression
           | module_expression
           | semantic_comment_expression expression
           | expression semantic_comment_expression
           | OPEN_PAREN expression CLOSE_PAREN;

module_expression : MODULE identifier OF expression;

// comments

SEMANTIC_COMMENT_START : HASH_SYM (SUBNAME | NUMERIC_SUBSEQUENCE | HASH_SYM)* OPEN_BRACE;
SEMANTIC_COMMENT_END : CLOSE_BRACE (SUBNAME | NUMERIC_SUBSEQUENCE | HASH_SYM)* HASH_SYM;
semantic_comment_start : SEMANTIC_COMMENT_START {true}? {
};
semantic_comment_end : SEMANTIC_COMMENT_END {true}? {
};
semantic_comment_statement : semantic_comment_start statement semantic_comment_end;
semantic_comment_expression : semantic_comment_start expression semantic_comment_end;

SYNTACTIC_COMMENT_START : HASH_SYM (SUBNAME | NUMERIC_SUBSEQUENCE | HASH_SYM)* OPEN_BRACK;
SYNTACTIC_COMMENT_END : CLOSE_BRACK (SUBNAME | NUMERIC_SUBSEQUENCE | HASH_SYM)* HASH_SYM;
syntactic_comment_start : SYNTACTIC_COMMENT_START {true}? {
};
syntactic_comment_end : SYNTACTIC_COMMENT_END {true}? {
};
syntactic_comment_statement : syntactic_comment_start statement syntactic_comment_end;
syntactic_comment_expression : syntactic_comment_start expression syntactic_comment_end;


LEXICAL_COMMENT_START : HASH_SYM (SUBNAME | NUMERIC_SUBSEQUENCE | HASH_SYM)* OPEN_PAREN {
    setChannel(HIDDEN);
    lexicalCommentMarks.push(getText().substring(1, getText().length() - 2));
};
LEXICAL_COMMENT_END : CLOSE_PAREN (SUBNAME | NUMERIC_SUBSEQUENCE | HASH_SYM)* HASH_SYM {
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
