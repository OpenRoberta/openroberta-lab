#!/bin/bash

SCRIPTPATH="$(dirname "$(readlink -f "$0")")"

INSTALL_FOLDER=/opt/upload-c4ev3
LINK_NAME=/usr/bin/upload-c4ev3

function check_java {
  if ! [ -x "$(command -v java)" ]; then
    echo "Java not found!"
    echo "Please install Java and then re-run this script"
    exit 1
  fi
}

function check_if_already_installed {
  if [ -x "$(command -v upload-c4ev3)" ]; then
    echo "upload-c4ev3 seems to be already installed"
    echo "Run 'uninstall.sh' if you want to uninstall it"
    exit 1
  fi
}

function copy_to_opt {
  echo "Copying script folder to $INSTALL_FOLDER"
  sudo cp -r "$SCRIPTPATH" "$INSTALL_FOLDER"
  result=$?
	if [[ $result -ne 0 ]]; then
		echo "Couldn't copy script folder. Did you insert your password when asked?"
		exit $result
	fi
}

function create_link {
  echo "Creating link to $LINK_NAME"
  sudo ln -s "$INSTALL_FOLDER/upload.sh" "$LINK_NAME"
  result=$?
	if [[ $result -ne 0 ]]; then
		echo "Couldn't create link. Did you insert your password when asked?"
		exit $result
	fi
}


check_java
check_if_already_installed
copy_to_opt
create_link

echo ""
echo "Script installed, type 'upload-c4ev3' to run it."
echo "To uninstall the script run 'uninstall.sh'"
