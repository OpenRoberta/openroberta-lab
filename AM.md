**Anne's New Notes**

### General notes about the system
The code found within /openroberta-lab covers the back-end logic of OpenRoberta. 
The block declarations, UI and general front-end logic is defined in the blockly git (see section 'Changing the blockly front-end' further down).

OpenRoberta DOES actually have custom blocks! They're called 'functions' and are available in the expert-toolbox. 

Most changes I've made in this code have the comment 'newmethod' over them. This is so it's easier to search for those changes within the code, if anyone wants to look at it.

**There is currently an error with the Action toolbox in NAO. I'm trying to fix this**

### How to run the OpenRoberta-Lab project
**I use bash to run all commands!**
First, check the Prerequisites in the README.md in the root folder. 

To (re)generate the server, run these two commands from root folder (SEE NOTES BELOW!):
rm -rf OpenRobertaRobot/target

mvn clean install -DskipTests

**IMPORTANT NOTES** Firstly, the 'mvn install' command skips all tests (with the -DskipTests flag). This is because it will run tests for ALL robot modes. Since we only build the system for the NAO robot, tests will fail and abort the build.
Also, we should likely think about creating tests for any new features we implement.

Secondly, you will likely experience errors when building. These errors are usually related to the 'target'-folders found within OpenRobertaRobot and OpenRobertaServer. If it complains that it failed to delete a file in the target folder, delete the file/folder manually and try the 'mvn install' command again, with the '-rf' flag provided in the terminal (this lets you avoid starting the build from scratch, and simply continues the build from a snapshot).

Then, we build the front-end:
cd OpenRobertaWeb
npm install && npm run build && npx gulp                     # build the frontend, check tsc and gulp, must succeed
cd ..
./ora.sh  start-from-git                 # starts server, writes logging to the console

Our OpenRoberta can be accessed at http://localhost:1999/

### Changing the blockly front-end 
Changes to the blockly front-end must be done in the blockly git code. 
Firstly, check out the first part of this page:
https://github.com/OpenRoberta/blockly/blob/master/README.md

**We only care about the first part of the page (the closure-library and you need python version 2.7).**

Our fork of the OpenRoberta blockly can be found here:
https://github.com/VictoriousAnnro/blockly_openRoberta

When changes have been made to the blockly git code, run build.py with this command from the root folder of the blockly git:
python2.7 build.py

Replace the blockly_compressed.js in this code (OpenRobertaServer\staticResources\blockly\blockly_compressed.js), with the file from the blockly git.

Replace the OpenRobertaServer\staticResources\blockly\msg folder in this code with the msg-folder from the blockly git. 

After this, repeat the steps in "How to run the OpenRoberta-Lab project".
**I'll check whether it's necessary to re-generate the server and build the frontend every time we update the blockly_compressed.js later**

### TO-DO
Overall, we're trying to do X things:
- Add feature that can locate code duplicates in the end-user's workspace
    * We need to decide how we're gonna do this. Firstly, do we try to find duplicates in the python-code generated from the blocks? Or do we look at duplicate chunks of block UI elements? (As seen in the blockly git).
    * Then, we need to figure out the algorithm/method to actually find the duplicates. Do we check for duplicates every time the user makes a change to the workspace? Every time they connect a new block to the stack? 

- Add feature to highlight duplicate chunks of code (the hightlight color should NOT be the same for all duplicate code! It should be easy for user to see which chunks are related).
    + Add feature that explains why the chunks are being highlighted (let the user know why they should care about this).

- Add feature to let the end-user create a custom block with few clicks (They must specify block name, variables, and where the variables are used. The feature takes care of moving the duplicate chunk into a custom block, and replacing the chunks with the new block).

- Change the name from 'Functions' to something more user-friendly