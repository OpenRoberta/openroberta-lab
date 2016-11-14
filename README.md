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
    [INFO] RobertaParent ...................................... SUCCESS [  3.858 s]
    [INFO] Resources .......................................... SUCCESS [  0.036 s]
    [INFO] OpenRobertaRobot ................................... SUCCESS [ 21.395 s]
    [INFO] RobotSIM ........................................... SUCCESS [  6.521 s]
    [INFO] RobotEV3 ........................................... SUCCESS [ 20.394 s]
    [INFO] RobotNXT ........................................... SUCCESS [ 19.531 s]
    [INFO] RobotArdu .......................................... SUCCESS [ 19.677 s]
    [INFO] OpenRobertaServer .................................. SUCCESS [ 33.178 s]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    
#### Step 2: Make sure you have a database
If you have a fresh clone of the server, make sure that the OpenRobertaServer folder has a subfolder **db** with the database inside. You can either 
* copy the folder **dbBase** (also in OpenRobertaServer) under the name **db**
* or create an empty an empty database with

    ./ora.sh --createemptydb OpenRoberta/db/openroberta-db (from the root folder)
    
If you update the server with git pull, your database will not be changed. 

#### Step 3a: Starting your own server instance using a unix-like shell (on either lin* or win*).

    cd .. # return to the root folder
    ./ora.sh --start-server # start the server using default properties

You can also run `./ora.sh --help` for more options.

#### Step 3b: Starting your own server instance without using the shell script

    cd ../OpenRobertaServer # go to the folder of the server resources and the database
    java -cp target/resources/\* de.fhg.iais.roberta.main.ServerStarter --properties  --ip 0.0.0.0 --port 1999

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

