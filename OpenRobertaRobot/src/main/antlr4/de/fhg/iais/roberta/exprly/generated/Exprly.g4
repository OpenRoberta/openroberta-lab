grammar Exprly;

expr     : VAR                                                       # VarName
         | CONST                                                     # MathConst
         | literal
         | numExpr
         | boolExpr
         | colorExpr
         | connExpr
         | funCall
         | '(' expr ')'
         ;
          
literal  :  INT                                                     # IntConst
         |  BOOL                                                    # BoolConstB
         |  '"' STR '"'                                             # ConstStr
         |  '[' (expr ',')* expr? ']'
         ;

numExpr :  op=(ADD | SUB) expr                                     # Unary
        |  expr op=(MOD|POW) expr                                  # Binary
        |  expr op=(MUL | DIV ) expr                               # Binary
        |  expr op=(ADD | SUB) expr                                # Binary
        ;

bool    :   op=NOT expr                                             # Unary
        |   expr op=AND expr                                        # Binary
        |   expr op=OR expr                                         # Binary
        |   expr op=EQUAL expr                                      # Binary
        |   expr op=NEQUAL expr                                     # Binary
        |   expr op=GET expr                                        # Binary
        |   expr op=LET expr                                        # Binary
        |   expr op=GEQ expr                                        # Binary
        |   expr op=LEQ expr                                        # Binary
        ;

color   :  COLOR                                                    # Col
        |  '(' r=INT ',' g=INT ',' b=INT ',' a=INT ')'              # RGB
        ;

connExpr: 'connect' STR ',' STR;

arg_list: (expr ',')* expr
        ;
        
*** this should be redone, see the gitter remarks ***
function : VAR '(' (fname=FNAME ',')?  (lop=LELEMOP ',')? (index=INDEX ',')?  (btype=BTYPE ',')?  args=arg_list? ')'
        |  'randInt' '(' arg=arg_list?')'                          # RandInt
        |  'randFloat()'                                           # RandFloat
;

// LEXER RULES
NEWLINE    :    '\r'? '\n' -> skip;

WS        :    (' '|'\t')+ -> skip;

INT     :    ('0'..'9')+;

FNAME   : [Tt][Ii][Mm][Ee]
        | [Dd][Ii][Vv][Ii][Ss][Ii][Bb][Ll][Ee] '_' [Bb][Yy]
        | [Mm][Aa][Xx]
        | [Mm][Ii][Nn]
        | [Ll][Ii][Ss][Tt][Ss] '_'[Rr][Ee][Pp][Ee][Aa][Tt]
        | [Rr][Aa][Nn][Dd][Oo][Mm]
        | [Ee][Vv][Ee][Nn]
        | [Oo][Dd][Dd]
        | [Pp][Rr][Ii][Mm][Ee]
        | [Ww][Hh][Oo][Ll][Ee]
        | [Pp][Oo][Ss][Ii][Tt][Ii][Vv][Ee]
        | [Nn][Ee][Gg][Aa][Tt][Ii][Vv][Ee]
        | [Ss][Uu][Mm]
        | [Aa][Vv][Ee][Rr][Aa][Gg][Ee]
        | [Mm][Ee][Dd][Ii][Aa][Nn]
        | [Mm][Oo][Dd][Ee]
        | [Ss][Tt][Dd] '_' [Dd][Ee][Vv]
        | [Rr][Oo][Oo][Tt]
        | [Aa][Bb][Ss]
        | [Ll][Nn]
        | [Ll][Oo][Gg] '10'
        | [Ee][Xx][Pp]
        | [Pp][Oo][Ww] '10'
        | [Ss][Ii][Nn]
        | [Cc][Oo][Ss]
        | [Tt][Aa][Nn]
        | [Aa][Ss][Ii][Nn]
        | [Aa][Cc][Oo][Ss]
        | [Aa][Tt][Aa][Nn]
        | [Pp][Oo][Ww][Ee][Rr]
        | [Rr][Oo][Uu][Nn][Dd]
        | [Rr][Oo][Uu][Nn][Dd][Uu][Pp]
        | [Rr][Oo][Uu][Nn][Dd][Dd][Oo][Ww][Nn]
        | [Ll][Ii][Ss][Tt] '_' [Ii][Ss] '_' [Ee][Mm][Pp][Tt][Yy]
        | [Ll][Ee][Ff][Tt]
        | [Rr][Ii][Gg][Hh][Tt]
        | [Tt][Ee][Xx][Tt]
        | [Nn][Uu][Mm][Bb][Ee][Rr]
        | [Ll][Ii][Ss][Tt][Ss] '_' [Ll][Ee][Nn][Gg][Tt][Hh]
        | [Gg][Ee][Tt]'_' [Ss][Uu][Bb][Ll][Ii][Ss][Tt]
        ;

BTYPE   :  [Aa][Nn][Yy]
        |  [Cc][Oo][Mm][Pp][Aa][Rr][Aa][Bb][Ll][Ee]
        |  [Aa][Dd][Dd][Aa][Bb][Ll][Ee]
        |  [Aa][Rr][Rr][Aa][Yy]
        |  [Aa][Rr][Rr][Aa][Yy] '_' [Nn][Uu][Mm][Bb][Ee][Rr]
        |  [Aa][Rr][Rr][Aa][Yy] '_' [Ss][Tt][Rr][Ii][Nn][Gg]
        |  [Aa][Rr][Rr][Aa][Yy] '_' [Cc][Oo][Ll][Oo][Uu][Rr]
        |  [Aa][Rr][Rr][Aa][Yy] '_' [Bb][Oo][Oo][Ll][Ee][Aa][Nn]
        |  [Aa][Rr][Rr][Aa][Yy] '_' [Ii][Mm][Aa][Gg][Ee]
        |  [Aa][Rr][Rr][Aa][Yy] '_' [Cc][Oo][Nn][Nn][Ee][Cc][Tt][Ii][Oo][Nn]
        |  [Bb][Oo][Oo][Ll][Ee][Aa][Nn]
        |  [Nn][Uu][Mm][Bb][Ee][Rr]
        |  [Nn][Uu][Mm][Bb][Ee][Rr] '_' [Ii][Nn][Tt]
        |  [Ss][Tt][Rr][Ii][Nn][Gg]
        |  [Cc][Oo][Ll][Oo][Rr]
        |  [Ii][Mm][Aa][Gg][Ee]
        |  [Pp][Rr][Ee][Dd][Ee][Ff][Ii][Nn][Ee][Dd] '_' [Ii][Mm][Aa][Gg][Ee]
        |  [Nn][Uu][Ll][Ll]
        |  [Rr][Ee][Ff]
        |  [Pp][Rr][Ii][Mm]
        |  [Nn][Oo][Tt][Hh][Ii][Nn][Gg]
        |  [Vv][Oo][Ii][Dd]
        |  [Cc][Oo][Nn][Nn][Ee][Cc][Tt][Ii][Oo][Nn]
        |  [Cc][Aa][Pp][Tt][Uu][Rr][Ee][Dd] '_' [Tt][Yy][Pp][Ee]
        |  'R'
        |  'S'
        |  'T'
        ;

CONST   :  'phi'
        |  'pi'
        |  'e'
        |  'sqrt'
        |  'sqrt_1_2'
        |  'inf'
        ;

LELEMOP : [Gg][Ee][Tt]
        | [Gg][Ee][Tt] '_' [Rr][Ee][Mm][Oo][Vv][Ee]
        | [Rr][Ee][Mm][Oo][Vv][Ee]
        | [Ss][Ee][Tt]
        | [Ii][Nn][Ss][Ee][Rr][Tt]
        ;

INDEX   : [Ff][Ii][Rr][Ss][Tt]
        | [Ll][Aa][Ss][Tt]
        | [Ff][Rr][Oo][Mm] '_' [Ss][Tt][Aa][Rr][Tt]
        | [Ff][Rr][Oo][Mm] '_' [Ee][Nn][Dd]
        | [Rr][Aa][Nn][Dd][Oo][Mm]
        ;

BOOL    :  [Tt][Rr][Uu][Ee] | [Ff][Aa][Ll][Ss][Ee];

COLOR   :  '#' HEX HEX HEX HEX HEX HEX;
HEX     :  ('A'..'F'|'a'..'f'|'0'..'9');

VAR     :  ('a'..'z')('a'..'z''0'..'9')*;
STR     :  ('a'..'z'|'A'..'Z'|'0'..'9'|' ')+;

AND     :   '&&';
OR      :   '||';
NOT     :   '!';
EQUAL   :   '==';
NEQUAL  :   '!=';
GET     :   '>';
LET     :   '<';
GEQ     :   '>=';
LEQ     :   '<=';
MOD     :   '%';
POW     :   '^';
MUL     :   '*';
DIV     :   '/';
ADD     :   '+';
SUB     :   '-';
SEMI    :   ';';
ASSIGN  :   ':=';
