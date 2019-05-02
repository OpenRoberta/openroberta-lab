FROM ubuntu:16.04
 
RUN apt update && apt install -y git maven openjdk-8-jdk openjdk-8-jdk-headless wget nbc gcc-arm-none-eabi binutils-arm-none-eabi libnewlib-arm-none-eabi srecord libssl-dev

WORKDIR /opt
RUN wget http://de.archive.ubuntu.com/ubuntu/pool/universe/g/gcc-avr/gcc-avr_5.4.0+Atmel3.6.0-1build1_amd64.deb
RUN wget http://de.archive.ubuntu.com/ubuntu/pool/universe/a/avr-libc/avr-libc_2.0.0+Atmel3.6.0-1_all.deb
RUN wget http://de.archive.ubuntu.com/ubuntu/pool/universe/b/binutils-avr/binutils-avr_2.26.20160125+Atmel3.6.0-1_amd64.deb
RUN wget http://de.archive.ubuntu.com/ubuntu/pool/main/m/mpfr4/libmpfr6_4.0.1-1_amd64.deb
RUN wget http://de.archive.ubuntu.com/ubuntu/pool/main/m/mpclib3/libmpc3_1.1.0-1_amd64.deb

RUN apt install -y ./gcc-avr_5.4.0+Atmel3.6.0-1build1_amd64.deb ./avr-libc_2.0.0+Atmel3.6.0-1_all.deb ./binutils-avr_2.26.20160125+Atmel3.6.0-1_amd64.deb ./libmpfr6_4.0.1-1_amd64.deb ./libmpc3_1.1.0-1_amd64.deb

COPY ["./runIT.sh","/opt/"]
RUN chmod +x /opt/runIT.sh
ENTRYPOINT ["/opt/runIT.sh"]
