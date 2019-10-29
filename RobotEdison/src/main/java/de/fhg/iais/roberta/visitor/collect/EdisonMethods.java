package de.fhg.iais.roberta.visitor.collect;

public enum EdisonMethods {
    OBSTACLEDETECTION, //Obstacle detection
    IRSEND, //IR sender
    IRSEEK, //IR seeker
    MOTORON, //Motor on / motor on for... block
    SHORTEN, //shorten a number for Edisons drive() methods
    GETDIR, //reverse direction when negative speed is applied
    DIFFCURVE, //for the steer block
    DIFFDRIVE, //for driving
    DIFFTURN, //for turning
}