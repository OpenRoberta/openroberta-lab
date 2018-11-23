FROM fedora:29

RUN dnf install -y libusb avr-gcc avr-gcc-c++ avr-libc avrdude nbc arm-none-eabi-gcc-cs arm-none-eabi-gcc-cs-c++ srecord uflash git maven arm-none-eabi-newlib python python2-pip
RUN pip install uflash
    
COPY ["./runIT.sh","/opt/"]
RUN chmod +x /opt/runIT.sh
ENTRYPOINT ["/opt/runIT.sh"]
