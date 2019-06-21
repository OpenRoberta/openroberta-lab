grammar Exprly;

expression: math
        | bool
        | string
        | color
        | connection
        | list
        | assingment
        | function
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


list:  list_math
    | list_bool
    | list_string
    | list_color
    | list_connection
    ;
    
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

function:  get_sub_f									# getSubFunction
		| index_f										# index
		| lenght_empty_f								# lenghtEmpty
		| list_get_index								# listGetIndex
		| list_repeat_f									# listRepeat
		| list_set_index_f								# listSetIndex
		| math_constrain_f								# mathConstraint
		| math_num_prop_f								# mathNumProp
		| math_on_list_f								# mathOnList
		| math_power_f									# mathPower
		| math_random_float_f							# mathRandomFloat
		| math_random_int_f								# mathRandomInt
		| math_single_f									# mathSingle
		| text_join_f									# textJoin
		| text_print_f									# textPrint
		;

arg_list: (expression ',')* expression 
		;
		
get_sub_f: 'getSubFunction' '(' ')'
		;
		
index_f: 'indexOfFunction' '(' INDEX ',' arg_list ')'
		;
		
lenght_empty_f: 'lenghtOfIsEmptyFunction' '(' FNAME ',' arg_list ')'
		;
		
list_get_index: 'listGetIndex' '(' LELEMOP ',' INDEX ',' arg_list ',' STR ')'
		;
		
list_repeat_f: 'listRepeat' '(' BTYPE ',' arg_list ')'
		;
		
list_set_index_f: 'listSetIndex' '(' LELEMOP ',' INDEX ',' arg_list ')'
		;
		
math_constrain_f: 'mathConstrain' '(' arg_list ')'
		;
		
math_num_prop_f: 'mathNumProp' '(' FNAME ',' arg_list ')'
		;
		
math_on_list_f: 'mathOnList' '(' FNAME ',' arg_list ')'
		;
		
math_power_f: 'mathPower' '('FNAME ',' arg_list')'
		;
		
math_random_float_f: 'mathRandomFloat' '(' FNAME ',' arg_list')'
		;
		
math_random_int_f: 'mathRandomInt' '(' arg_list ')'
		;
		
math_single_f: 'mathSingle' '(' FNAME ',' arg_list ')'
		;
		
text_join_f: 'textJoin' '(' list ')'
		;
		
text_print_f: 'textPrint' '(' arg_list ')'
		;


// LEXER RULES
NEWLINE	:	'\r'? '\n' -> skip;

WS		:	(' '|'\t')+ -> skip;

INT     :	('0'..'9')+;

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

BTYPE	:  [Aa][Nn][Yy]
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
		 
CONST   :  [Gg][Oo][Ll][Dd][Ee][Nn]'_'[Rr][Aa][Tt][Ii][Oo] 
        |  [Pp][Ii] 
        |  [Ee] 
        |  [Ss][Qq][Rr][Tt] '2' 
        |  [Ss][Qq][Rr][Tt] '1_2' 
        |  [Ii][Nn][Ff][Ii][Nn][Ii][Tt][Yy] 
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

