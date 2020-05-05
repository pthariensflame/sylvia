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
}

// comments

//SEMANTIC_COMMENT_START : HASH_SYM (IDENT_CONT | NUMERIC_SUBSEQUENCE | HASH_SYM)* OPEN_BRACE;
//SEMANTIC_COMMENT_END : CLOSE_BRACE (IDENT_CONT | NUMERIC_SUBSEQUENCE | HASH_SYM)* HASH_SYM;
//semantic_comment_start : SEMANTIC_COMMENT_START {true}? {
//};
//semantic_comment_end : SEMANTIC_COMMENT_END {true}? {
//};
//semantic_comment_statement : semantic_comment_start statement semantic_comment_end;
//semantic_comment_expression : semantic_comment_start expression semantic_comment_end;
//
//SYNTACTIC_COMMENT_START : HASH_SYM (IDENT_CONT | NUMERIC_SUBSEQUENCE | HASH_SYM)* OPEN_BRACK;
//SYNTACTIC_COMMENT_END : CLOSE_BRACK (IDENT_CONT | NUMERIC_SUBSEQUENCE | HASH_SYM)* HASH_SYM;
//syntactic_comment_start : SYNTACTIC_COMMENT_START {true}? {
//};
//syntactic_comment_end : SYNTACTIC_COMMENT_END {true}? {
//};
//syntactic_comment_statement : syntactic_comment_start statement syntactic_comment_end;
//syntactic_comment_expression : syntactic_comment_start expression syntactic_comment_end;

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

// strings

STRAIGHT_DOUBLE_STRING : '"' STRAIGHT_DOUBLE_STRING_INNER* '"';
fragment STRAIGHT_DOUBLE_STRING_INNER : ~["\\] | '\\' ["\\];
straight_double_string : STRAIGHT_DOUBLE_STRING;

STRAIGHT_SINGLE_STRING : '\'' STRAIGHT_SINGLE_STRING_INNER* '\'';
fragment STRAIGHT_SINGLE_STRING_INNER : ~['\\] | '\\' ['\\];
straight_single_string : STRAIGHT_SINGLE_STRING;

STRAIGHT_BACKTICK_STRING : '`' STRAIGHT_BACKTICK_STRING_INNER* '`';
fragment STRAIGHT_BACKTICK_STRING_INNER : ~[`\\] | '\\' [`\\];
straight_backtick_string : STRAIGHT_BACKTICK_STRING;

SMART_DOUBLE_STRING : '“' SMART_STRING_INNER* '”';
smart_double_string : SMART_DOUBLE_STRING;

SMART_SINGLE_STRING : '‘' SMART_STRING_INNER* '’';
smart_single_string : SMART_SINGLE_STRING;

SMART_CHEVRON_STRING : '«' SMART_STRING_INNER* '»';
smart_chevron_string : SMART_CHEVRON_STRING;

fragment SMART_STRING_ANY : SMART_DOUBLE_STRING
                          | SMART_SINGLE_STRING
                          | SMART_CHEVRON_STRING;
fragment SMART_STRING_INNER : ~[“”‘’«»\\] | '\\' [“”‘’«»\\] | SMART_STRING_ANY;

string_literal : contentStD=straight_double_string # StraightDoubleStringLiteral
               | contentStS=straight_single_string # StraightSingleStringLiteral
               | contentStB=straight_backtick_string # StraightBacktickStringLiteral
               | contentSmD=smart_double_string # SmartDoubleStringLiteral
               | contentSmS=smart_single_string # SmartSingleStringLiteral
               | contentSmC=smart_chevron_string # SmartChevronStringLiteral;

// Keywords and symbols

BIND : 'bind';
CASE : 'case';
CASES : 'cases';
DO : 'do';
DOC : 'doc';
EFFECT : 'effect';
ENUM : 'enum';
EXPORT : 'export';
FALSE : 'false';
FROM : 'from';
GET : 'get';
IMPORT : 'import';
MODULE : 'module';
NATURE : 'nature';
NEWTYPE : 'newType';
OF : 'of';
OVER : 'over';
PROC : 'proc';
PROC_TYPE : 'Proc';
RECORD : 'record';
TRUE : 'true';
TYPEALIAS : 'typeAlias';
AT_SYM : '@';
COLON : ':';
UNDERSCORE : '_';
ARROW_FROM : '<-' | '←';
ARROW_TO : '->' | '→';
COMMA : ',';
SEMICOLON : ';';
CLOSE_DOUBLE_BRACE : '⦄' | '|}';
CLOSE_BRACE : '}';
CLOSE_DOUBLE_BRACK : '⟧' | '|]';
CLOSE_BRACK : ']';
CLOSE_DOUBLE_PAREN : '⦆' | '|)';
CLOSE_PAREN : ')';
OPEN_DOUBLE_BRACE : '⦃' | '{|';
OPEN_BRACE : '{';
OPEN_DOUBLE_BRACK : '⟦' | '[|';
OPEN_BRACK : '[';
OPEN_DOUBLE_PAREN : '⦅' | '(|';
OPEN_PAREN : '(';
HASH_SYM : '#';
DOT : '.';

keyword : BIND
        | CASE
        | CASES
        | DO
        | DOC
        | EFFECT
        | ENUM
        | EXPORT
        | FALSE
        | FROM
        | GET
        | IMPORT
        | MODULE
        | NATURE
        | NEWTYPE
        | OF
        | OVER
        | PROC
        | PROC_TYPE
        | RECORD
        | TRUE
        | TYPEALIAS
        | AT_SYM
        | COLON
        | UNDERSCORE;

separator_symbol : ARROW_FROM
                 | ARROW_TO
                 | COMMA
                 | SEMICOLON;

enclosure_symbol : OPEN_BRACE
                 | OPEN_DOUBLE_BRACE
                 | OPEN_BRACK
                 | OPEN_DOUBLE_BRACK
                 | OPEN_PAREN
                 | OPEN_DOUBLE_PAREN
                 | CLOSE_BRACE
                 | CLOSE_DOUBLE_BRACE
                 | CLOSE_BRACK
                 | CLOSE_DOUBLE_BRACK
                 | CLOSE_PAREN
                 | CLOSE_DOUBLE_PAREN;

misc_symbol : HASH_SYM
            | DOT;

// numbers

fragment DIGIT : [\p{Numeric_Type=Decimal}];
fragment NUMERIC_SUBSEQUENCE : (UNDERSCORE | DOT | DIGIT)*;
NUMBER: NUMERIC_SUBSEQUENCE DIGIT NUMERIC_SUBSEQUENCE; // at least one digit
numeric_literal : NUMBER;

// booleans

boolean_literal : TRUE | FALSE;

// identifiers

fragment IDENT_START : [\p{XID_START}];
fragment IDENT_CONT : IDENT_START | [\p{XID_CONTINUE}];
IDENT : IDENT_START IDENT_CONT*;
identifier : IDENT;
path : segments+=identifier (DOT segments+=identifier)*;

name_declarator : AT_SYM nature=expression OF name=identifier COLON type=expression;

// procedure declarations

procedure_decl : head=procedure_decl_head body=block;
procedure_decl_head : PROC name=identifier paramList=single_or_brack_param;
anon_procedure_decl : head=anon_procedure_decl_head body=block;
anon_procedure_decl_head : PROC paramList=single_or_brack_param;
parameter_list : (params+=parameter (seps+=separator_symbol params+=parameter)*)?;
parameter : parts=name_declarator # FullParam
          | name=identifier # SimpleParam
          | UNDERSCORE # IgnoredParam
          | brack_param # BrackParam
          | paren_param # ParenParam;
brack_param : OPEN_BRACK parameter_list CLOSE_BRACK # SingleBrackParam
            | OPEN_DOUBLE_BRACK parameter_list CLOSE_DOUBLE_BRACK # DoubleBrackParam;
paren_param : OPEN_PAREN inner=parameter CLOSE_PAREN;
single_or_brack_param : brack_param | paren_param;

// collections

collection_literal returns [@NotNull Argument_listContext elemList]
    : OPEN_BRACK elemListS=argument_list CLOSE_BRACK { $ctx.elemList=$elemListS.ctx; } # SingleBrackCollLit
    | OPEN_DOUBLE_BRACK elemListD=argument_list CLOSE_DOUBLE_BRACK { $ctx.elemList=$elemListD.ctx; }# DoubleBrackCollLit;
argument_list returns [boolean hasMissings = false]
    : (args+=argument (seps+=separator_symbol args+=argument)*)? {
    };
argument returns [boolean hasMissings]
    : expr=expression { $ctx.hasMissings = false; } # ExprArg
    | missing=missing_arg { $ctx.hasMissings = true; } # MissingArg
    | sublist=collection_literal { $ctx.hasMissings = $sublist.elemList.hasMissings; } #SublistArg;
missing_arg : UNDERSCORE;

// procedure calls

procedure_call : invokee=procedure_call_head argExpr=expression;
procedure_call_head : name=path # PathProcCallHead
                    | OPEN_PAREN expr=expression CLOSE_PAREN # ParenProcCallHead;

// declarations

bind_decl : BIND paramList=parameter_list FROM body=block;

module_decl : MODULE name=identifier OF body=decl_block;

decl_block : OPEN_BRACE decls+=declaration* CLOSE_BRACE;

declaration : bind_decl # BindDecl
            | module_decl # ModuleDecl
            | procedure_decl # ProcedureDecl
            | documented_decl # DocumentedDecl;
documented_decl : DOC documentation=string_literal inner=declaration;

// statements

statement : call=procedure_call  # ProcedureCallStmt
          | decl=declaration # DeclarationStmt
//          | comment_statement
          | DO body=block # DoBlockStmt;
block : OPEN_BRACE stmts+=statement* CLOSE_BRACE;
//comment_statement : syntactic_comment_statement | semantic_comment_statement;

// expressions

literal : contentB=boolean_literal
        | contentN=numeric_literal
        | contentS=string_literal;

get_expr : GET paramList=parameter_list FROM block;

expression : getExpr=get_expr # GetExpr
           | anon_procedure_decl # AnonProcExpr
           | DO body=block # DoBlockExpr
//           | comment_expression expression # CommentBeforeExpr
//           | expression comment_expression # CommentAfterExpr
           | call=procedure_call # ProcedureCallExpr
           | lit=literal # LiteralExpr
           | name=path # UseValueExpr
           | OPEN_PAREN innerExpr=expression CLOSE_PAREN # ParenExpr;
//comment_expression : syntactic_comment_expression | semantic_comment_expression;

// whitespace

WHITESPACE : [\p{WHITE_SPACE}]
           -> skip;
