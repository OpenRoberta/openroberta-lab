grammar Ev3Configuration;

conf    	: 'robot' 'ev3' NAME '{' sizes? sensors? actors? '}'
        	;

sizes		: 'size' '{' 'wheel' 'diameter' RATIONAL 'cm' ';'? 'track' 'width' RATIONAL 'cm' ';'? '}' ';'?
			;

sensors     : 'sensor' 'port' '{' sdecl* '}' ';'?
            ;

actors      : 'actor' 'port' '{' adecl* '}' ';'?
            ;

sdecl   	: SENSORPORT ':' SENSOR ';'?
;

adecl   	: ACTORPORT ':' actor ';'?
;

actor		: MOTORKIND  'motor' ','? motorSpec
			;
			
motorSpec	: REGULATION ','? ROTATION (','? LEFTORRIGHT)?
			;
			
SENSORPORT	: [1234];

SENSOR 		: 'color' | 'touch' | 'ultrasonic' | 'infrared' | 'gyro'
            ;
			
ACTORPORT	: [ABCD];

MOTORKIND	: 'large' | 'middle';

ROTATION	: 'forward' | 'backward';

LEFTORRIGHT : 'left'| 'right';

REGULATION	: 'regulated' | 'unregulated';

NAME	    : [a-zA-Z][a-zA-Z0-9_-]*;

RATIONAL	: '+'?[0-9]+('.'[0-9])?
			;

NEWLINE	:	'\r'? '\n' -> skip;
WS		:	(' '|'\t')+ -> skip;