# Script to upload c4ev3 program to EV3 from linux

The script `upload.sh` will upload the uf2 file specified as first argument to EV3 robot
connected through USB.

The script uses:
 - [ev3duder](https://github.com/c4ev3/ev3duder/) program from c4ev3 to upload the program files and execute the program
  for the first time;
 - [a Java tool](https://github.com/simonedegiacomi/files2uf2) to extract program files from the uf2 file;
 