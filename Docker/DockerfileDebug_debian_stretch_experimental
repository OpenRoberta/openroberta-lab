FROM debian:stretch

ARG LAST_RUN_OF_BASE=2018-11-28

RUN apt-get update && apt-get -y upgrade && \
    apt-get install -y git maven && \
    apt-get install -y python-pip && \
    apt-get install -y libusb-0.1-4 && \
    apt-get install -y gcc-avr binutils-avr gdb-avr avr-libc avrdude && \
    apt-get install -y nbc && \
    apt-get install -y gcc-arm-none-eabi srecord libssl-dev && \
    pip install uflash && \
    apt-get update && apt-get -y upgrade && \
    apt-get install -y openjdk-8-jdk && \
    update-java-alternatives -s java-1.8.0-openjdk-amd64 && \
    apt-get install -y wget && \
    apt-get clean

RUN git config --global core.fileMode false && \
    git config --global user.email "reinhard.budde@iais.fraunhofer.de" && \
    git config --global user.name "Reinhard Budde"

WORKDIR /tmp
RUN wget http://de.archive.ubuntu.com/ubuntu/pool/universe/n/newlib/libnewlib-dev_3.0.0.20180802-2_all.deb && \
    wget http://de.archive.ubuntu.com/ubuntu/pool/universe/n/newlib/libnewlib-arm-none-eabi_3.0.0.20180802-2_all.deb && \
    dpkg -i libnewlib-dev_3.0.0.20180802-2_all.deb libnewlib-arm-none-eabi_3.0.0.20180802-2_all.deb && \
    rm libnewlib-dev_3.0.0.20180802-2_all.deb libnewlib-arm-none-eabi_3.0.0.20180802-2_all.deb

# clone the repo. Run mvn once to install most all the artifacts needed in /root/.m2 to speed up later builds
WORKDIR /opt
RUN git clone https://github.com/OpenRoberta/robertalab.git
WORKDIR /opt/robertalab/OpenRobertaParent
RUN git checkout develop && \
    chmod +x RobotArdu/resources/linux/arduino-builder RobotArdu/resources/linux/tools-builder/ctags/5.8*/ctags && \
    chmod +x RobotArdu/resources/linux-arm/arduino-builder RobotArdu/resources/linux-arm/tools-builder/ctags/5.8*/ctags && \
    ( mvn clean install -PrunIT || echo '!!!!!!!!!! runIT crashed. Should NOT happen !!!!!!!!!!' )

# prepare the entry point
WORKDIR /opt/robertalab
ENTRYPOINT ["/bin/bash"]