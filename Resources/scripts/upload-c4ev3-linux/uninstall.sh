#!/bin/bash

INSTALL_FOLDER=/opt/upload-c4ev3
LINK_NAME=/usr/bin/upload-c4ev3

function check_if_installed {
  if ! [ -x "$(command -v upload-c4ev3)" ]; then
    echo "upload-c4ev3 doesn't seem to be installed"
    exit 1
  fi
}

function remove_link {
  echo "Removing link $LINK_NAME"
  sudo rm "$LINK_NAME"
  result=$?
	if [[ $result -ne 0 ]]; then
		echo "Couldn't remove the link. Did you insert your password when asked?"
		exit $result
	fi
}

function remove_folder() {
  echo "Removing folder $INSTALL_FOLDER"
  sudo rm -r "$INSTALL_FOLDER"
  result=$?
	if [[ $result -ne 0 ]]; then
		echo "Couldn't remove the folder. Did you insert your password when asked?"
		exit $result
	fi
}

check_if_installed
remove_link
remove_folder


echo ""
echo "Script uninstalled"

