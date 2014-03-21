grammar Textly;

expr	:	unary                    # SingleUnary
        |   expr (MUL | DIV) expr    # MulDiv
        |   expr (ADD | SUB) expr    # AddSub
        |   VAR                      # VarName
        |   INT                      # IntConst
        |   '(' expr ')'             # Parentheses
        ;
        
unary   :  (ADD | SUB) INT          # UnaryConst
        |  (ADD | SUB) '(' expr ')' # UnaryExpr
        ;

INT	    :	('0'..'9')+;

VAR	    :   ('a'..'z')('a'..'z''0'..'9')*;

NEWLINE	:	'\r'? '\n' -> skip;
WS		:	(' '|'\t')+ -> skip;

MUL     :   '*';
DIV     :   '/';
ADD     :   '+';
SUB     :   '-';