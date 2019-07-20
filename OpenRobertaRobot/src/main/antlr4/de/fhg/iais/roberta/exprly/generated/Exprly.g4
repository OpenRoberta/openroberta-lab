grammar Exprly;
 
expression: expr;

expr     : CONST                                                     # MathConst
         | VAR                                                       # VarName
         | literal                                                   # LiteralExp
         | connExpr                                                  # ConnExp
         | funCall                                                   # FuncExp
         | '(' expr ')'                                              # Parenthese
         | op=(ADD | SUB) expr                                       # UnaryN
         | op=NOT expr                                               # UnaryB
         | <assoc=right> expr op=POW  expr                           # BinaryN
         | <assoc=right> expr op=MOD expr                            # BinaryN
         | expr op=(MUL | DIV ) expr                                 # BinaryN
         | expr op=(ADD | SUB) expr                                  # BinaryN
         | expr op=AND expr                                          # BinaryB
         | expr op=OR expr                                           # BinaryB
         | expr op=EQUAL expr                                        # BinaryB
         | expr op=NEQUAL expr                                       # BinaryB
         | expr op=GET expr                                          # BinaryB
         | expr op=LET expr                                          # BinaryB
         | expr op=GEQ expr                                          # BinaryB
         | expr op=LEQ expr                                          # BinaryB
         ; 
          
literal  : INT                                                       # IntConst
         | FLOAT                                                     # FloatConst
         | BOOL                                                      # BoolConstB
         | '"'  .*?  '"'                                             # ConstStr
         | COLOR                                                     # Col
         | '[' (expr ',')* expr? ']'                                 # ListExpr
         ;

connExpr: 'connect' op0=(STR|VAR) ',' op1=(STR|VAR)                  # Conn
         ;

funCall : FNAME '(' ( expr (',' expr)* )? ')'                        # Func
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
        |  'abs'
        |  'log10'
        |  'ln'
        |  'randInt'
        |  'randFloat' 
        |  'floor'
        |  'ceil'
        |  'isEven'
        |  'isOdd'
        |  'isPrime'
        |  'isWhole'
        |  'isEmpty'
        |  'sum'
        |  'max'
        |  'min'
        |  'avg'
        |  'median'
        |  'sd'
        |  'lengthOf'
        |  'setIndex'
        |  'setIndexFromEnd'
        |  'setIndexFirst'
        |  'setIndexLast'
        |  'insertIndex'
        |  'insertIndexFromEnd'
        |  'insertIndexFirst'
        |  'insertIndexLast'
        |  'getIndex'
        |  'getIndexFromEnd'
        |  'getIndexFirst'
        |  'getIndexLast'
        |  'getAndRemoveIndex'
        |  'getAndRemoveIndexFromEnd'
        |  'getAndRemoveIndexFirst'
        |  'getAndRemoveIndexLast'
        |  'removeIndex'
        |  'removeIndexFromEnd'
        |  'removeIndexFirst'
        |  'removeIndexLast'
        |  'repeatList'
        |  'subList'
        |  'subListFromIndexToLast'
        |  'subListFromIndexToEnd'
        |  'subListFromFirstToIndex'
        |  'subListFromFirstToLast'
        |  'subListFromFirstToEnd'
        |  'subListFromEndToIndex'
        |  'subListFromEndToEnd'
        |  'subListFromEndToLast'
        |  'print'
        |  'cat'
        |  'constrain'
        |  'getRGB'
        ;
        
CONST   :  'phi'
        |  'pi'
        |  'e'
        |  'sqrt2'
        |  'sqrt_1_2'
        |  'inf'
        ;


INT     :    ('0'..'9')+;

FLOAT   :    INT+ '.' INT*
        |    '.' INT+
        ;


BOOL    :  'true' | 'false';

COLOR   :  '#' HEX HEX HEX HEX HEX HEX;
HEX     :  ('A'..'F'|'0'..'9');

VAR     :  ('a'..'z')('a'..'z'|'0'..'9'|'_')*;
STR     :  ('a'..'z'|'A'..'Z'|'0'..'9')+;

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
