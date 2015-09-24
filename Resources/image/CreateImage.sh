#!/bin/bash

# Current version is only for Linux. Probably Mac is OK too.
os=$(uname -a)
if [[ ${os} == *"MINGW"* ]]
then
  echo "You are using Mingw on Windows (probably Git).";
  echo "Please run this script only on Linux to keep file permissions of the firmware!";
  exit 1
fi
#---

# temporary directory in repository to extract zip file
tmp='temp'
# temporary directory on Linux file system
work='/tmp/openroberta'
# Original leJOS image file which you can download
lejosimage='lejosimage/lejosimage.zip'

# "Main location for defining a version number"
parentpom='../../OpenRobertaParent/pom.xml'

# Directory contains files for updating the brick after successful maven install
libdir='../../OpenRobertaServer/target/updateResources'

menu=${libdir}/EV3Menu.jar
json=${libdir}/json.jar
runtime=${libdir}/OpenRobertaRuntime.jar
shared=${libdir}/OpenRobertaShared.jar

echo ""
echo "---Creating Open Roberta Firmware---"

# Retrieve Open Roberta version (from pom.xml)
version=$(grep -Po -m 1 '<version>\K.+\..+\..+?(?=[^0-9])' ${parentpom})
echo "OpenRobertaParent version is ${version}!"
# ---

# Check if required files exist
if [ -f ${menu} ] && [ -f ${json} ] &&[ -f ${runtime} ] && [ -f ${shared} ]
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

if [ -f ${lejosimage} ]
then
    echo "Lejosimage found!"
    echo "Using ${lejosimage}"
else
    echo "Lejosimage in ${lejosimage} is missing!"
    echo "Has someone changed the file structure of the repository?"
    echo "---Abort---"
    echo ""
    exit 1
fi
# ---

# Create temporary directories in Linux file system to keep file permissions of leJOS
echo "Create temporary directories..."
mkdir -p -v ${tmp}
mkdir -p -v ${work}
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

cd - > /dev/null 2>&1
cp ${menu} ${work}/lejosimage/lejosfs/home/root/lejos/bin/utils
cp ${json} ${work}/lejosimage/lejosfs/home/roberta/lib
cp ${runtime} ${work}/lejosimage/lejosfs/home/roberta/lib
cp ${shared} ${work}/lejosimage/lejosfs/home/roberta/lib
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
rm -r ${tmp}
rm -r /tmp/openroberta
# ---

echo "If there are no error messages, the image was created successfully."
echo "The image file is: OpenRobertaFirmware-${version}-release.zip"
echo "---Finish---"
echo ""
