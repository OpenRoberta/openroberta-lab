### overview

This project should make testing of WeDo programs a lot easier. You can export any WeDo program (using the lab), generate interpreter code (using a Java Junit class)
and run the interpreter code (using node). Requirements:
 
* an IDE (as eclipse)
* node is installed
    
* this project is imported as maven project

* a run-configuration for 'jsGenerated/main.js' as node application is created. The working directory in the run configuration is the project's base.

### using this project

1. use the lab to create a wedo program, export it as XML into the directory 'xmlTests', e.g. using the name 'y-assign'. The file 'y-assign.xml' is created.

2. run the JUnit test class 'GenerateJsonFromSimIT' after adding 'y-assign' to the constant NAME_OF_TESTS (a run configuration is handy here).

3. after refresh 'y-assign.json' appears. After formatting the operations can be inspected.

4. in the run-configuration of 'jsGenerated/main.js' set 'y-assign' as parameter and run.
   The operations are executed. Watch the console.

### typescript support for developing the WeDo interpreter

to be written

### javascript support  for developing the WeDo interpreter (OUTDATED! WE USE TYPESCRIPT!)

1. project -> right click -> properties
    * convert to faceted form
    * add Javascript facet
    * activate Javascript builder in facets
    * in Javascript add formatter + save actions (tbd)
    * add the base directory /js to the include path, add **/*.js as inclusion pattern
    
2. in preferences -> Javascript -> Runtimes
    * add the node location
    * select the node.js runtime
    * error: this is resetted to chromium by eclipse

3. open a js file with the Javascript editor
    * outline ok
    * call hierarchy ok
    * completion ok
    * goto definition fails

4. debug/run
    * run as ... ok
    * debug as ... ok when stepping or hitting a breakpoint in the "main" file
    * showing variables etc is ok
    * fails to hit a breakpoint in a "non-main" file
    * but stepping down into that file and then seting a breakpoint is ok
    * the breakpoint vanishes randomly(?)
    * process must be cancelled explicitly
