update PROGRAM set ROBOT_ID = (select ID from ROBOT where NAME='botnroll') where ROBOT_ID = (select ID from ROBOT where NAME='ardu');
update CONFIGURATION set ROBOT_ID = (select ID from ROBOT where NAME='botnroll') where ROBOT_ID = (select ID from ROBOT where NAME='ardu');
commit;

update PROGRAM set PROGRAM_TEXT = replace(PROGRAM_TEXT, '"ardu"', '"botnroll"') where ROBOT_ID = (select ID from ROBOT where NAME='botnroll');
commit;

update CONFIGURATION_DATA set CONFIGURATION_TEXT = replace(CONFIGURATION_TEXT, '"ardu"', '"botnroll"')
       where CONFIGURATION_HASH in (select CONFIGURATION_HASH from CONFIGURATION where ROBOT_ID = (select ID from ROBOT where NAME='botnroll'));  
commit;