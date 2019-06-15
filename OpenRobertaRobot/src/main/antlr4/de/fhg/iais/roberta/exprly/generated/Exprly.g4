grammar Exprly;

expression: math
        | bool
        | string
        | color
        | connection
        | list_math
        | list_bool
        | list_string
        | list_color
        | list_connection
        | assingment
        ;

math	:	op=(ADD | SUB) math                          # Unary
		|   math op=MOD math							 # Binary
        |   math op=(MUL | DIV ) math                    # Binary
        |   math op=(ADD | SUB) math                     # Binary
        |   CONST                                        # MathConst
        |   VAR                                          # VarName
        |   INT                                          # IntConst
        |   '(' math ')'                                 # Parentheses
        ;

bool:       op=NOT bool                                  # UnaryB
        |   bool op=AND bool                             # BinaryB
        |   bool op=OR bool                              # BinaryB
		|   bool op=EQUAL bool               			 # BinaryB
		|   math op=EQUAL math							 # EqualityMath
		|   string op=EQUAL string						 # EqualityString
		|   color op=EQUAL color						 # EqualityColor
		|   connection op=EQUAL connection				 # EqualityConnection
		|   list_bool op=EQUAL list_bool               	 # EqualityBoolL
		|   list_math op=EQUAL list_math				 # EqualityMathL
		|   list_string op=EQUAL list_string			 # EqualityStringL
		|   list_color op=EQUAL list_color				 # EqualityColorL
		|   list_connection op=EQUAL list_connection	 # EqualityConnectionL
		|   bool op=NEQUAL bool               			 # BinaryB
		|   math op=NEQUAL math							 # NEqualityMath
		|   string op=NEQUAL string						 # NEqualityString
		|   color op=NEQUAL color						 # NEqualityColor
		|   connection op=NEQUAL connection				 # NEqualityConnection
		|   list_bool op=NEQUAL list_bool                # NEqualityBoolL
		|   list_math op=NEQUAL list_math				 # NEqualityMathL
		|   list_string op=NEQUAL list_string			 # NEqualityStringL
		|   list_color op=NEQUAL list_color				 # NEqualityColorL
		|   list_connection op=NEQUAL list_connection	 # NEqualityConnectionL
		|   math op=GET math							 # InEqualityMath
		|   math op=LET math							 # InEqualityMath
		|   math op=GEQ math							 # InEqualityMath
		|   math op=LEQ math							 # InEqualityMath
        |   BOOL                                         # BoolConstB
        |   VAR                                          # VarNameB
        |   '(' bool ')'                                 # ParenthesesB
        ;
        
string: 'String:' STR;

color:    COLOR                                                     # Col
        |  '(' r=INT ',' g=INT ',' b=INT ',' a=INT ')'              # RGB
        ;

connection: 'Connection:' STR ',' STR;

list_math:       '[' (math ',')* math ']'				 # ListM
		 ;

list_bool:       '[' (bool ',')* bool ']'				 # ListB
		 ;

list_string:     '[' (string ',')* string ']'			 # ListS
		   ;

list_color:      '[' (color ',')* color ']'			 # ListCol
		  ;

list_connection: '[' (connection ',')* connection ']'	 # ListCon
			   ;

assingment: 	  VAR ASSIGN math 							 # AssignM
			  |   VAR ASSIGN bool						 	 # AssignB
			  |   VAR ASSIGN string 					     # AssignS
			  |   VAR ASSIGN color 							 # AssignCol
			  |   VAR ASSIGN connection 					 # AssignCon
			  |   VAR ASSIGN list_math 						 # AssignML
			  |   VAR ASSIGN list_bool						 # AssignBL
			  |   VAR ASSIGN list_string 					 # AssignSL
			  |   VAR ASSIGN list_color 					 # AssignColL
			  |   VAR ASSIGN list_connection 				 # AssignConL
			  ;

NEWLINE	:	'\r'? '\n' -> skip;
WS		:	(' '|'\t')+ -> skip;

INT     :	('0'..'9')+;
CONST   :  [Gg][Oo][Ll][Dd][Ee][Nn]'_'[Rr][Aa][Tt][Ii][Oo] 
        |  [Pp][Ii] 
        |  [Ee] 
        |  [Ss][Qq][Rr][Tt] '2' 
        |  [Ss][Qq][Rr][Tt] '1_2' 
        |  [Ii][Nn][Ff][Ii][Nn][Ii][Tt][Yy] 
        ;
        
BOOL    :  [Tt][Rr][Uu][Ee] | [Ff][Aa][Ll][Ss][Ee];
COLOR   :  '#'HEX HEX HEX HEX HEX HEX;
HEX     :  ('A'..'F'|'a'..'f'|'0'..'9');

VAR	    :  ('a'..'z')('a'..'z''0'..'9')*;
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
MUL     :   '*';
DIV     :   '/';
ADD     :   '+';
SUB     :   '-';
SEMI    :   ';';
ASSIGN  :   ':=';

