@ECHO OFF
pscp -scp -hostkey 6c:59:d2:e4:e6:78:fd:51:5d:be:85:a8:ea:a1:e5:7c -q -batch -l root -pw "" OpenRobertaRuntime.jar OpenRobertaShared.jar json.jar 10.0.1.1:/home/roberta/lib
pscp -scp -hostkey 6c:59:d2:e4:e6:78:fd:51:5d:be:85:a8:ea:a1:e5:7c -q -batch -l root -pw "" EV3Menu.jar 10.0.1.1:/home/root/lejos/bin/utils

