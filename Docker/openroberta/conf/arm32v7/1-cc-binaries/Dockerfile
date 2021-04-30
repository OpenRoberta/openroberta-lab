FROM arm32v7/debian:buster

RUN apt-get update && \
    apt-get install -y \
        wget unzip curl jq \
        libusb-0.1-4 \
        gcc-avr binutils-avr gdb-avr avr-libc avrdude \
        nbc \
        gcc-arm-none-eabi srecord libssl-dev  \
        python-pip && \
    apt-get autoremove
RUN pip install uflash

# Install Java JDK
RUN wget https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u222-b10/OpenJDK8U-jdk_arm_linux_hotspot_8u222b10.tar.gz && \
    tar xzvf OpenJDK8U-jdk_arm_linux_hotspot_8u222b10.tar.gz && \
    rm OpenJDK8U-jdk_arm_linux_hotspot_8u222b10.tar.gz && \
    mv jdk8u222-b10 /opt/jdk8
ENV PATH="/opt/jdk8/bin:${PATH}"

# c4ev3 cross compiler for arm
# TODO: Replace with public URL
# COPY C4EV3.Toolchain-2019.08.0-rpi.tar.gz C4EV3.Toolchain-2019.08.0-rpi.tar.gz
# RUN tar -zxvf C4EV3.Toolchain-2019.08.0-rpi.tar.gz && \
#     rm C4EV3.Toolchain-2019.08.0-rpi.tar.gz && \
#     mv C4EV3.Toolchain-2019.08.0 /opt/C4EV3.Toolchain-2019.08.0 && \
#     ln -s /opt/C4EV3.Toolchain-2019.08.0/arm-c4ev3-linux-uclibceabi/gcc-8.2.1-uclibc-ng-1.0.31-binutils-2.31.1-kernel-2.6.33-rc4-sanitized/bin/arm-c4ev3-linux-uclibceabi-g++ /usr/bin/arm-c4ev3-linux-uclibceabi-g++
