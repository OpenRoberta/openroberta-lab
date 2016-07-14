Open Roberta Lab
================

Build status:

* master [![master](https://travis-ci.org/OpenRoberta/robertalab.svg?branch=master)](https://travis-ci.org/OpenRoberta/robertalab/builds)
* develop [![develop](https://travis-ci.org/OpenRoberta/robertalab.svg?branch=develop)](https://travis-ci.org/OpenRoberta/robertalab/builds)

### Introduction

The steps below explain how to get started with your own deployment of the
OpenRoberta programming environment.

After a fresh git clone you get the **robertalab** project folder.
It includes everything you need to setup and extend your own browser programming
environment. License information is available in the **docs** folder.

Things you need on your computer:

* Java 1.7
* Maven >= 3.2
* Phantomjs
* Git
* Web browser

Please also check our [wiki](http://wiki.open-roberta.org) for a detailed install
instruction, development procedure, coding conventions and further reading. Please
also checkout our project [issue tracker](http://jira.open-roberta.org).

### Fast installation with maven

#### Step 1: Clone the repository and compile

    git clone git://github.com/OpenRoberta/robertalab.git
    cd robertalab/OpenRobertaParent
    mvn clean install

Get a coffee! Might take a couple of minutes.

A successful build looks like:


    [INFO] ------------------------------------------------------------------------
    [INFO] Reactor Summary:
    [INFO] 
    [INFO] RobertaParent ...................................... SUCCESS [  1.083 s]
    [INFO] Resources .......................................... SUCCESS [  0.032 s]
    [INFO] OpenRobertaRobot ................................... SUCCESS [ 15.718 s]
    [INFO] RobotSIM ........................................... SUCCESS [  3.240 s]
    [INFO] RobotEV3 ........................................... SUCCESS [ 12.599 s]
    [INFO] RobotNXT ........................................... SUCCESS [ 13.902 s]
    [INFO] OpenRobertaServer .................................. SUCCESS [ 46.404 s]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS

#### Step 2a: Starting your own server instance using a unix-like shell (on either lin* or win*).

    cd .. # return to the root folder
    ./ora.sh --start-server # start the server using default properties

You can also run `./ora.sh --help` for more options.

#### Step 2b: Starting your own server instance without using the shell script

    cd ../OpenRobertaServer # go to the folder of the server resources and the database
    java -cp target/resources/\* de.fhg.iais.roberta.main.ServerStarter --properties  --ip 0.0.0.0 --port 1999

#### Step 3: Accessing your programming environment

Start your browser at: http://localhost:1999


That's it!

### Development notes

You can follow the test status on https://travis-ci.org/OpenRoberta/.

#### Blockly

We are using Blockly as a submodule. The build of the blockly is only done in
the OpenRoberta/Blockly project and then copied to the
OpenRobertaServer/StaticResources. You can not build Blockly in
OpenRobertaServer project directly.

