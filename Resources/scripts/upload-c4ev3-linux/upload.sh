#!/bin/bash

SCRIPTPATH="$(dirname "$(readlink -f "$0")")"
UF2_FILE=$1

EV3DUDER="$SCRIPTPATH/ev3duder"
UF2_TOOL="$SCRIPTPATH/files2uf2-0.1.1-SNAPSHOT.jar"
TMP_FOLDER=/tmp/upload-c4ev3/prjs

rm -rf $TMP_FOLDER
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
	echo "usage: upload-c4ev3 <program.uf2>"
	echo "example: upload-c4ev3 NEPOprog.uf2"
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
	echo "(ev3duder output follow)"
	for file_path in "$TMP_FOLDER"/BrkProg_SAVE/*
	do
	    file_name=$(basename "$file_path")
   		sudo "$EV3DUDER" up "$TMP_FOLDER/BrkProg_SAVE/$file_name" "../prjs/BrkProg_SAVE/$file_name"
   		result=$?
   		if [[ $result -ne 0 ]]; then
   			echo "(end of ev3duder output)"
		    echo "Error while uploading file $file_name to the robot"
		    exit $result
		  fi
	done
	echo "(end of ev3duder output)"
}

function get_elf_file_name_from_tmp_folder {
  elf_files=("$TMP_FOLDER"/BrkProg_SAVE/*.elf)
  elf_file_path=${elf_files[0]}
  elf_file_name=$(basename "$elf_file_path")
  echo "$elf_file_name"
}

function run_program_first_time {
	echo "(ev3duder output follow)"
	elf_file_name=$(get_elf_file_name_from_tmp_folder)
	sudo "$EV3DUDER" exec "../prjs/BrkProg_SAVE/$elf_file_name"
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
