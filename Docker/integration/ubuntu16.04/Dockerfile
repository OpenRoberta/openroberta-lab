FROM ubuntu:16.04
 
RUN apt update && apt install -y git maven openjdk-8-jdk openjdk-8-jdk-headless python python-pip nbc avrdude avr-libc gcc-avr libusb-0.1-4 srecord libstdc++-4.9-dev gcc-arm-none-eabi binutils-arm-none-eabi libnewlib-arm-none-eabi srecord libssl-dev
RUN pip install uflash
    
COPY ["./runIT.sh","/opt/"]
RUN chmod +x /opt/runIT.sh
ENTRYPOINT ["/opt/runIT.sh"]
