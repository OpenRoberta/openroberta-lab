grammar Textly;

stmtl   :   '{' stmt* '}'                                # StmtList
        ;
        
stmt    :   expr SEMI                                    # ExprStmt
        |   ifBase SEMI                                  # IfStmt
        |   repeat SEMI                                  # RepeatStmt
        |   assign SEMI                                  # AssignStmt
        ;
        
ifBase  :   'if' '(' expr ')' stmtl ifElse?
        ;
        
ifElse  :   'else' stmtl
        |   'else' ifBase
        ;
        
repeat  :   'repeat' expr 'times' stmtl
        ;
        
assign  :   VAR ASSIGN expr
        ;
        
expr	:	(ADD | SUB) expr                             # UnaryAddSub
        |   expr (MUL | DIV ) expr                       # MulDiv
        |   expr (ADD | SUB) expr                        # AddSub
        |   expr EQUAL expr                              # Equal
        |   NOT expr                                     # UnaryNot
        |   expr AND expr                                # And
        |   expr OR expr                                 # Or
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