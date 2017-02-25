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
* Git
* Web browser

If you would like the server to compile code for the different systems, you need to install additional software:

for linux:
* Arduino based robots
 * sudo apt-get install libusb-0.1-4
 * sudo apt-get install gcc-avr binutils-avr gdb-avr avr-libc avrdude
* NXT
 * sudo apt-get install nbc
* Calliope
 * sudo apt-get install gcc-arm-none-eabi srecord libssl-dev
* micro:bit
 * pip install uflash
 
for windows:
* Calliope
 * install gcc-arm-none-eabi (see: http://gnuarmeclipse.github.io/toolchain/install/)
 * install srecord (see: http://srecord.sourceforge.net/)
* micro:bit
 * install python
 * pip install uflash


Please also check our wiki for detailed installation instructions, development procedure, coding conventions and further reading. We also use the github issue tracking system. Please file issues in the main project **robertalab**.


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
    [INFO] RobertaParent ...................................... SUCCESS [  1.323 s]
    [INFO] Resources .......................................... SUCCESS [  0.033 s]
    [INFO] OpenRobertaRobot ................................... SUCCESS [ 16.520 s]
    [INFO] RobotEV3 ........................................... SUCCESS [ 19.235 s]
    [INFO] RobotNXT ........................................... SUCCESS [ 17.004 s]
    [INFO] RobotArdu .......................................... SUCCESS [ 17.060 s]
    [INFO] RobotNAO ........................................... SUCCESS [  7.106 s]
    [INFO] RobotMbed .......................................... SUCCESS [  7.271 s]
    [INFO] OpenRobertaServer .................................. SUCCESS [01:52 min]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 03:18 min
    [INFO] Finished at: 2016-12-21T16:52:55+01:00
    [INFO] Final Memory: 52M/414M
    [INFO] ------------------------------------------------------------------------
    
    
#### Step 2: Make sure you have a database
If you have a fresh clone of the server, make sure that the OpenRobertaServer folder has a subfolder **db** with the database inside. You can either 
* copy the folder **dbBase** (also in OpenRobertaServer) under the name **db**
* or create an empty an empty database with

    ./ora.sh --createemptydb OpenRobertaServer/db/openroberta-db (from the root folder)
    
If you update the server with git pull, your database will not be changed. 

#### Step 3: Starting your own server instance using a unix-like shell (on either lin* or win*).

    cd .. # return to the root folder
    ./ora.sh --start-from-git # start the server using default properties

You can also run `./ora.sh --help` for more options.

#### Step 4: Accessing your programming environment

Start your browser at: http://localhost:1999


That's it!

### Development notes

You can follow the test status on https://travis-ci.org/OpenRoberta/.

Development happens in the 'develop# branch. Please sent PRs against that
branch.

    git clone git://github.com/OpenRoberta/robertalab.git
    cd robertalab
    git checkout -b develop origin/develop

#### Blockly

We are using Blockly as a submodule. The build of the blockly is only done in
the OpenRoberta/Blockly project and then copied to the
OpenRobertaServer/staticResources. You can not build Blockly in
OpenRobertaServer project directly.

