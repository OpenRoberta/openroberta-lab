grammar Exprly;

expr     : CONST                                                     # MathConst
         | VAR                                                       # VarName
         | literal                                                   # LiteralExp
         | numExpr                                                   # NumExp
         | boolExpr                                                  # BoolExp
         | colorExpr                                                 # ColorExp
         | connExpr                                                  # ConnExp
         | funCall                                                   # FuncExp
         | '(' expr ')'                                              # Parenthese
         ; 
          
literal  :  INT                                                     # IntConst
         |  FLOAT                                                   # FloatConst
         |  BOOL                                                    # BoolConstB
         |  '"' STR '"'                                             # ConstStr
         |  '[' (expr ',')* expr? ']'                               # ListExpr
         ;

numExpr :  op=(ADD | SUB) expr                                      # UnaryN
        |  expr op=(MOD|POW) expr                                   # BinaryN
        |  expr op=(MUL | DIV ) expr                                # BinaryN
        |  expr op=(ADD | SUB) expr                                 # BinaryN
        ;

boolExpr:   op=NOT expr                                             # UnaryB
        |   expr op=AND expr                                        # BinaryB
        |   expr op=OR expr                                         # BinaryB
        |   expr op=EQUAL expr                                      # BinaryB
        |   expr op=NEQUAL expr                                     # BinaryB
        |   expr op=GET expr                                        # BinaryB
        |   expr op=LET expr                                        # BinaryB
        |   expr op=GEQ expr                                        # BinaryB
        |   expr op=LEQ expr                                        # BinaryB
        ;

colorExpr:  COLOR                                                   # Col
        |  '(' r=INT ',' g=INT ',' b=INT ',' a=INT ')'              # RGB
        ;

connExpr: 'connect' STR ',' STR;

args    : '('(expr ',')* expr? ')'                                  # ArgList
        ;

funCall : FNAME  (expr| args) 
		;
		
// LEXER RULES
NEWLINE    :    '\r'? '\n' -> skip;

WS        :    (' '|'\t')+ -> skip;

FNAME   :  'sin'
        |  'cos'
        |  'tan'
        |  'asin'
        |  'acos'
        |  'atan'
        |  'exp'
        |  'sqrt'
        |  'e^'
        |  'abs'
        |  'log10'
        |  'ln'
        |  '10^'
        |  'randInt'
        |  'randFLoat'
        |  'floor'
        |  'ceil'
        |  'isEven'
        |  'isOdd'
        |  'isPrime'
        |  'isWhole'
        |  'sum'
        |  'max'
        |  'min'
        |  'avg'
        |  'mean'
        |  'sd'
        |  'randItem'
        ;
        
CONST   :  'phi'
        |  'pi'
        |  'e'
        |  'sqrt2'
        |  'sqrt_1_2'
        |  'inf'
        ;


FLOAT   :    ('0'..'9')+ '.' ('0'..'9')+;
INT     :    ('0'..'9')+;

BOOL    :  'true' | 'false';

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
