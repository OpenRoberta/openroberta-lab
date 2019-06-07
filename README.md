Open Roberta Lab
================

### please read the file IMPORTANT_CHANGE.MD

escpecially if you have forked or cloned the robertalab repository in the past!


### Introduction

The source of the openroberta lab are stored in the Github repository `https://github.com/OpenRoberta/openroberta-lab`.

The steps below explain how to get started with the sources. If you just want to run the server locally, please have a look into
the [wiki - installation](https://github.com/OpenRoberta/openroberta-lab/wiki/Installation). If you want to contribute, please get in touch with us,
see [wiki - Community](https://github.com/OpenRoberta/openroberta-lab/wiki/Community) before you start.

After a fresh git clone you get the **openroberta-lab** project folder. It includes almost everything you need to setup and extend your own openrobertalab server.
License information is available in the **docs** folder.

Things you need on your computer:

* Java 1.8
* Maven >= 3.2
* Git
* Web browser

If you would like your local server to compile code for the different systems, you need to install additional software (crosscompiler, ...):

on linux:
* Arduino based robots
  * sudo apt-get install libusb-0.1-4
  * sudo apt-get install gcc-avr binutils-avr gdb-avr avr-libc avrdude
* NXT
  * sudo apt-get install nbc
* Calliope
  * sudo apt-get install gcc-arm-none-eabi srecord libssl-dev
* micro:bit
  * pip install uflash
* EV3 (c4ev3)
  * sudo apt-get install arm-linux-gnueabi-gcc
 
on windows:
* Calliope
  * install gcc-arm-none-eabi (see: http://gnuarmeclipse.github.io/toolchain/install/)
  * install srecord (see: http://srecord.sourceforge.net/)
* micro:bit
  * install python
  * pip install uflash
  
The crossompiler need resources to work properly (header files, libraries, ...). These resources change little over time and are stored in a repository of its own:

    https://github.com/OpenRoberta/ora-cc-rsc.git

Please clone that directory. When the openrobertalab server is started, you have to supply the path to these resources (see below). If the resources are not available,
everything works fine (writing programs, import, export, creating accounts, etc.), but running programs on real robots doesn't work, because the crosscompiler will fail.

Please also check our wiki for detailed installation instructions, development procedure, coding conventions and further reading. We also use the github issue tracking system.
Please file issues in the main project **openroberta-lab**.

### Fast installation with maven

#### Step 1: Clone the repository and compile

    git clone https://github.com/OpenRoberta/openroberta-lab.git # get the repository
    cd openroberta-lab                                           # cd into repository
    mvn clean install                                            # generate the server

Might take some time. The last lines of a successful build looks like:

    ...
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 02:16 min
    [INFO] Finished at: 2018-01-07T13:05:00+02:00
    [INFO] Final Memory: 60M/540M
    [INFO] ------------------------------------------------------------------------
    
#### Step 2: Make sure you have a database
If you have a fresh clone of the server, make sure that the OpenRobertaServer folder has a subfolder **db-x.y.z** with the database inside, where x.y.z is the current version from the server. The actual server version is found in the pom.xml of the OpenRobertaParent project. If you don't have a database, you can create an empty database with

    ./ora.sh create-empty-db

#### Step 3: Starting your own server instance using a unix-like shell (on either lin* or win*).

    ./ora.sh start-from-git [optional-path-to-crosscompiler-resources] # start the server using the default properties

You can also run `./ora.sh help` to see more commands for administration of the server. If you have Eclipse installed and

* the repositories base directory is imported as maven project (this will import all submodules automatically)
* a maven build in the repositories base directory has been executed and a database is created (see above!)
* a run configuration is created with
  * OpenRobertaServer as project
  * de.fhg.iais.roberta.main.ServerStarter as main class
  * and the program arguments are set to

        -d server.staticresources.dir=./staticResources
        -d robot.crosscompiler.resourcebase=[optional-path-to-crosscompiler-resources]
        -d database.parentdir=. -d server.tutorial.dir=../Resources/tutorial

* then the server can be started with this run configuration

#### Step 4: Accessing your programming environment

Start your browser at: http://localhost:1999

That's it!

### Development notes

You can follow the test status on https://travis-ci.org/OpenRoberta/.

Development happens in the `develop` branch. Please sent PRs against that branch.

    git clone https://github.com/OpenRoberta/openroberta-lab.git
    cd openroberta-lab
    git checkout develop
	
The project OpenRobertaServer contains the server logic, that accesses
* a database with Hibernate-based DAO objects
* plugins for various robots which are supported in OpenRoberta
* services for browser-based clients
* services for robots connected to the lab either by Wifi or USB

The server is made of
* an embedded jetty server exposing REST services
* the services are based on jersey
* JSON (sometimes XML or plain text) is used for data exchange between front, robots and server

Furthermore, the project OpenRobertaServer contains in directory staticResources for the browser client
* HTML and CSS
* Javascript libraries based on jquery and bootstrap for the frontend
  * assertions (DBC), ajax-based server calls (COMM), logging (LOG) and
  * javascript resources for blockly (see: http://code.google.com/p/blockly/)
  * controller and models written in Javascript, which implement the GUI

To run tests, use `mvn test`. Running `mvn clean install` will make a stable, reproducible build with all unit tests executed.

#### Blockly

We are using Blockly, it is located in a separate repository. The build of the blockly is only done in the OpenRoberta/Blockly project and then copied to the OpenRobertaServer/staticResources. You can not build Blockly in OpenRobertaServer project directly.

#### Have a look at the notes in LICENCE and NOTICE

Build status:

* master [![master](https://travis-ci.org/OpenRoberta/openroberta-lab.svg?branch=master)](https://travis-ci.org/OpenRoberta/openroberta-lab/builds)
* develop [![develop](https://travis-ci.org/OpenRoberta/openroberta-lab.svg?branch=develop)](https://travis-ci.org/OpenRoberta/openroberta-lab/builds)

