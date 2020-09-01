insert into ROBOT
( NAME, CREATED, TAGS, ICON_NUMBER )
values('ev3',
now,
 '', 0
 );
commit;

insert into USER
(USERGROUP_ID, ACCOUNT, PASSWORD, EMAIL, ROLE, CREATED, LAST_LOGIN, TAGS, USER_NAME)
values (null, 'Roberta','f17a0084220e822e:313c4eda282166163f78cd0b13da3b66f5ed6a0e','','TEACHER',now ,now ,'','Roberta Roboter'
);
commit;

insert into USER
(USERGROUP_ID, ACCOUNT, PASSWORD, EMAIL, ROLE, CREATED, LAST_LOGIN, TAGS, USER_NAME)
values (null, 'Gallery','f17a0084220e822e:313c4eda282166163f78cd0b13da3b66f5ed6a0e','','TEACHER',now ,now ,'','The Gallery'
);
commit;

insert into USER
(USERGROUP_ID, ACCOUNT, PASSWORD, EMAIL, ROLE, CREATED, LAST_LOGIN, TAGS, USER_NAME)
values (null, 'testUser','f17a0084220e822e:313c4eda282166163f78cd0b13da3b66f5ed6a0e','','TEACHER',now ,now ,'','Test User'
);
commit;

insert into PROGRAM
( NAME, OWNER_ID, ROBOT_ID, AUTHOR_ID, PROGRAM_TEXT, CREATED, LAST_CHANGED, LAST_CHECKED, LAST_ERRORFREE, VIEWED, TAGS, ICON_NUMBER )
values('TestProg',3,42,3,'<block_set xmlns="http://de.fhg.iais.roberta.blockly"><instance x="370" y="50"><block type="robControls_start" id="149" intask="true" deletable="false"><mutation declare="false"></mutation></block><block type="robActions_motorDiff_on" id="168" inline="false" intask="true"><field name="DIRECTION">FOREWARD</field><value name="POWER"><block type="math_number" id="169" intask="true"><field name="NUM">30</field></block></value></block><block type="robControls_wait_for" id="189" inline="false" intask="true"><value name="WAIT0"><block type="logic_compare" id="190" inline="true" intask="true"><mutation operator_range="COLOUR"></mutation><field name="OP">EQ</field><value name="A"><block type="sim_getSample" id="191" intask="true" deletable="false" movable="false"><mutation input="COLOUR_COLOUR"></mutation><field name="SENSORTYPE">COLOUR_COLOUR</field><field name="SENSORPORT">3</field></block></value><value name="B"><block type="robColour_picker" id="197" intask="true"><field name="COLOUR">#000000</field></block></value></block></value></block></instance></block_set>',
now, now, now, now,
0,'', 0
 );
commit;