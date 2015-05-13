@ECHO OFF
pscp -scp -batch -l root -pw "" OpenRobertaRuntime.jar OpenRobertaShared.jar json.jar 10.0.1.1:/home/roberta/lib
pscp -scp -batch -l root -pw "" EV3Menu.jar 10.0.1.1:/home/root/lejos/bin/utils
