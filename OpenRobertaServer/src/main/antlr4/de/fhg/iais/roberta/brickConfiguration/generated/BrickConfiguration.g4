grammar BrickConfiguration;

connectorl	: 'brick' NAME '{' connector* '}'					# ConnectorList
        	;
        		
connector	: 'sensor' 'port' SENSORPORT ATTACHSENSOR ';'		# SensorStmt
        	| 'actor'  'port' ACTORPORT  attachActor ';'		# ActorStmt
			;
        
ATTACHSENSOR : 'color' | 'touch' | 'ultrasonic' | 'infrared'
            | 'rotation' | 'gyro'
            | 'Farbe' | 'Berührung' | 'Ultraschall'
            | 'Infrarot' | 'Drehung'
            ;
			
attachActor	: ROTATION LEFTORRIGHT MOTORTYPE 'motor' REGULATION?			# Motor
			;
			
SENSORPORT	: [1234];

ACTORPORT	: [ABCD];

ROTATION	: 'off' | 'on' | 'forward' | 'backward' | 'vorwärts' | 'rückwärts';

LEFTORRIGHT : 'left'| 'right' | 'links' | 'rechts';

MOTORTYPE	: 'middle' | 'large';

REGULATION	: 'regulated' | 'unregulated';

NAME	    : [a-zA-Z][a-zA-Z0-9_-]*;

NEWLINE	:	'\r'? '\n' -> skip;
WS		:	(' '|'\t')+ -> skip;