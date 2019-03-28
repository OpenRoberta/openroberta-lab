# Changelog
All notable changes to this project will be documented in this file. 

## [Unreleased]

### Fixed
 - issue #N/A:  naming problem UMD <-> AMD in WeDo interpreter
 - issue #N/A:  integration tests can run completely without stopping on the first fail (3646bb5c)
 - issue #970:  template type argument removed from the return type of functions (artifact from previous implementation of lists), tests added
 - issue #981:  resetting, refreshing and restarting programs in multiple robot simulation
 - issue #981:  buttons and displays are connected to corresponding robots in multiple robot simulation
 - issue #1015: for arduino-based and mbed robots insert at last position is implemented using .push_back() method
 - issue #1112: server gets and sets the mutation for in list get block to preserver type information so the block does not pop-out
 - issue #1119: program download modal window's header displays correct robot name after switching the robot system
 - issue #1122: bluetooth send/receive block no longer reset after opening code view or compiling the program (reverse transformation corrections)
 - issue #1125: BOB3 revision number in the library is hardcoded to 103 and not read from flash anymore
 - issue #1138: reverse transformation of gyro sensor for sensebox, so the blockly block does not get reset
 - issue #1142: all ES6 specific code removed from simulation, so it is compatible with IE11 again
 - issue #1160: string constant escaping fixed for java based robots
 - issue #1166: get analog/digital value block does not destroy the program anymore
 - issue #1168: light sensor in ev3 and nxt simulation
 - issue #1170: saved programs for Calliope that contain wait blocks with button A/B behave correctly now
 - issue #1175: brace-enclosed initiliser lists are now properly converted to arrays for usage with animate block in Calliope

### Added
 - issue #N/A:  new plugin for Calliope simulation (a45bcf72)
 - issue #1118: display deactivation for Calliope as switch LED matrix on/off block under Pin section of Action toolbox
 - issue #1124: new flag in the http session to give better feedback of the robot's state during compilation, so the play button does not become active before needed
 - issue #1138: sensebox OLED screen support for textual and plotting modes
 - issue #1138: missing OLED screen off block to the sensebox toolboxes
 - issue #1138: unit tests for OLED screen (text mode, off block), write to SD card action, send data to OpenSenseMap action, serial print, buzzer, LED, RGB LED, in-built BMX055 sensor, external sensors (HDC1080, BME280, VEML/TSL), button, sound, ultrasonic and light sensors for sensebox
 - issue #1138: checkers for missing configuration components for sensebox
 - issue #1152: execution of WeDo programs can be aborted
 - issue #1164: raspberry pi plugin
 - issue #1165: SHT31 sensor (humidity) support for Calliope, includes code generation, blockly changes and unit tests

### Changed
 - issue #N/A:  email address for support changed to support-o-r@iais.fraunhofer.de
 - issue #N/A:  background images for Calliope changed
 - issue #1138: sensebox libraries updated
 - issue #1138: sensebox default configuration updated
 - issue #1138: correct C++ STL (ARM port, not ArduinoSTL) is used for sensebox
 - issue #1138: label for OLED screen changed from OLED I2C to OLED Display I2C for sensebox
 - issue #1138: default value type for send data inputs changed from string to number for sensebox, has effect on empty inputs
 - issue #1157: colour handling re-designed to make it simpler
 - issue #1159: trashcan is now bigger for easier block delition
 - issue #1160: string constants are put in single quotes instead of double
 - issue #1166: read/write pin action classes pulled from RobotMbed to Robot project
 - issue #1167: program download modal window now filled with JavaScript, table HTML removed from index.html and custom messages defined for sensebox. Affects systems with auto connection: microbit, calliope 2016 and 2017, sensebox.
 - issue #1168: light sensor mode changed from RED to LIGHT due to server refactoring, affects ev3 and nxt
 - issue #1171: direction and regulation are set to false for other power consumers, affected ev3dev

### Removed
 - issue #1166: read/write pin action classes from RobotArdu plugin
 - issue #1171: side property is removed for the middle motor, affected ev3dev
 
### Deprecated
### Security

## [3.2.1] - 2019-02-27

Hotfix

### Fixed
 - Bob3 connection type changed from token to arduinoAgentOrToken

## [3.2.0] - 2019-02-20

### Fixed
 - issue #1101: python functions would now always have return statement

### Added
 - issue #963:  chinese and swedish translations
 - issue #1107: initial sensebox support
 - issue #1131: sensebox and mBot background images

### Changed
 - issue #N/A:  WeDo now uses colour numbers instead of colour constants
 - issue #970:  template type argument removed from the return type of functions (artifact from previous implementation of lists), calliope only
 - issue #1012: a new ArrayList is now generated from a subList call to make a copy, instead of a view for Java based robots
 - issue #1132: ev3lejosv0 internally mapped to ev3lejos, so old robots could connect
 - issue #1133: directory for storing user project programs on the server renamed (src->source)

### Removed
### Deprecated
### Security

## [3.1.1] - 2019-02-19

Hotfix

### Fixed
 - issue #1130: python scripts generated for ev3dev are stored on the server for further retreval

## [3.1.0] - 2019-02-17

### Fixed
### Added
### Changed
### Removed
### Deprecated
### Security

## [3.0.3] - 2018-12-22

hotfix

### Fixed
### Added
### Changed
### Removed
### Deprecated
### Security

## [3.0.2] - 2018-10-09

hotfix

### Fixed
### Added
### Changed
### Removed
### Deprecated
### Security

## [3.0.1] - 2018-09-05

### Fixed
### Added
### Changed
### Removed
### Deprecated
### Security

## [3.0.0] - 2018-09-03

### Fixed
### Added
### Changed
### Removed
### Deprecated
### Security

## [2.8.1] - 2018-07-18

### Fixed
### Added
### Changed
### Removed
### Deprecated
### Security
