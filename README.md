Open Roberta Lab
================

### Introduction

The source of the OpenRoberta Lab is stored in the Github repository '[openroberta-lab](https://github.com/OpenRoberta/openroberta-lab)'.

The steps below explain how to get started with the sources. If you just want to run the server locally, please have a look into
the [Wiki - Installation](https://github.com/OpenRoberta/openroberta-lab/wiki/Installation). If you want to contribute, please get in touch with us,
see [Wiki - Community](https://github.com/OpenRoberta/openroberta-lab/wiki/Community), before you start.

After a fresh git clone you get the **openroberta-lab** project folder. It includes almost everything you need to setup and extend your own openrobertalab server.
License information is available in the **docs** folder.

Things you need on your computer:

* Java JDK >= 1.8 (e.g. `openjdk-11-jdk` on Ubuntu) and JAVA JDK <= 13.0.2
* Maven >= 3.2
* Git
* Web browser

If you would like your local server to compile code for the different systems, you need to install additional software (crosscompilers, libraries, ...):

To "install" directly downloaded compilers on Linux systems, extract them to a folder of your choice (e.g. `/opt/compilers/`) and add the `bin` folder to 
your `PATH`, e.g. with `echo export PATH="$PATH:<path-to-the-compiler-folder>/bin" >> ~/.profile`.

on Ubuntu:
* Arduino based robots
  * `sudo apt-get install libusb-0.1-4`
  * `sudo apt-get install binutils-avr gdb-avr avrdude`
  * install [avr-gcc](http://downloads.arduino.cc/tools/avr-gcc-7.3.0-atmel3.6.1-arduino5-x86_64-pc-linux-gnu.tar.bz2)
* NXT
  * `sudo apt-get install nbc`
* Calliope
  * `sudo apt-get install srecord libssl-dev`
  * install the latest [gcc-arm-none-eabi](https://developer.arm.com/tools-and-software/open-source-software/developer-tools/gnu-toolchain/gnu-rm/downloads)
* micro:bit
  * `pip install uflash` (to install pip run `sudo apt install` with `python-pip` on Ubuntu 18.04 and `python3-pip` on 20.04)
* EV3 c4ev3
  * `sudo apt-get install g++-arm-linux-gnueabi`
* Edison
  * `sudo apt-get python` (Python 2 is needed, it is called `python` for Ubuntu 18.04 and `python2` for 20.04)
* Bionics4Education
  * `sudo apt-get python-serial` (`python3-serial` for Ubuntu 20.04)
  * install [xtensa-esp32-elf](https://dl.espressif.com/dl/xtensa-esp32-elf-linux64-1.22.0-61-gab8375a-5.2.0.tar.gz)
 
on Windows:
* Arduino based robots
  * install [avr-gcc](http://downloads.arduino.cc/tools/avr-gcc-7.3.0-atmel3.6.1-arduino5-i686-w64-mingw32.zip)
* Calliope
  * install [gcc-arm-none-eabi](https://developer.arm.com/tools-and-software/open-source-software/developer-tools/gnu-toolchain/gnu-rm/downloads)
  * install [srecord](http://srecord.sourceforge.net/)
* micro:bit
  * install Python 3
  * `pip install uflash`
* Edison
  * install Python 2
* Bionics4Education
  * install [xtensa-esp32-elf with ESP-IDF Tools](https://docs.espressif.com/projects/esp-idf/en/latest/esp32/get-started/windows-setup.html)
  
The cross-compiler needs resources to work properly (header files, libraries, ...). These resources change little over time and are stored in the '[ora-cc-rsc](https://github.com/OpenRoberta/ora-cc-rsc)' repository.

Please clone that directory and build it using `mvn clean install`. When the openroberta-lab server is started, you have to supply the path to these resources (see below). If the resources are not available,
everything works fine (writing programs, import, export, creating accounts, etc.), but running programs on real robots doesn't work, because the cross-compiler will fail.

Please also check our wiki for detailed installation instructions, development procedure, coding conventions and further reading. We also use the Github issue tracking system.
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
    [INFO] Total time:  03:24 min
    [INFO] Finished at: 2020-09-08T14:13:10+02:00
    [INFO] ------------------------------------------------------------------------
    
#### Step 2: Make sure you have a database
If you have a fresh clone of the server, make sure that the OpenRobertaServer folder has a subfolder **db-embedded** with the database inside. If you don't have a database, you can create an empty database with

    ./ora.sh create-empty-db
    
If you try to create a new database, but one exists, the old one is *not* changed and the command has no effect. The new database is found in the folder **OpenRobertaServer/db-embedded**.

#### Step 3: Starting your own server instance using a Unix-like shell (on either lin* or win*).

    ./ora.sh [-oraccrsc <optional-path-to-crosscompiler-resources, defaults-to '../ora-cc-rsc'>] start-from-git

#### Step 4: Accessing your openroberta installation

Start your browser at [http://localhost:1999](http://localhost:1999) That's it!

### Creating an installation outside of the git repo

Often you want to run an openroberta installation of a fixed version for a long time. This is easy. Let's assume, that you want to use the (non-existing) directory /data/my-openroberta.

    mvn clean install                          # generate the server in the git repo
    ./ora.sh export /data/my-openroberta gzip  # export to the target directory. gzip compresses the static web resources fpr better performance.
    cd /data/my-openroberta
    ./admin.sh create-empty-db                 # create an empty db at ./db-server - of course you may copy an old database to that location
    ./admin.sh start-server                    # spawns two processes: a database server and the openroberta jetty server

The following administrative commands are useful:

    ./admin.sh backup ./admin/dbBackup   # create a backup in the folder given as parameter. Copy the tgz-file to another machine (cron will automate everything)
    ./admin.sh shutdown                  # shutdown of the database. The openroberta jetty server continues to run, but database access will fail, of course.
                                         # get the pid of the server using **ps** and execute a **kill <pid>** to stop the server
    ./admin.sh sqlgui                    # access the database concurrently with the server. Be careful not to block the server, e.g. by an update without commit
                                         # the database server must be running, of course.
    ./admin.sh sqlguiEmbedded            # if the database server is stopped, this allows to access the database. The access is exclusive. Terminate the sql client
                                         # before you start the openroberta installation again.

This kind of installation is useful for small productive systems (e.g. an a RaspberryPi). Large productive systems,
* if you want to run many servers (maybe of different versions) concurrently,
* if you want to use continous deployment based on incoming git commits
* ...

needs more support as available with the scripts **ora.sh** and **admin.sh**. We deploy our openroberta installation using docker. It is described in detail in file **Docker/README.md**. Inside the
**Docker** directory everything is contained which is needed to create images and start them. You can try it. If there are problems, you can contact us.

### Remarks about the database

OpenRoberta needs a database to store user accounts, programs, etc. It uses HyperSQL. Other database systems (MySql, Postgres, ..., oracle, db2, ...) would work, too. There are no
special requirements on the database, that would exclude one. Only a Hibernate binding and a full transaction support is needed.

As described above either **db-embedded** or **db-server** is the name of the directory, in which the database files reside. Sometimes the database must be upgraded, e.g. new tables or
new columns for new features. The java class **DbUpgrader** is responsible for detecting the need for an upgrade and executing the upgrade (once and only once) when the database is openend
at server startup. If a database needs more than one upgrade, this is no problem. They are executed one after the other.

There is _no_ need for the developer to care about database upgrades. But note, that is _not_ possible to upgrade databases with versions before **3.1.0**. As this a very old version, this should
never be necessary. If this is needed, please contact the OpenRoberta team.

### Importing the Project

You can also import the project into IDE's such as [Eclipse](https://github.com/OpenRoberta/openroberta-lab/wiki/Importing-into-Eclipse) and [IntelliJ](https://github.com/OpenRoberta/openroberta-lab/wiki/Importing-into-IntelliJ)!

### Development notes

You can follow the test status [here](https://travis-ci.org/OpenRoberta/).

Development happens in the [develop](https://github.com/OpenRoberta/openroberta-lab/tree/develop) branch. Please sent PRs against that branch.

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
  * javascript resources for [blockly](https://developers.google.com/blockly)
  * controller and models written in Javascript, which implement the GUI

To run tests, use `mvn test`. Running `mvn clean install` will make a stable, reproducible build with all unit tests executed.

To run the integration tests you have to supply an additional flag: `mvn clean install -PrunIT`.
For these you need an environment variable `robot_crosscompiler_resourcebase` pointing to your `ora-cc-rsc` directory.

#### Some Frameworks used

We use Blockly, it is located in a separate repository. The build of the blockly is only done in the OpenRoberta/Blockly project and then copied to the OpenRobertaServer/staticResources. You can not build Blockly in OpenRobertaServer project directly.

We use BrowserStack for Cross-Browser Testing

[<img src="https://github.com/OpenRoberta/openroberta-lab/blob/develop/Resources/images/blockly.png" width="100">](https://developers.google.com/blockly/)
[<img src="https://github.com/OpenRoberta/openroberta-lab/blob/develop/Resources/images/browserstack-logo-600x315.png" width="150">](http://browserstack.com/)

#### Have a look at the notes in LICENCE and NOTICE

Build status:

* master [![master](https://travis-ci.org/OpenRoberta/openroberta-lab.svg?branch=master)](https://travis-ci.org/OpenRoberta/openroberta-lab/builds)
* develop [![develop](https://travis-ci.org/OpenRoberta/openroberta-lab.svg?branch=develop)](https://travis-ci.org/OpenRoberta/openroberta-lab/builds)

