#!/bin/bash

SCRIPTPATH="$( cd "$(dirname "$0")" ; pwd -P )"
UF2_FILE=$1

function get_program_name {
	path=${UF2_FILE%/*} 
	base=${UF2_FILE##*/}
	program_name=${base%.*}
	echo $program_name
}

PROGRAM_NAME=$(get_program_name)

EV3DUDER="$SCRIPTPATH/ev3duder"
UF2_TOOL="$SCRIPTPATH/files2uf2-0.1.1-SNAPSHOT.jar"
TMP_FOLDER=/tmp/upload-c4ev3/prjs

mkdir -p $TMP_FOLDER
result=$?
if [[ $result -ne 0 ]]; then
	echo "Can't create temp folder"
	exit $result
fi

function print_help {
	echo "This script uploads a program generated for the EV3 using c4ev3"
	echo "to the EV3 robot connected via USB."
	echo ""
	echo "Permissions: this script will ask for your password (using sudo)"
	echo "to access the USB device."
	echo ""
	echo "usage: upload.sh <program.uf2>"
	echo "example: ./upload.sh NEPOprog.uf2"
}

function extract_uf2 {
	java -jar "$UF2_TOOL" unpack "$UF2_FILE" "$TMP_FOLDER"
	result=$?
	if [[ $result -ne 0 ]]; then
		echo "Error while unpacking the uf2 file"
		exit $result
	fi
}

function upload_program_files {
	declare -a files=("$PROGRAM_NAME.elf"
					  "$PROGRAM_NAME.rbf"
					  "$PROGRAM_NAME.tmp"
					  "NEPO-just-uploaded.txt")

	echo "(ev3duder output follow)"
	for i in "${files[@]}"
	do
   		sudo "$EV3DUDER" up "$TMP_FOLDER/BrkProg_SAVE/$i" "../prjs/BrkProg_SAVE/$i"
   		result=$?
   		if [[ $result -ne 0 ]]; then
   			echo "(end of ev3duder output)"
		    echo "Error while uploading file $i to the robot"
		    exit $result
		fi
	done
	echo "(end of ev3duder output)"
}

function run_program_first_time {
	echo "(ev3duder output follow)"
	sudo "$EV3DUDER" exec "../prjs/BrkProg_SAVE/$PROGRAM_NAME.elf"
   	echo "(end of ev3duder output)"
   	result=$?
   	if [[ $result -ne 0 ]]; then
	    echo "Error while starting the program for the first time to complete installation"
	    exit $result
	fi
}

if [[ "$#" -ne 1 ]]
then
    print_help
    exit
fi

echo "Extracting UF2 file"
extract_uf2
echo ""

echo "Uploading program files ..."
upload_program_files
run_program_first_time
echo "Program files uploaded"
echo ""

echo "Done!"
echo "Wait for the robot to beep and then start the program"
