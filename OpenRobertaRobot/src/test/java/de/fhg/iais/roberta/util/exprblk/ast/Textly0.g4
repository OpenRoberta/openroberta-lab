grammar Textly0;

expression: expr;

expr	:	op=(ADD | SUB) expr                             # Unary
        |   expr op=(MUL | DIV ) expr                       # Binary
        |   expr op=(ADD | SUB) expr                        # Binary
        |   expr op=EQUAL expr                              # Binary
        |   op=NOT expr                                     # Unary
        |   expr op=AND expr                                # Binary
        |   expr op=OR expr                                 # Binary
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