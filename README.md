# Open Roberta: Getting started.
***


### Introduction: ###
After a fresh git clone you get the **robertalab** project folder.
It includes everything you need to setup and extend your own browser programming environment. License information is available in the **docs** folder.

###### Things you need on your computer: ######
Basic toolset: Java 1.7, Maven, Git, Browser


Please also check our [wiki](http://wiki.open-roberta.org) for a detailed install instruction, development procedure, coding conventions and further reading. Please also checkout our project [issue tracker](http://jira.open-roberta.org).

###### Fast installation (maven is taking care): ######
***

Step 1) Compilation

***


``$ cd /OpenRobertaParent ``

``$ mvn clean install  ``

Get a coffee! Might take a couple of minutes.

***

A successful build looks like:

``[INFO] ---------------------------------------``


``[INFO] Reactor Summary:``

 
``[INFO] RobertaParent ..................SUCCESS``


``[INFO] OpenRobertaShared ..............SUCCESS``

``[INFO] OpenRobertaServer ..............SUCCESS``

``[INFO] OpenRobertaRuntime .............SUCCESS``

``[INFO] ---------------------------------------``

``[INFO] BUILD SUCCESS``

***

Step 2) Starting your own server instance.

***

``$ cd ..``

``$ cd OpenRobertaServer``

``$ java -jar target/OpenRobertaServer-1.0.0-SNAPSHOT.jar``

***

###### Accessing your programming environment: ######
Start your browser at:

`` http://localhost:1999``

***

That's it!

***