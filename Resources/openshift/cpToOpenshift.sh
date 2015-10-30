#!/bin/sh

# it is expected that this script is executed with the 'openshift' directory as its base directory
cd ../.. # now at the root of the working directory
case `pwd` in
 */openRoberta) : ;;
 *)             echo 'copy script called from a unexpected directory. Script terminates for safety reasons'
                exit 12 ;;
esac

SOURCE=.
TARGET=../redhatOpenRoberta

rm -rf $TARGET/diy
./ora.sh --export -i $TARGET/diy
cp -r $SOURCE/OpenRobertaServer/db $TARGET/diy/db
cp $SOURCE/Resources/openshift/action_hooks/* $TARGET/.openshift/action_hooks/