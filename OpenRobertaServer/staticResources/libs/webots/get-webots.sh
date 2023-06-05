#!/bin/bash

# Usage ./get-webots.sh <webots-directory>

TARGET_DIRECTORY=$(pwd)
WEBOTS_REPO=$1
WEBOTS_VERSION=R2021c

cd "$TARGET_DIRECTORY" || exit

echo "Compiling webots.js"
rollup $WEBOTS_REPO/resources/web/wwi/webots.js --file webots.min.js --format amd --compact > /dev/null 2>&1

echo "Copying images from webots"

if [ -d "$TARGET_DIRECTORY"/images ]
then
    rm -r "$TARGET_DIRECTORY"/images
fi

cp -r $WEBOTS_REPO/resources/web/wwi/images/ "$TARGET_DIRECTORY"/

if [ -f "$TARGET_DIRECTORY"/images/.gitignore ]
then
  rm "$TARGET_DIRECTORY"/images/.gitignore
fi

cp -r $WEBOTS_REPO/resources/wren/textures/ "$TARGET_DIRECTORY"/images/post_processing

echo "Download dependencies from cyberbotics"
wget -q https://git.io/glm-js.min.js -O glm-js.min.js
wget -q https://cyberbotics.com/wwi/$WEBOTS_VERSION/enum.js -O enum.js
wget -q https://cyberbotics.com/wwi/$WEBOTS_VERSION/wrenjs.js -O wrenjs.js
wget -q https://cyberbotics.com/wwi/$WEBOTS_VERSION/wrenjs.data -O wrenjs.data
wget -q https://cyberbotics.com/wwi/$WEBOTS_VERSION/wrenjs.wasm -O wrenjs.wasm
