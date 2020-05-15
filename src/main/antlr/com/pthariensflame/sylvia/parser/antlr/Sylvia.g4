grammar Sylvia;

options {
    language = Java;
}

@lexer::header {
    package com.pthariensflame.sylvia.parser.antlr;
    import static com.pthariensflame.sylvia.parser.PredicatesAndExtractorsKt.checkMatchedComment;
}

@parser::header {
    package com.pthariensflame.sylvia.parser.antlr;
}

program : stmts+=statement*;

// comments

//SEMANTIC_COMMENT_START : HASH_SYM COMMENT_IDENTIFIER OPEN_BRACE;
//SEMANTIC_COMMENT_END : CLOSE_BRACE COMMENT_IDENTIFIER HASH_SYM;
//semantic_comment_start : SEMANTIC_COMMENT_START {true}? {
//};
//semantic_comment_end : SEMANTIC_COMMENT_END {true}? {
//};
//semantic_comment_statement : semantic_comment_start statement semantic_comment_end;
//semantic_comment_expression : semantic_comment_start expression semantic_comment_end;
//
//SYNTACTIC_COMMENT_START : HASH_SYM COMMENT_IDENTIFIER OPEN_BRACK;
//SYNTACTIC_COMMENT_END : CLOSE_BRACK COMMENT_IDENTIFIER HASH_SYM;
//syntactic_comment_start : SYNTACTIC_COMMENT_START {true}? {
//};
//syntactic_comment_end : SYNTACTIC_COMMENT_END {true}? {
//};
//syntactic_comment_statement : syntactic_comment_start statement syntactic_comment_end;
//syntactic_comment_expression : syntactic_comment_start expression syntactic_comment_end;

fragment COMMENT_IDENTIFIER : (IDENT_CONT | NUMERIC_SUBSEQUENCE | HASH_SYM)*;
fragment LEXICAL_LINE_COMMENT_BEGIN : HASH_SYM COMMENT_IDENTIFIER COLON;
fragment LEXICAL_COMMENT_START : HASH_SYM COMMENT_IDENTIFIER (OPEN_PAREN | OPEN_DOUBLE_PAREN);
fragment LEXICAL_COMMENT_END : (CLOSE_PAREN | CLOSE_DOUBLE_PAREN) COMMENT_IDENTIFIER HASH_SYM;
LEXICAL_LINE_COMMENT : LEXICAL_LINE_COMMENT_BEGIN NON_LINE_END* LINE_END -> channel(HIDDEN);
LEXICAL_BLOCK_COMMENT : LEXICAL_COMMENT_START (LEXICAL_BLOCK_COMMENT | OTHER_CHARS)* LEXICAL_COMMENT_END
    {checkMatchedComment(getText())}? -> channel(HIDDEN);

// strings

fragment STRAIGHT_STRING_ESCAPE : ["'`\\0nNrRfFtT] | [Uu] (
    '+' [0-9A-Fa-f]+
    | '!' [-_a-zA-Z0-9\p{WHITE_SPACE}]+
)* ':';
fragment SMART_STRING_ESCAPE : [“”‘’«»] | STRAIGHT_STRING_ESCAPE;

STRAIGHT_DOUBLE_STRING : '"' STRAIGHT_DOUBLE_STRING_INNER* '"';
fragment STRAIGHT_DOUBLE_STRING_INNER : ~["\\] | '\\' STRAIGHT_STRING_ESCAPE;
straight_double_string : STRAIGHT_DOUBLE_STRING;

STRAIGHT_SINGLE_STRING : '\'' STRAIGHT_SINGLE_STRING_INNER* '\'';
fragment STRAIGHT_SINGLE_STRING_INNER : ~['\\] | '\\' STRAIGHT_STRING_ESCAPE;
straight_single_string : STRAIGHT_SINGLE_STRING;

STRAIGHT_BACKTICK_STRING : '`' STRAIGHT_BACKTICK_STRING_INNER* '`';
fragment STRAIGHT_BACKTICK_STRING_INNER : ~[`\\] | '\\' STRAIGHT_STRING_ESCAPE;
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
fragment SMART_STRING_INNER : ~[“”‘’«»\\] | '\\' SMART_STRING_ESCAPE | SMART_STRING_ANY;

string_literal : stDString=straight_double_string # StraightDoubleStringLiteral
               | stSString=straight_single_string # StraightSingleStringLiteral
               | stBString=straight_backtick_string # StraightBacktickStringLiteral
               | smDString=smart_double_string # SmartDoubleStringLiteral
               | smSString=smart_single_string # SmartSingleStringLiteral
               | smCString=smart_chevron_string # SmartChevronStringLiteral;

// Keywords and symbols

ALIAS : 'alias';
BIND : 'bind';
CASES : 'cases';
CASE : 'case';
DOC : 'doc';
DO : 'do';
EFFECT : 'effect';
ENUM : 'enum';
FALSE : 'false';
FROM : 'from';
GET : 'get';
HIDDEN_VIS : 'hidden';
MODULE : 'module';
NATURE : 'nature';
OF : 'of';
OVER : 'over';
PRIMITIVE : 'primitive';
PROC : 'proc';
PROC_TYPE : 'Proc';
RECORD : 'record';
TRUE : 'true';
TYPE_TYPE : 'Type';
VISIBLE_VIS : 'visible';
AT_SYM : '@';
COLON : ':';
SCOPE_SEP : '/';
UNDERSCORE : '_';
ARROW_FROM : '<-' | '←';
THICK_ARROW_FROM : '<=' | '⇐';
ARROW_TO : '->' | '→';
THICK_ARROW_TO : '=>' | '⇒';
COMMA : ',';
SEMICOLON : ';';
TILDE : '~';
CLOSE_DOUBLE_BRACE : '⦄';
CLOSE_BRACE : '}';
CLOSE_DOUBLE_BRACK : '⟧';
CLOSE_BRACK : ']';
CLOSE_DOUBLE_PAREN : '⦆';
CLOSE_PAREN : ')';
OPEN_DOUBLE_BRACE : '⦃';
OPEN_BRACE : '{';
OPEN_DOUBLE_BRACK : '⟦';
OPEN_BRACK : '[';
OPEN_DOUBLE_PAREN : '⦅';
OPEN_PAREN : '(';
HASH_SYM : '#';
DOT : '.';

keyword : ALIAS
        | BIND
        | CASE
        | CASES
        | DO
        | DOC
        | EFFECT
        | ENUM
        | FALSE
        | FROM
        | GET
        | HIDDEN_VIS
        | MODULE
        | NATURE
        | OF
        | OVER
        | PRIMITIVE
        | PROC
        | PROC_TYPE
        | RECORD
        | TRUE
        | TYPE_TYPE
        | VISIBLE_VIS
        | AT_SYM
        | COLON
        | SCOPE_SEP
        | UNDERSCORE;

separator_symbol : ARROW_FROM
                 | THICK_ARROW_FROM
                 | ARROW_TO
                 | THICK_ARROW_TO
                 | COMMA
                 | SEMICOLON
                 | TILDE;

enclosure_symbol : (OPEN_PAREN
                 | CLOSE_PAREN) # ParenthesisEnclosure
                 | (OPEN_BRACE
                 | CLOSE_BRACE) # BracketEnclosure
                 | (OPEN_BRACK
                 | CLOSE_BRACK) # BraceEnclosure
                 | (OPEN_DOUBLE_PAREN
                 | CLOSE_DOUBLE_PAREN) # DoubleParenthesisEnclosure
                 | (OPEN_DOUBLE_BRACK
                 | CLOSE_DOUBLE_BRACK) # DoubleBracketEnclosure
                 | (OPEN_DOUBLE_BRACE
                 | CLOSE_DOUBLE_BRACE) # DoubleBraceEnclosure;

misc_symbol : HASH_SYM
            | DOT;

// numbers

fragment DIGIT : [\p{Numeric_Type=Decimal}];
fragment NUMERIC_SUBSEQUENCE : (UNDERSCORE | DOT | DIGIT)*;
NUMBER: NUMERIC_SUBSEQUENCE DIGIT NUMERIC_SUBSEQUENCE; // at least one digit
numeric_literal : NUMBER;

// booleans

boolean_literal : TRUE # TrueLiteral
                | FALSE # FalseLiteral;

// identifiers

fragment IDENT_START : [\p{XID_START}];
fragment IDENT_CONT : IDENT_START | [\p{XID_CONTINUE}];
IDENT : IDENT_START IDENT_CONT*;
identifier : IDENT;
path : segments+=identifier (SCOPE_SEP segments+=identifier)*;

name_declarator : AT_SYM nature=expression OF name=identifier COLON type=expression;

// procedure declarations
procedure_decl : alias=alias_procedure_decl # AliasProcedureDecl
               | prim=prim_procedure_decl # PrimProcedureDecl
               | norm=norm_procedure_decl # NormProcedureDecl;
norm_procedure_decl : head=procedure_decl_head body=block;
prim_procedure_decl : head=procedure_decl_head PRIMITIVE primID=string_literal;
alias_procedure_decl : PROC ALIAS name=identifier OF underlying=path;
procedure_decl_head : PROC name=identifier paramList=single_or_brack_param;
anon_procedure_expr : norm=norm_anon_procedure_expr # NormAnonProcExpr
                    | prim=prim_anon_procedure_expr # PrimAnonProcExpr;
norm_anon_procedure_expr : head=anon_procedure_decl_head body=block;
prim_anon_procedure_expr : head=anon_procedure_decl_head PRIMITIVE primID=string_literal;
anon_procedure_decl_head : PROC paramList=single_or_brack_param;
parameter_list : (params+=parameter (seps+=separator_symbol params+=parameter)*)?;
parameter : declarator=name_declarator # FullParam
          | name=identifier # SimpleParam
          | UNDERSCORE # IgnoredParam
          | inner=single_or_brack_param # NestedParam;
brack_param : OPEN_BRACK params=parameter_list CLOSE_BRACK // deliberately unnamed
            | OPEN_DOUBLE_BRACK params=parameter_list CLOSE_DOUBLE_BRACK; // deliberately unnamed
paren_param : OPEN_PAREN inner=parameter CLOSE_PAREN // deliberately unnamed
            | OPEN_DOUBLE_PAREN inner=parameter CLOSE_DOUBLE_PAREN; // deliberately unnamed
single_or_brack_param : brackParam=brack_param # BrackParam
                      | parenParam=paren_param # ParenParam;

// collections

collection_literal : OPEN_BRACK elemList=argument_list CLOSE_BRACK
                   | OPEN_DOUBLE_BRACK elemList=argument_list CLOSE_DOUBLE_BRACK;
argument_list : (args+=argument (seps+=separator_symbol args+=argument)*)?;
argument : expr=expression # ExprArg
         | missing=missing_arg # MissingArg
         | sublist=collection_literal #SublistArg;
missing_arg : UNDERSCORE;

// procedure calls

procedure_call : invokee=enclosed_expr argExpr=enclosed_expr;

// declarations

bind_decl : BIND paramList=parameter_list bindingSource=binding_source;

module_decl : MODULE name=identifier OF body=decl_block;

decl_block : OPEN_BRACE decls+=declaration* CLOSE_BRACE // deliberately unnamed
           | OPEN_DOUBLE_BRACE decls+=declaration* CLOSE_DOUBLE_BRACE;// deliberately unnamed

declaration : bind=bind_decl # BindDecl
            | module=module_decl # ModuleDecl
            | ALIAS name=identifier OF path # AliasDecl
            | procedure=procedure_decl # ProcedureDecl
            | documented=documented_decl # DocumentedDecl
            | DO BIND body=decl_block # DoBlockDecl
            | vis=visibility inner=declaration # VisDecl;
documented_decl : DOC documentation=string_literal inner=declaration;
visibility : VISIBLE_VIS # VisibleVisibility
           | HIDDEN_VIS # HiddenVisibility;

// statements

statement : decl=declaration # DeclarationStmt
          | expr=expression  # ExpressionStmt
          | DO body=block # DoBlockStmt;
//          | comment_statement
block : OPEN_BRACE stmts+=statement* CLOSE_BRACE // deliberately unnamed
      | OPEN_DOUBLE_BRACE stmts+=statement* CLOSE_DOUBLE_BRACE; // deliberately unnamed
//comment_statement : syntactic_comment_statement | semantic_comment_statement;

// expressions

literal : booleanLit=boolean_literal # BoolLiteral
        | numericLit=numeric_literal # NumLiteral
        | stringLit=string_literal # StringLiteral;

get_expr : GET paramList=parameter_list bindingSource=binding_source;

expression : getExpr=get_expr # GetExpr
           | anonProc=anon_procedure_expr # AnonProcExpr
           | DO GET body=block # DoBlockExpr
           | call=procedure_call # ProcedureCallExp
//              | comment_expression expression # CommentBeforeExpr
//              | expression comment_expression # CommentAfterExpr
           | inner=enclosed_expr # EnclosedExpr;
enclosed_expr : lit=literal # LiteralExpr
              | name=path # PathUseExpr
              | (OPEN_PAREN innerExpr=expression CLOSE_PAREN
              | OPEN_DOUBLE_PAREN innerExpr=expression CLOSE_DOUBLE_PAREN) # ParenExpr;
//comment_expression : syntactic_comment_expression | semantic_comment_expression;

binding_source : FROM body=block # BlockBindingSource
               | FROM inner=expression # ExprBindingSource
               | PRIMITIVE primID=string_literal # PrimBindingSource;

// whitespace and line endings

fragment LINE_END : [\u000D\u000A\u0085\u000B\u000C\u2028\u2029];
fragment NON_LINE_END : ~[\u000D\u000A\u0085\u000B\u000C\u2028\u2029];

WHITESPACE : [\p{WHITE_SPACE}] -> skip;

fragment OTHER_CHARS : .+?;
