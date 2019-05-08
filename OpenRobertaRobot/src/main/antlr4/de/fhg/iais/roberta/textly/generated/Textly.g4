grammar Textly;

expr	:	(ADD | SUB) expr                                 # Unary
        |   expr (MUL | DIV ) expr                       # Binary
        |   expr (ADD | SUB) expr                        # Binary
        |   expr EQUAL expr                              # Binary
        |   NOT expr                                     # Unary
        |   expr AND expr                                # Binary
        |   expr OR expr                                 # Binary
        |   VAR                                          # VarName
        |   INT                                          # IntConst
        |   '(' expr ')'                                 # Parentheses
        ;

INT	    :	('0'..'9')+;

VAR	    :   ('a'..'z')('a'..'z''0'..'9')*;

NEWLINE	:	'\r'? '\n' -> skip;
WS		:	(' '|'\t')+ -> skip;

AND     :   '&&';
OR      :   '||';
NOT     :   '!';
EQUAL   :   '==';
MUL     :   '*';
DIV     :   '/';
ADD     :   '+';
SUB     :   '-';
SEMI    :   ';';
ASSIGN  :   ':=';