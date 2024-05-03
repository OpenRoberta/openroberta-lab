Open Roberta Lab
================

[![Unit Test](https://github.com/OpenRoberta/openroberta-lab/actions/workflows/unit_test_triggered_by_develop_push.yml/badge.svg?branch=develop)](https://github.com/OpenRoberta/openroberta-lab/actions/workflows/unit_test.yml)
[![Integration Test (Nightly)](https://github.com/OpenRoberta/openroberta-lab/actions/workflows/integration_test_triggered_by_cron.yml/badge.svg?branch=develop&event=schedule)](https://github.com/OpenRoberta/openroberta-lab/actions/workflows/integration_test_triggered_by_cron.yml)

### Introduction

The steps below explain how to get started with the _sources_ of the OpenRoberta lab. If you just want to run the server locally only, please have a look into
the [Wiki - Docker Installation](https://github.com/OpenRoberta/openroberta-lab/wiki/Instructions-to-run-a-openroberta-lab-server-using-DOCKER) or the
(outdated, but working) description [Wiki - Installation](https://github.com/OpenRoberta/openroberta-lab/wiki/Installation).

If you want to contribute, please get in touch with us, see [Wiki - Community](https://github.com/OpenRoberta/openroberta-lab/wiki/Community), before you start.
Please also check our wiki for development procedure, coding conventions and further reading. We use the Github issue tracking system. Please file issues in the
main project **openroberta-lab**.

All of our production and test systems run on _Linux_ servers. Most of our developers use Linux laptops. The following instructions are valid for Linux
machines. Nevertheless you can follow the steps on a Win* machine. Please leave out the installation of cross compilers, because not all exist/work on Win*.
Without crosscompiler you can do almost everything (start the server, experiment with the frontend, create user, use the data base, change the lab by editing
our sources, ...), except cross compilation of course.

#### Prerequisites

You need Java JDK >= 1.8 and JAVA JDK <= 13.0.2 (e.g. `openjdk-11-jdk`), Maven, Git and Python3 together with pip3 and python3-serial. A typical install on
ubuntu:22.04 as superuser with apt is (other systems are similar):

```bash
apt-get update
apt-get install openjdk-11-jdk
apt-get install maven
apt-get install git
apt-get install npm
apt-get install python-is-python3
apt-get install python3-pip
apt-get install python3-serial
# check the installation:
java --version
mvn --version
git --version
python --version
pip --version
```

### Cross Compiler (optional)

#### Binaries

If you would like your local server to compile code for the different systems, you need to install software (crosscompilers, libraries, ...). It is recommended
to download them to a user-defined directory, such as `/opt/compilers/`, as this will save us the trouble of finding the correct path to the binaries later on!

* Arduino based robots
    * `sudo apt-get install libusb-0.1-4`
    * `sudo apt-get install binutils-avr gdb-avr avrdude`
    * download and unpack [avr-gcc](https://downloads.arduino.cc/tools/avr-gcc-7.3.0-atmel3.6.1-arduino5-x86_64-pc-linux-gnu.tar.bz2) to a directory of your
      choice.
* NXT
    * `sudo apt-get install nbc`
* Calliope
    * `sudo apt-get install srecord libssl-dev`
    * download and unpack the
      latest [gcc-arm-none-eabi](https://developer.arm.com/tools-and-software/open-source-software/developer-tools/gnu-toolchain/gnu-rm/downloads) to a
      directory of your choice.
* EV3 c4ev3
    * `sudo apt-get install g++-arm-linux-gnueabi`
* Bionics4Education
    * download and unpack [xtensa-esp32-elf](https://dl.espressif.com/dl/xtensa-esp32-elf-linux64-1.22.0-61-gab8375a-5.2.0.tar.gz) to a directory of your choice.
* Spike Prime / Robot Inventor using the Pybricks firmware:
    * `pip3 install mpy-cross-v6`     

Next, in .profile (or in the file your shell consumes at login time) add all downloaded binaries to the `PATH`.
The `<path-to-the-bin-folder>` should be replaced by one of the download directories (created earlier)
followed by the path to the bin folder in each compiler folder, usually `/bin`:

```shell
export PATH="$PATH:<path-to-the-bin-folder>:<path-to-the-next-bin-folder>:..."
```

After `source ~/.profile` verify with `echo $PATH` that the `PATH` is updated correctly.
Check whether the versions of the compiler installed are correct:

```shell
xtensa-esp32-elf-g++ --version 
#>> 5.2.0
arm-none-eabi-g++ --version 
#>> 10.3.1 
avr-gcc --version 
#>> 7.3.0
dpkg -l | grep libusb-0.1-4 
#>> 2:0.1.12-32build3
dpkg -l | grep binutils-avr 
#>> 2.26.20160125+Atmel3.6.2-4
avr-gdb --version 
#>> 10.1.90.20210103-git
avrdude -v 
#>> 6.3-20171130
nbc --version 
#>> 1.2
dpkg -l | grep libssl-dev  
#>> 3.0.2
dpkg -l | grep srecord 
#>> 1.64-3
dpkg -l | grep g++-arm-linux-gnueabi 
#>> 4:11.2.0
mpy-cross-v6 --version
#>> MicroPython a327cfc on 2023-02-16; mpy-cross emitting mpy v6
```

#### Resources

The cross-compiler need resources to work properly (header files, libraries, ...). These resources change little over time and are stored in
the '[ora-cc-rsc](https://github.com/OpenRoberta/ora-cc-rsc)' repository.

Please clone that directory and build it using `mvn clean install`. If the resources are not available, everything works fine, but running programs on real
robots doesn't work, because the cross-compiler will fail.

After successfully building the directory, make sure to add its path to `robot_crosscompiler_resourcebase` variable:

```shell
echo export robot_crosscompiler_resourcebase="<path-to-ora-cc-rsc-folder>" >> ~/.bashrc
```

### Installation

#### Step 1: Clone the repository and compile

The source of the OpenRoberta Lab is stored in the Github repository '[openroberta-lab](https://github.com/OpenRoberta/openroberta-lab)'. We use ``develop`` as
the default branch of our repository. If you clone our repository, you'll get ``develop`` first. In general this is a stable version.
If you want to run the currently deployed version, checkout ``master`` before building.

After a fresh git clone you get the **openroberta-lab** project folder. It includes almost everything you need to setup and extend your own openrobertalab
server. License information is available in the **docs** folder. Btw: if you use many repositories, put all of them into a common directoy as ``~/git``.

    git clone https://github.com/OpenRoberta/openroberta-lab.git # get the repository
    cd openroberta-lab                                           # cd into repository
    mvn clean install                                            # generate the server, at the end: build success

    cd OpenRobertaWeb
    npm install && npm run build && npx gulp                     # build the frontend, check tsc and gulp, must succeed
    cd ..

Takes some time.

#### Step 2: Make sure you have a database and start the server

The two main shell scripts to work with are `./admin.sh` and `./ora.sh`. _They have a lot of commands and options._
Call the scripts without parameter or with `-h` to get a help message.
`./admin.sh` is used for data base administration and server start, `./ora.sh` for server export and start. If you have a fresh clone of the server, make sure
that the OpenRobertaServer folder has a subfolder **db-embedded** with a database inside. If you don't have a database, you can create an empty database with

    ./admin.sh -git-mode create-empty-db

If you try to create a new database, but one exists, the old one is *not* changed and the command has no effect. The new database is found in the folder **
OpenRobertaServer/db-embedded**. We needs a database to store user accounts, programs, etc. We use HyperSQL. Other database systems (MySql, Postgres, ...,
oracle, db2, ...) would work, too. There are no special requirements on the database, that would exclude one. Only a Hibernate binding and a full transaction
support is needed.

Now use one of the following commands to start the server

    ./admin.sh -git-mode  start-from-git     # writes logging to ./admin
    ./ora.sh  start-from-git                 # writes logging to the console

If you did not install the crosscompiler resources, everything works fine (programming, simulation, code generation, user management, ...), except of generation
of binaries for robot systems.

The URL for your browser is [http://localhost:1999](http://localhost:1999) That's it!

### Creating an installation outside of the git repo

Often you want to run an openroberta installation of a fixed version for some time. This is easy. Let's assume, that you want to use the (non-existing)
directory /data/my-openroberta.

    mvn clean install && npm run build         # generate the server in the git repo
    ./ora.sh export /data/my-openroberta gzip  # export to the target directory. gzip compresses the static web resources fpr better performance.
    cd /data/my-openroberta                    # the export command supplies an administration script admin.sh, which is different from the one in the git workspace
    ./admin.sh create-empty-db                 # create an empty db at ./db-server - of course you may copy an old database to that location
    ./admin.sh start-server                    # spawns two processes: a database server and the openroberta jetty server

This kind of installation is useful for small installations. True productive systems need more support. We deploy our openroberta installation using docker. It
is described in detail in the file `README.md` of the repository `openroberta-docker`. In the repository you will find everything needed to create images and
start them. You can try it. If there are problems, you can contact us.

### Development notes

You should import the projects of the openroberta-lab repository locally into IDE's such as
[Eclipse](https://github.com/OpenRoberta/openroberta-lab/wiki/Importing-into-Eclipse)
or [IntelliJ](https://github.com/OpenRoberta/openroberta-lab/wiki/Importing-into-IntelliJ)!

The project OpenRobertaServer contains the server logic, that accesses

* a database with Hibernate-based DAO objects
* plugins for various robots which are supported in OpenRoberta
* services for browser-based clients
* services for robots connected to the lab either by Wifi or USB

The server is made of

* an embedded jetty server exposing REST services
* the services are based on jersey
* JSON (sometimes XML or plain text) is used for data exchange between front end, robots and server

Furthermore, the project OpenRobertaServer contains in directory staticResources for the browser client

* HTML and CSS
* _Generated_ Javascript under `/js` (**this should not be edited!**)
    * Javascript libraries based on jquery and bootstrap for the frontend
    * assertions (DBC), ajax-based server calls (COMM), logging (LOG) and
    * javascript resources for [blockly](https://developers.google.com/blockly)
    * controller and models written in Javascript, which implement the GUI

**The TypeScript and Javascript _sources_ can be found in `OpenRobertaWeb/`**

To work with the frontend (e.g. compiling the sources), please read the `README.md` of the
directory `OpenRoertaWeb` and follow the instructions.

#### Testing

To run backend tests, use `mvn test`. Running `mvn clean install` will make a stable, reproducible build with all unit tests executed.

To run the integration tests you have to supply an additional flag: `mvn clean install -PrunIT`. These tests expects, that all crosscompiler are installed
and Python3 including the `pylint` module version 3.x.

#### Some Frameworks used

We use Blockly, it is located in a separate repository. The build of the blockly is only done in the OpenRoberta/Blockly project and then copied to the
OpenRobertaServer/staticResources. You can not build Blockly in OpenRobertaServer project directly.

We use BrowserStack for Cross-Browser Testing

[<img src="https://github.com/OpenRoberta/openroberta-lab/blob/develop/Resources/images/blockly.png" width="100">](https://developers.google.com/blockly/)
[<img src="https://github.com/OpenRoberta/openroberta-lab/blob/develop/Resources/images/browserstack-logo-600x315.png" width="150">](http://browserstack.com/)

#### Have a look at the notes in LICENCE and NOTICE
