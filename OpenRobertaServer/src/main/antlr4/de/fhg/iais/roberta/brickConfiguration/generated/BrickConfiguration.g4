grammar BrickConfiguration;

conf    	: 'ev3' NAME '{' sizes connector* '}'
        	;

sizes		: 'wheel' 'diameter' RATIONAL 'cm' 'track' 'width' RATIONAL 'cm'
			;
			
connector	: 'sensor' 'port' SENSORPORT SENSOR   				# SensorStmt
        	| 'actor'  'port' ACTORPORT  actor    				# ActorStmt
			;
        
SENSOR 		: 'color' | 'touch' | 'ultrasonic' | 'infrared' | 'gyro'
            ;
			
actor		: MOTORKIND  'motor' '{' motorSpec '}'
			;
			
motorSpec	: REGULATION ROTATION LEFTORRIGHT?
			;
			
SENSORPORT	: [1234];

RATIONAL	: '+'?[0-9]+('.'[0-9])?
			;

MOTORKIND	: 'large' | 'middle';

ACTORPORT	: [ABCD];

ROTATION	: 'foreward' | 'backward';

LEFTORRIGHT : 'left'| 'right';

REGULATION	: 'regulated' | 'unregulated';

NAME	    : [a-zA-Z][a-zA-Z0-9_-]*;

NEWLINE	:	'\r'? '\n' -> skip;
WS		:	(' '|'\t')+ -> skip;