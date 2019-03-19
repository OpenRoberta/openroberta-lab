FROM ubuntu:18.04
 
RUN apt update
RUN apt install -y avrdude avr-libc gcc-avr git maven python python-pip libusb-0.1-4 srecord nbc gcc-arm-none-eabi binutils-arm-none-eabi libnewlib-arm-none-eabi openjdk-8-jdk openjdk-8-jdk-headless
RUN apt remove -y openjdk-11-jre-headless
RUN pip install uflash
    
COPY ["./runIT.sh","/opt/"]
RUN chmod +x /opt/runIT.sh
ENTRYPOINT ["/opt/runIT.sh"]
 
