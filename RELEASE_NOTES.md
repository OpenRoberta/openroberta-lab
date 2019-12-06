# Release notes

## [3.5.0] - 2019-07-31

### New features
 - C4EV3: beta version of new robot plugin for EV3 robots
 - ALL: compiler output is now shown to user
 - SENSEBOX: added german translations for configuration blocks
### General improvements
 
### Bug fixes
 - ALL: wait until captions no longer disappear
 - ALL: my configurations are now single clickable anywhere, same behaviour as in my programs
 - ALL: lab reconnects automatically after wifi network changes
 - ALL: fixed invalid connections in nested ternary operators
 - ALL: cookie disclaimer no longer increases window height
 - ALL: updated wrong release link
 - EV3: lists containing only one element are generated correctly
 - EV3DEV: fixed connection problems
 - NXT/EV3 SIMULATION: no more duplicated robot views after changing scene
 - CALLIOPE: motors a + b no longer generate invalid code
 - ARDUINO: colours can now be output to screens or serial
 - MBOT: corrected flipped output for line sensor
 - NAO: fixed isPrime
 - WEDO: renamed gyroscope to tilt sensor

## [3.4.1] - 2019-06-27

### New features
### General improvements
 - ALL: deployment and administration improvements
 
### Bug fixes
 - WEDO:  Tiltsensor does work now
 - SENSEBOX: WLAN credentials are supplied by the user separate from programs and never stored in the database
 - CALLIOPE: serial write fixed for colours
 - ALL: simulation can be opened now, if the "to (var) append text (string)" block is used
 - ALL: updated the link to github releases on the start pop-up

## [3.4.0] - 2019-05-28

### New features
 - CALLIOPE: implemented and adopted blocks and code generation for calli:bot
 - CALIIOPE/SIM: new calliope plugin for the new sim execution
 - MICROBIT: serial write implemented
 - NAO: NAO update process supports NAO v5 and v6
 - SENSEBOX: changed code generation to show plot axis and title
 - ALL: robertalab repository moved to openroberta-lab
 
### General improvements
 - ALL: deployment, testing scripting improvements
 - ALL: integration tests, logging init.
 - ALL: refactoring for containerised deployments
 - ALL: configuration block names now correctly work with upper/lower case names
 - CALLIOPE: improved codegeneration for calli:bot
 
### Bug fixes
 - EV3: if in java code generation now surrounded with parens
 - BOB3: fixed rgb function
 - SENSEBOX: for plotting the plot is cleared and not the display (axes are not removed anymore when using clear plot block, but plot data is)
 - SENSEBOX: added missing RobertaFunctions header for code generation
 - CALLIOPE: serial write fixed
 - ARDUINO: swapped "RST" and "SDA" in the code generation and in tests

## [3.3.1] - 2019-04-02

Hotfix

### Bug fixes
 - SENSEBOX: field names added in configuration of SenseBox

## [3.3.0] - 2019-03-29

### New features
 - CALLIOPE: display deactivation as switch LED matrix on/off block under Pin section of Action toolbox
 - CALLIOPE: SHT31 humidity sensor
 - SENSEBOX: text mode for OLED display
 - SENSEBOX: screen off block for OLED display
 - WEDO: execution of programs can be aborted (play button changes to stop button)

### General improvements
 - ALL: email address for support changed to support-o-r@iais.fraunhofer.de
 - ALL: play button does not get activated too soon after the press
 - ALL: trashcan is now bigger for easier block deletion
 - ALL: colour data type handling improved
 - ALL: server-side code structural improvements
 - WEDO: interpreter improved (NodeJS vs Browser JS code specifics)
 - CALLIOPE: background images updated
 - SENSEBOX: libraries updated
 - SENSEBOX: label for OLED screen changed from OLED I2C to OLED Display I2C
 - SENSEBOX: checkers for missing configuration components added
 - SENSEBOX: correct C++ STL port used now

### Bug fixes
 - ALL: get block from lists toolbox does not pop-out after code generation or program run
 - ALL: simulation in IE11 is working again
 - ARDUINO: get analog/digital value block does not destroy the program anymore
 - ARDUINO/CALLIOPE: functions returning lists now have correct return type
 - ARDUINO/CALLIOPE: insert at last position implemented correctly
 - CALLIOPE: saved programs with wait until button A/B pressed block behave correctly now
 - CALLIOPE: animate block now accepts list variable declaration as an input
 - CALLIOPE/MICROBIT/SENSEBOX: program download modal window’s header displays correct robot name after switching the robot system
 - SENSEBOX: gyro sensor block is not resetting anymore after code generation or program run
 - SIMULATION: resetting, refreshing and restarting programs in multiple robot simulation
 - SIMULATION: buttons and displays are connected to corresponding robots in multiple robot simulation
 - NXT/EV3 SIMULATION: light sensor fixed
 - NXT/EV3: bluetooth send/receive block no longer reset after opening code view or compiling the program
 - EV3: middle motor does not crash code generation for dev variant
 - EV3/MICROBIT/NAO: strings are escaped properly now
 - BOB3: arm sensor on all models behaves correctly now
