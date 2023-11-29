grammar Exprly;

expression: expr EOF
         ;

expr     : NULL                                                      # NullConst
         | CONST                                                     # MathConst
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
         | expr '?' expr ':' expr									 # IfElseOp
         ;

statementList: (stmt';')*
         ;

stmt:    FNAMESTMT '(' ( expr (',' expr)* )? ')'                                                                                         # StmtFunc
        | VAR op=SET  expr                                                                                                               # BinaryVarAssign
        | IF '(' expr ')' '{' statementList '}' (ELSEIF '(' expr ')' '{' statementList '}')* (op=ELSE '{' statementList '}')?            # ConditionStatementBlock
        | REPEATINF '{' statementList '}'                                                                                                # RepeatIndefinitely
        | REPEATFOR VAR '=''(' expr ','expr ','expr')' '{' statementList '}'                                                            # RepeatFor
        | op= (REPEATTIMES  | REPEATUNTIL | REPEATWHILE  )'(' expr ')' '{' statementList '}'                                             # RepeatStatement
        | REPEATFOREACH '(' PRIMITIVETYPE  VAR ':' expr ')' '{' statementList '}'                                                        # RepeatForEach
        | WAIT '(' expr ')' '{' statementList '}' (op=ORWAITFOR '(' expr ')' '{' statementList '}')*                                     # WaitStatement
        | op= (BREAK | CONTINUE )                                                                                                        # FlowControl
        | WAITMS'(' expr ')'                                                                                                             # WaitTimeStatement
        ;


literal  : COLOR                                                     # Col
         | INT                                                       # IntConst
         | FLOAT                                                     # FloatConst
         | BOOL                                                      # BoolConstB
         | '"'  (.*?|'.'|'?')  '"'                                   # ConstStr
         | '[' (expr ',')* expr? ']'                                 # ListExpr
         ;

connExpr : 'connect' op0=(STR|VAR) ',' op1=(STR|VAR)                 # Conn
         ;

funCall  : FNAME '(' ( expr (',' expr)* )? ')'                      # Func
		 ;

// LEXER RULES
IF              :   'if';
ELSEIF          :   'elseif';
ELSE            :   'else';

REPEATINF       :   'repeatInf';
REPEATTIMES     :   'repeatTimes';
REPEATUNTIL     :   'repeatUntil';
REPEATWHILE     :   'repeatWhile';
REPEATFOR       :   'repeatFor';
REPEATFOREACH   :   'for';
BREAK           :   'break';
CONTINUE        :   'continue';
WAIT            :   'waitUntil';
ORWAITFOR       :   'orWaitFor';
WAITMS          :   'waitMs';


PRIMITIVETYPE   :   'number'
                |    'boolean'
                |    'string'
                |    'color'
                |    'connection'
                ;

NEWLINE : '\r'? '\n'  -> skip;

WS      : (' '|'\t')+ -> skip;

FNAME   : 'sin'
        | 'cos'
        | 'tan'
        | 'asin'
        | 'acos'
        | 'atan'
        | 'exp'
        | 'square'
        | 'sqrt'
        | 'abs'
        | 'log10'
        | 'ln'
        | 'randInt'
        | 'randFloat'
        | 'randItem'
        | 'roundDown'
        | 'roundUp'
        | 'round'
        | 'isEven'
        | 'isOdd'
        | 'isPrime'
        | 'isWhole'
        | 'isEmpty'
        | 'isPositive'
        | 'isNegative'
        | 'isDivisibleBy'
        | 'sum'
        | 'max'
        | 'min'
        | 'avg'
        | 'median'
        | 'sd'
        | 'lengthOf'
        | 'indexOfFirst'
        | 'indexOfLast'
        | 'getIndex'
        | 'getIndexFromEnd'
        | 'getIndexFirst'
        | 'getIndexLast'
        | 'getAndRemoveIndex'
        | 'getAndRemoveIndexFromEnd'
        | 'getAndRemoveIndexFirst'
        | 'getAndRemoveIndexLast'
        | 'repeatList'
        | 'subList'
        | 'subListFromIndexToLast'
        | 'subListFromIndexToEnd'
        | 'subListFromFirstToIndex'
        | 'subListFromFirstToLast'
        | 'subListFromFirstToEnd'
        | 'subListFromEndToIndex'
        | 'subListFromEndToEnd'
        | 'subListFromEndToLast'
        | 'print'
        | 'createTextWith'
        | 'constrain'
        | 'getRGB'
        ;

FNAMESTMT: 'showText'
        | 'setIndex'
        | 'setIndexFromEnd'
        | 'setIndexFirst'
        | 'setIndexLast'
        | 'insertIndex'
        | 'insertIndexFromEnd'
        | 'insertIndexFirst'
        | 'insertIndexLast'
        | 'removeIndex'
        | 'removeIndexFromEnd'
        | 'removeIndexFirst'
        | 'removeIndexLast'
        ;

CONST   : 'phi'
        | 'pi'
        | 'e'
        | 'sqrt2'
        | 'sqrt_1_2'
        | 'inf'
        ;

NULL    : 'null';

INT     : ('0'..'9')+;

FLOAT   : INT+ '.' INT*
        | '.' INT+
        ;

COLOR   : '#black'
        | '#blue'
        | '#green'
        | '#yellow'
        | '#red'
        | '#white'
        | '#brown'
        | '#none'
        | '#rgb(' HEX HEX HEX HEX HEX HEX ')'
        ;

BOOL    : 'true' | 'false';
HEX     : ('A'..'F'|'0'..'9');

VAR     : ('a'..'z')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;
STR     : ('a'..'z'|'A'..'Z'|'0'..'9')+;
SET     :'=';
AND     : '&&';
OR      : '||';
NOT     : '!';
EQUAL   : '==';
NEQUAL  : '!=';
GET     : '>';
LET     : '<';
GEQ     : '>=';
LEQ     : '<=';
MOD     : '%';
POW     : '^';
MUL     : '*';
DIV     : '/';
ADD     : '+';
SUB     : '-';


