grammar Sylvia;

options {
    language = Java;
}

@lexer::header {
    package com.pthariensflame.sylvia.parser.antlr;
    import static com.pthariensflame.sylvia.parser.PredicatesKt.checkMatchedComment;
}

@parser::header {
    package com.pthariensflame.sylvia.parser.antlr;
}

program : stmts+=statement*;

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

fragment LEXICAL_LINE_COMMENT_BEGIN : HASH_SYM (HASH_SYM | DOT | AT_SYM | COLON)* COLON;
fragment LEXICAL_COMMENT_START : HASH_SYM (IDENT_CONT | NUMERIC_SUBSEQUENCE | HASH_SYM)* (OPEN_PAREN | OPEN_DOUBLE_PAREN);
fragment LEXICAL_COMMENT_END : (CLOSE_PAREN | CLOSE_DOUBLE_PAREN) (IDENT_CONT | NUMERIC_SUBSEQUENCE | HASH_SYM)* HASH_SYM;
LEXICAL_LINE_COMMENT : LEXICAL_LINE_COMMENT_BEGIN NON_LINE_END* LINE_END -> channel(HIDDEN);
LEXICAL_BLOCK_COMMENT : LEXICAL_COMMENT_START (LEXICAL_BLOCK_COMMENT | ANY_CHAR)* LEXICAL_COMMENT_END
    {checkMatchedComment(getText())}? -> channel(HIDDEN);

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
PRIMITIVE : 'primitive';
PROC : 'proc';
PROC_TYPE : 'Proc';
RECORD : 'record';
TRUE : 'true';
TYPEALIAS : 'typeAlias';
AT_SYM : '@';
COLON : ':';
UNDERSCORE : '_';
ARROW_FROM : '<-' | '←';
THICK_ARROW_FROM : '<=' | '⇐';
ARROW_TO : '->' | '→';
THICK_ARROW_TO : '=>' | '⇒';
COMMA : ',';
SEMICOLON : ';';
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
TILDE : '~';
SCOPE_SEP : '/';

keyword : ALIAS
        | BIND
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
        | PRIMITIVE
        | PROC
        | PROC_TYPE
        | RECORD
        | TRUE
        | TYPEALIAS
        | AT_SYM
        | COLON
        | UNDERSCORE
        | SCOPE_SEP;

separator_symbol : ARROW_FROM
                 | THICK_ARROW_FROM
                 | ARROW_TO
                 | THICK_ARROW_TO
                 | COMMA
                 | SEMICOLON
                 | TILDE;

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
procedure_decl : norm=norm_procedure_decl # NormProcedureDecl
               | prim=prim_procedure_decl # PrimProcedureDecl;
norm_procedure_decl : head=procedure_decl_head body=block;
prim_procedure_decl : head=procedure_decl_head PRIMITIVE primID=string_literal;
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
brack_param : OPEN_BRACK params=parameter_list CLOSE_BRACK # SingleBrackParam
            | OPEN_DOUBLE_BRACK params=parameter_list CLOSE_DOUBLE_BRACK # DoubleBrackParam;
paren_param : (OPEN_PAREN inner=parameter CLOSE_PAREN
            | OPEN_DOUBLE_PAREN inner=parameter CLOSE_DOUBLE_PAREN);
single_or_brack_param : brackParam=brack_param # BrackParam
                      | parenParam=paren_param # ParenParam;

// collections

collection_literal : OPEN_BRACK elemList=argument_list CLOSE_BRACK # SingleBrackCollLit
                   | OPEN_DOUBLE_BRACK elemList=argument_list CLOSE_DOUBLE_BRACK # DoubleBrackCollLit;
argument_list : (args+=argument (seps+=separator_symbol args+=argument)*)?;
argument : expr=expression # ExprArg
         | missing=missing_arg # MissingArg
         | sublist=collection_literal #SublistArg;
missing_arg : UNDERSCORE;

// procedure calls

procedure_call : invokee=procedure_call_head argExpr=expression;
procedure_call_head : name=path # PathProcCallHead
                    | OPEN_PAREN expr=expression CLOSE_PAREN # SingleParenProcCallHead
                    | OPEN_DOUBLE_PAREN expr=expression CLOSE_DOUBLE_PAREN # DoubleParenProcCallHead;

// declarations

bind_prim_decl : BIND PRIMITIVE declarator=name_declarator FROM primID=string_literal;

bind_decl : BIND paramList=parameter_list FROM body=block;

module_decl : MODULE name=identifier OF body=decl_block;

decl_block : OPEN_BRACE decls+=declaration* CLOSE_BRACE;

declaration : bindPrim=bind_prim_decl # BindPrimDecl
            | bind=bind_decl # BindDecl
            | module=module_decl # ModuleDecl
            | procedure=procedure_decl # ProcedureDecl
            | documented=documented_decl # DocumentedDecl
            | EXPORT exported=declaration # ExportedDecl
            | IMPORT identifier FROM path # ImportedDecl
            | DO body=decl_block # DoBlockDecl;
documented_decl : DOC documentation=string_literal inner=declaration;

// statements

statement : expr=expression  # ExpressionStmt
          | decl=declaration # DeclarationStmt
          | DO body=block # DoBlockStmt;
//          | comment_statement
block : OPEN_BRACE stmts+=statement* CLOSE_BRACE;
//comment_statement : syntactic_comment_statement | semantic_comment_statement;

// expressions

literal : booleanLit=boolean_literal # BoolLiteral
        | numericLit=numeric_literal # NumLiteral
        | stringLit=string_literal # StringLiteral;

get_expr : GET paramList=parameter_list FROM body=block;

expression : getExpr=get_expr # GetExpr
           | anonProc=anon_procedure_expr # AnonProcExpr
           | DO body=block # DoBlockExpr
//           | comment_expression expression # CommentBeforeExpr
//           | expression comment_expression # CommentAfterExpr
           | call=procedure_call # ProcedureCallExpr
           | lit=literal # LiteralExpr
           | name=path # PathUseExpr
           | (OPEN_PAREN innerExpr=expression CLOSE_PAREN
           | OPEN_DOUBLE_PAREN innerExpr=expression CLOSE_DOUBLE_PAREN) # ParenExpr;
//comment_expression : syntactic_comment_expression | semantic_comment_expression;

// whitespace and line endings

fragment LINE_END : [\u000D\u000A\u0085\u000B\u000C\u2028\u2029];
fragment NON_LINE_END : [^\u000D\u000A\u0085\u000B\u000C\u2028\u2029];

WHITESPACE : [\p{WHITE_SPACE}] -> skip;

// assorted

fragment ANY_CHAR : .;
