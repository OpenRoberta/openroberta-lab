#!/bin/bash

# Current version is only for Linux. Probably Mac is OK too.
os=$(uname -a)
if [[ ${os} == *"MINGW"* ]]
then
  echo "You are using Mingw on Windows (probably Git).";
  echo "Please run this script only on Linux!!!";
  exit 1
fi
#---

# temporary directory in repository to extract zip file
tmp='temp'
# temporary directory on Linux file system (ext)
work='/tmp/openroberta'
# folder in which scripts are in
scripts='lejos_scripts'
espeak='espeak'

# Directory contains files for updating the brick after successful maven install
libdir='../../OpenRobertaParent/RobotEV3/resources/updateResources/lejos_v1'

# Open Roberta default version, can be modified by the first input parameter
# for example "sh CreateImage.sh 1.4.0"
# the version only defines the file name
# the "real" version comes from the maven install procedure
version='2.4.0'

menu=${libdir}/EV3Menu.jar
json=${libdir}/json.jar
runtime=${libdir}/EV3Runtime.jar
websocket=${libdir}/Java-WebSocket.jar
lejosfile=lejos.tar.gz
lejosimage=lejosimage.zip

echo ""
echo "---Creating Open Roberta Firmware---"

# Set the version from the input parameter
if [ ! -z $1 ]
then
	version=$1
	echo "Open Roberta version is ${version}!"
else
	echo "Open Roberta default version is ${version}!"
fi
# ---

# Check if required files exist
if [ -f ${menu} ] && [ -f ${json} ] && [ -f ${runtime} ] && [ -f ${websocket} ]
then
    echo "Compiled EV3 libraries found!"
    echo "Using files in ${libdir}"
else
    echo "Compiled EV3 libraries are missing!"
    echo "Please maven install the project before using this script!"
    echo "---Abort---"
    echo ""
    exit 1
fi
# ---

# Download lejos automatically with wget from sourceforge
echo "Downloading lejos files from sourceforge!"
echo ""
wget -O ${lejosfile} https://sourceforge.net/projects/ev3.lejos.p/files/0.9.1-beta/leJOS_EV3_0.9.1-beta.tar.gz/download

if [ -f ${lejosfile} ]
then
	echo "Download successful!"
	echo "Extracting file now..."
	tar --wildcards -zxf ${lejosfile} leJOS*/${lejosimage}
	mv leJOS*/${lejosimage} ${lejosimage}
	rm -r leJOS*
	rm ${lejosfile}
	if [ -f ${lejosimage} ]
	then
		echo "Got ${lejosimage}!"
	else
		echo "${lejosimage} does not exist. Extracting the file from ${lejosfile} failed!"
		exit 1
	fi
else
	echo "${lejosfile} not found! Downloading the file failed."
	exit 1
fi
# ---

# Create temporary directories in Linux file system to keep file permissions of leJOS
echo "Create temporary directories..."
mkdir -p ${tmp}
mkdir -p ${work}
# ---

# Extract image
echo "Extract lejosimage..."
unzip -o -q ${lejosimage} -d ${tmp}
cp ${tmp}/lejosimage.bz2 ${work}
rm ${tmp}/lejosimage.bz2
cd ${work}
tar -xjf lejosimage.bz2 lejosimage
rm lejosimage.bz2
# ---

# Insert/ replace files
echo "Replace files..."
rm ${work}/lejosimage/lejosfs/home/root/lejos/bin/utils/EV3Menu.jar
mkdir -p ${work}/lejosimage/lejosfs/home/roberta/lib
mkdir -p ${work}/lejosimage/lejosfs/usr/lib

cd - > /dev/null 2>&1
cp ${menu} ${work}/lejosimage/lejosfs/home/root/lejos/bin/utils
cp ${json} ${work}/lejosimage/lejosfs/home/roberta/lib
cp ${runtime} ${work}/lejosimage/lejosfs/home/roberta/lib
cp ${websocket} ${work}/lejosimage/lejosfs/home/roberta/lib

cp ${scripts}/checkroot ${work}/lejosimage/lejosfs/etc/init.d/
cp ${scripts}/ev3init.sh ${work}/lejosimage/lejosfs/etc/init.d/
cp ${scripts}/jrun ${work}/lejosimage/lejosfs/home/root/lejos/bin
cp ${scripts}/startbt ${work}/lejosimage/lejosfs/home/root/lejos/bin
cp ${scripts}/startpan ${work}/lejosimage/lejosfs/home/root/lejos/bin
cp ${scripts}/startup ${work}/lejosimage/lejosfs/home/root/lejos/bin

cp ${espeak}/speak ${work}/lejosimage/lejosfs/usr/bin/speak
cp ${espeak}/lib/* ${work}/lejosimage/lejosfs/usr/lib/
cp -r ${espeak}/share/espeak-data ${work}/lejosimage/lejosfs/usr/share

# ---

# Pack everything together again
echo "Pack image..."
cd ${work}
tar -jcf lejosimage.bz2 lejosimage
cd - > /dev/null 2>&1
cp ${work}/lejosimage.bz2 ${tmp}
rm -f OpenRobertaFirmware-x.x.x-release.zip
cd ${tmp}
zip -r -q ../OpenRobertaFirmware-${version}-release.zip *
echo "...Done."
# ---

# Cleanup
echo "Delete temporary directories..."
cd - > /dev/null 2>&1
rm ${lejosimage}
rm -r ${tmp}
rm -r ${work}
# ---

openrobertaimage="OpenRobertaFirmware-${version}-release.zip"
if [ -f ${openrobertaimage} ]
then
	echo "Image file created successfully!"
	echo "The image file is: ${openrobertaimage}"
else
	echo "Image file does not exist. Something went wrong."
fi
echo "---Finish---"
echo ""
