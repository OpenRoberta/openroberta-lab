# Release notes

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
 - ALL: trashcan is now bigger for easier block delition
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
 - CALLIOPE: saved programs with wait until button A/B pressed block behave correclty now
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
