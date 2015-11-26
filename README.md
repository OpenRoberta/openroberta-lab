# Open Roberta: Getting started with an own deployment of the OpenRoberta programming environment.
***


### Introduction: ###
After a fresh git clone you get the **robertalab** project folder.
It includes everything you need to setup and extend your own browser programming environment. License information is available in the **docs** folder.

###### Things you need on your computer: ######
Basic toolset: Java 1.7, Maven, Git, Browser, Phantomjs


Please also check our [wiki](http://wiki.open-roberta.org) for a detailed install instruction, development procedure, coding conventions and further reading. Please also checkout our project [issue tracker](http://jira.open-roberta.org).

###### Fast installation (maven is taking care): ######
***

Step 1) Compilation

***


``$ cd /OpenRobertaParent # move from the root folder to the folder of the (maven) parent project``

``$ mvn clean install  ``

Get a coffee! Might take a couple of minutes.

***

A successful build looks like:

``[INFO] ---------------------------------------``
``[INFO] Reactor Summary:``
``[INFO] RobertaParent ..................SUCCESS``
``[INFO] Resources ......................SUCCESS``
``[INFO] OpenRobertaShared ..............SUCCESS``
``[INFO] OpenRobertaRuntime .............SUCCESS``
``[INFO] EV3Menu ........................SUCCESS``
``[INFO] OpenRobertaRobot ...............SUCCESS``
``[INFO] OpenRobertaServer ..............SUCCESS``
``[INFO] OpenRobertaUSB .................SUCCESS``
``[INFO] ---------------------------------------``
``[INFO] BUILD SUCCESS``

***

Step 2a) Starting your own server instance, if a unix-like shell is available (on either lin* or win*).

***

``$ cd .. # return to the root folder``


``$ ./ora.sh --start # start the server, using default properties. Use --help for more options.``

***

***

Step 2b) Starting your own server instance, without using the shell script

***

``$ cd .. # return to the root folder``

``$ cd OpenRobertaServer # go to the folder of the server resources and the database``

``java -cp target/resources/\* de.fhg.iais.roberta.main.ServerStarter --properties  --ip 0.0.0.0 --port 1999``

***

###### Accessing your programming environment: ######
Start your browser at:

`` http://localhost:1999``

***

That's it!

***
