This directory contains the resources for the openroberta crosscompiler tests.
These are integration tests, that run crosscompilers to check whether our code generator are ok.

The classes that use the resources are:

* `de.fhg.iais.roberta.javaServer.integrationTest.CompilerWorkflowRobotCommonIT`
* `de.fhg.iais.roberta.javaServer.integrationTest.CompilerWorkflowRobotSpecificIT`

The integration tests are split up in two categories (stored in two different folders):

#### robot specific tests

In `testSpec.yml` all robots to be tested are declared. For each robot one
directory is used to store either blockly-xml programs (pattern `*.xml`) or already generated
source programs for that robot (pattern `*.cpp` for Calliope, for instance). The suffix used
for each robot is declared in the `testSpec.yml`.
  
If you want to extend these tests,
  
* first check, whether a program exists, that can be extended. It should become not too large, anyhow.
  Extending an existing program can be done by importing it into the openroberta lab, changing it,
  exporting it (overwriting the outdated version)
* otherwise write a new program, export it into the matching directory
  
#### common tests, that apply to _all_ robots

These tests are defined by XML-fragments, that are used to _generate_ from one set of fragments tests
that can be crosscompiled for _all_ robots. Consider the following:

* tests apply to all robots: thus only blocks can be used, that are available on all robots (if, loop, ...)
  and only the default configuration is available (and nothing from it can be used, of course)
* the XML-fragments needed for each program are:
  * the variable declarations (in directory `decl`)
  * the user-defined functions (in directory `fragment`)
  * the main program (in directory `prog`)
* to add a program you have to do the following steps
  * write a program (use the robot you like). Use only blocks available for all robots. There are special
    debug- and assert-blocks available for all robots to document what the program is supposed to do.
  * store this program for documentation purposes in directory `progOriginals`. Prefix the program with the
    robot name, e.g. `ev3_add_two_numbers.xml`)
  * use an editor to separate the program into the three XML-fragments mentioned above. Name them `add_two_numbers`, e.g..
    If you don't have user-defined functions, put nothing into directory `fragment`.
  * edit file `testSpec.yml` and put a new declaration in group `progs`, e.g. for `add_two_numbers`. `fragment:` is optional, use `decl: default`
    if you don't have variables (usually you have).
    
If the common tests are run, the following happens:

* one program after the other is taken and the following is done for _every_ robot
* the template matching the robot is taken from directory `template`.
* the robot's default configuration is copied into the template
* the XML-fragments are copied into the template
* a compile request (a JSON object) is generated and executed
* the return code from the compiler is saved for later logging