FROM ubuntu:22.04 as builder

RUN apt-get update && \
    apt-get install --no-install-recommends -y curl wget lbzip2 python3 python3-pip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /tmp
RUN wget http://downloads.arduino.cc/tools/avr-gcc-7.3.0-atmel3.6.1-arduino5-x86_64-pc-linux-gnu.tar.bz2 && \
    wget https://developer.arm.com/-/media/Files/downloads/gnu-rm/9-2019q4/RC2.1/gcc-arm-none-eabi-9-2019-q4-major-x86_64-linux.tar.bz2 && \
    wget https://dl.espressif.com/dl/xtensa-esp32-elf-linux64-1.22.0-61-gab8375a-5.2.0.tar.gz
WORKDIR /opt/compiler
RUN tar -xf  /tmp/avr-gcc-7.3.0-atmel3.6.1-arduino5-x86_64-pc-linux-gnu.tar.bz2 && \
    tar -xf  /tmp/gcc-arm-none-eabi-9-2019-q4-major-x86_64-linux.tar.bz2 && \
    tar -xzf /tmp/xtensa-esp32-elf-linux64-1.22.0-61-gab8375a-5.2.0.tar.gz && \
    rm -rf /tmp/*

WORKDIR /tmp
RUN wget http://ftp.debian.org/debian/pool/main/n/newlib/libnewlib-dev_3.1.0.20181231-1_all.deb && \
    wget http://ftp.debian.org/debian/pool/main/n/newlib/libnewlib-arm-none-eabi_3.1.0.20181231-1_all.deb

FROM ubuntu:22.04

ENV LANG de_DE.UTF-8
ENV LANGUAGE de_DE:de
ENV LC_ALL de_DE.UTF-8

RUN apt-get update && \
    apt-get install --no-install-recommends -y locales tzdata curl wget lbzip2 openjdk-8-jdk python3 python3-pip maven && \
    ln -s /usr/bin/python3 /usr/bin/python && \
    pip3 install esptool && \
    locale-gen de_DE.UTF-8 && \
    ln -fs /usr/share/zoneinfo/Europe/Berlin /etc/localtime && \
    export DEBIAN_FRONTEND=noninteractive && \
    dpkg-reconfigure --frontend noninteractive tzdata && \
    apt-get install --no-install-recommends -y nbc libusb-0.1-4 srecord libssl-dev g++-arm-linux-gnueabi && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
    
WORKDIR /tmp
COPY --from=builder /tmp ./
RUN dpkg -i libnewlib-dev_3.1.0.20181231-1_all.deb libnewlib-arm-none-eabi_3.1.0.20181231-1_all.deb && \
    rm libnewlib-dev_3.1.0.20181231-1_all.deb libnewlib-arm-none-eabi_3.1.0.20181231-1_all.deb

WORKDIR /opt/compiler
COPY --from=builder /opt/compiler ./
ENV PATH="${PATH}:/opt/compiler/avr/bin:/opt/compiler/gcc-arm-none-eabi-9-2019-q4-major/bin:/opt/compiler/xtensa-esp32-elf/bin"
