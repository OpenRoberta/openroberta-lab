FROM ubuntu:16.04
 
RUN apt update && apt install -y srecord gcc-arm-none-eabi binutils-arm-none-eabi libnewlib-arm-none-eabi

COPY ["./resources/", "/opt/calliope/"]
RUN chmod +x /opt/calliope/compile.sh
COPY ["./init.sh", "/opt/"]

ENTRYPOINT ["/opt/init.sh"]
