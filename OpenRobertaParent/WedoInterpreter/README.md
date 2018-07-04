USING THIS PROJECT

1. preconditions:
    * Java project with pom and some Java test classes (that generate JSON for later consumption by Javascript)
    * node is installed
    
2. import this project as maven project

3. create a wedo program, export as XML and remove all XML elements but the <block_set /> of the program (usually starting from line 3) and store it in
   directory 'simulatorTests'

4. run the JUnit test class 'GenerateJsonFromSimIT' after setting the array of XML files to be compiled (constant NAME_OF_TESTS) as needed, e.g. 'myTest'.

5. after refresh 'mytest.json' appears. After formatting the operations can be inspected

6. in a run-configuration (run 'jsGenerated/main.js' as node application!) set 'myTest' as parameter (lists are possible, too) and run.
   The operations are executed. Watch the console.

TYPESCRIPT SUPPORT

to be written

JAVASCRIPT SUPPORT (OUTDATED!)

3. project -> right click -> properties
    * convert to faceted form
    * add Javascript facet
    * activate Javascript builder in facets
    * in Javascript add formatter + save actions (tbd)
    * add the base directory /js to the include path, add **/*.js as inclusion pattern
    
4. in preferences -> Javascript -> Runtimes
    * add the node location
    * select the node.js runtime
    * error: this is resetted to chromium by eclipse

5. open a js file with the Javascript editor
    * outline ok
    * call hierarchy ok
    * completion ok
    * goto definition fails

6. debug/run
    * run as ... ok
    * debug as ... ok when stepping or hitting a breakpoint in the "main" file
    * showing variables etc is ok
    * fails to hit a breakpoint in a "non-main" file
    * but stepping down into that file and then seting a breakpoint is ok
    * the breakpoint vanishes randomly(?)
    * process must be cancelled explicitly
