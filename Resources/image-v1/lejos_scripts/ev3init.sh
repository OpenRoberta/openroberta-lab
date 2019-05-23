#!/bin/sh
echo -n -e "Load Bluetooth         \r" > /dev/tty1
bluetoothd -n > /dev/null 2>&1 &

echo "Initialize Bluetooth..."
#Read Hw version from eeprom and save it in HwId file
Hw1=$(eeprog /dev/i2c-1 0x50 -16 -r 0x3F00:1 -x -f -q)
Hw2=$(eeprog /dev/i2c-1 0x50 -16 -r 0x3F01:1 -x -f -q)
Hw1=0x${Hw1//3f00| /}
Hw2=0x${Hw2//3f01| /}
Hw1=${Hw1//[[:space:]]}
Hw2=${Hw2//[[:space:]]}

if [ $((Hw1 ^ Hw2)) == 255 ]; then
  echo -e "HwId="${Hw1//0x/} > /var/volatile/HwId
  adr=$(eeprog /dev/i2c-1 0x50 -16 -r 0x3F06:6 -x -f -q)
  STRING=${adr//3f06| /}
else
  echo -e "HwId=03" > /var/volatile/HwId
  adr=$(eeprog /dev/i2c-1 0x50 -16 -r 0x3F00:6 -x -f -q)
  STRING=${adr//3f00| /}
fi
#----------------------------------------
# OPTIMIZE THIS SECTION

#Save Bluetooth address in file
echo -e ${STRING//[[:space:]]} > /var/volatile/BTser

#Remove first 2 spaces
STRING=${STRING/  /}
#Replace spaces with :
STRING=${STRING// /:}

#Remove last character
STRING="${STRING%?}"
ETHSTRING=$STRING
#---------------------------------------------
#Invert string
STRING=`echo $STRING | sed "s/\(.*\):\(.*\):\(.*\):\(.*\):\(.*\):\(.*\)/\6:\5:\4:\3:\2:\1/"`
#sleep 2
hciattach /dev/ttyS2 texas 2000000 "flow" "nosleep" $STRING
sdptool add SP
echo -n -e "Load USBNET            \r" > /dev/tty1

# load g_ether with MAC address based on Bluetooth address
modprobe g_ether dev_addr=02${ETHSTRING:3} host_addr=12${ETHSTRING:3}
