Getting startet with OpenRoberta:
----------------------------------

Project base is: /OpenRobertaParent
This folder inlcudes the main parent pom.xml file which is required by all other 
submodules. 

Compile and run (command line):
STEP 1):
$ cd OpenRobertaParent
$ mvn install

A successfull build includes the following modules and looks like:

[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] RobertaParent ..................................... SUCCESS
[INFO] OpenRobertaShared ................................. SUCCESS
[INFO] OpenRobertaServer ................................. SUCCESS
[INFO] OpenRobertaRuntime ................................ SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------

STEP 2):
To start working with your own OpenRoberta instance, start the OpenRobertaServer
after your successful build. The server project is located in the
'OpenRobertaServer' folder.

$ cd ..
$ cd OpenRobertaServer
$ java -jar target/OpenRobertaServer-0.0.1-SNAPSHOT.jar


STEP 3):
Start your browser at:
http://localhost:1999
Done!


Please also check the wiki manual page.

Project wiki:
http://wiki.open-roberta.org

Please file bug reports to:
http://jira.open-roberta.org
