### The Nepo Project

This framework provides an abstraction layer of the [Blockly library](https://github.com/google/blockly), which is used to create graphical blocks for the Open Roberta Lab on client site. Changes and extensions for NEPO should only be made here and never directly in Blockly.

This project is completely written in TypeScript and will be extended continuously. Once this framework is integrated into the Open Roberta Lab the [fork from Blockly](https://github.com/OpenRoberta/blockly) is not needed anymore.

1. TypeScript support for developing the Nepo project in eclipse

* install e.g. the Wild Web Developer plugin
* use (install) nodejs, recommended version is **12.16.3**
  * `sudo npm cache clean -f` 
  * `sudo npm install -g n` 
  * `sudo n 12.16.3` 
  * Check the version with `node -v`
* use (install) TypeScript, recommended version is **3.9.2**
  * e.g. `sudo npm install -g typescript@3.9.2`
  * check the version with `tsc -v`
* import the Nepo project 
* install dependencies
  * `npm install`
* run the TypeScript compiler to get the required JavaScript files
  * `tsc` or `tsc --watch`

2. debug/test/run
   
  To simple run the playground the setup of TypeScript in step 1 is not needed, only 
  an installation of node is required and `npm install`
  * start a server in `openroberta-lab` (parent folder of Nepo)
    * with e.g. `python -m SimpleHTTPServer`
    * open a browser
    * type `http://localhost:8000/Nepo/playground.html`
    
    
