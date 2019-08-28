package de.fhg.iais.roberta.visitor.collect;

/**
 * Blockly Blocks that need an extra helper method in the source code
 */
public enum EdisonMethods {
    OBSTACLEDETECTION, //Obstacle detection
    IRSEND, //IR sender
    IRSEEK, //IR seeker
    MOTORON, //Motor on / motor on for... block
    SHORTEN, //shorten a number for Edisons drive() methods
    CURVE, //for the steer block
    DIFFDRIVE, //for driving
    DIFFTURN, //for turning
    READDIST //to read the distance for the curve block
}