#!/usr/bin/python

import math
import time
import random
from roberta import Hal
h = Hal()

from roberta import FaceRecognitionModule
faceRecognitionModule = FaceRecognitionModule("faceRecognitionModule")


___numberVar = 0
___booleanVar = True
___stringVar = ""
___colourVar = 0xff0000
___numberList = [0, 0]
___booleanList = [True, True]
___stringList = ["", ""]
___colourList = [0xff0000, 0xff0000]
def ____action():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    ____move()
    ____walk()
    ____animation()
    ____sounds()
    ____vision()
    ____lights()

def ____move():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    h.applyPosture("Rest")
    h.applyPosture("Crouch")
    h.applyPosture("Stand")
    h.applyPosture("StandInit")
    h.applyPosture("StandZero")
    h.applyPosture("Sit")
    h.applyPosture("SitRelax")
    h.applyPosture("LyingBelly")
    h.applyPosture("LyingBack")
    h.hand("RHand", 1)
    h.hand("RHand", 2)
    h.hand("LHand", 1)
    h.hand("LHand", 2)
    h.moveJoint("HeadYaw", ___numberVar, 1)
    h.moveJoint("HeadYaw", ___numberVar, 2)
    h.moveJoint("HeadPitch", ___numberVar, 1)
    h.moveJoint("HeadPitch", ___numberVar, 2)
    h.moveJoint("LShoulderPitch", ___numberVar, 1)
    h.moveJoint("LShoulderPitch", ___numberVar, 2)
    h.moveJoint("RShoulderPitch", ___numberVar, 1)
    h.moveJoint("RShoulderPitch", ___numberVar, 2)
    h.moveJoint("LShoulderRoll", ___numberVar, 1)
    h.moveJoint("LShoulderRoll", ___numberVar, 2)
    h.moveJoint("RShoulderRoll", ___numberVar, 1)
    h.moveJoint("RShoulderRoll", ___numberVar, 2)
    h.moveJoint("LElbowYaw", ___numberVar, 1)
    h.moveJoint("LElbowYaw", ___numberVar, 2)
    h.moveJoint("RElbowYaw", ___numberVar, 1)
    h.moveJoint("RElbowYaw", ___numberVar, 2)
    h.moveJoint("LElbowRoll", ___numberVar, 1)
    h.moveJoint("LElbowRoll", ___numberVar, 2)
    h.moveJoint("RElbowRoll", ___numberVar, 1)
    h.moveJoint("RElbowRoll", ___numberVar, 2)
    h.moveJoint("LWristYaw", ___numberVar, 1)
    h.moveJoint("LWristYaw", ___numberVar, 2)
    h.moveJoint("RWristYaw", ___numberVar, 1)
    h.moveJoint("RWristYaw", ___numberVar, 2)
    h.moveJoint("LHand", ___numberVar, 1)
    h.moveJoint("LHand", ___numberVar, 2)
    h.moveJoint("RHand", ___numberVar, 1)
    h.moveJoint("RHand", ___numberVar, 2)
    h.moveJoint("LHipYawPitch", ___numberVar, 1)
    h.moveJoint("LHipYawPitch", ___numberVar, 2)
    h.moveJoint("RHipYawPitch", ___numberVar, 1)
    h.moveJoint("RHipYawPitch", ___numberVar, 2)
    h.moveJoint("LHipRoll", ___numberVar, 1)
    h.moveJoint("LHipRoll", ___numberVar, 2)
    h.moveJoint("RHipRoll", ___numberVar, 1)
    h.moveJoint("RHipRoll", ___numberVar, 2)
    h.moveJoint("LHipPitch", ___numberVar, 1)
    h.moveJoint("LHipPitch", ___numberVar, 2)
    h.moveJoint("RHipPitch", ___numberVar, 1)
    h.moveJoint("RHipPitch", ___numberVar, 2)
    h.moveJoint("LKneePitch", ___numberVar, 1)
    h.moveJoint("LKneePitch", ___numberVar, 2)
    h.moveJoint("RKneePitch", ___numberVar, 1)
    h.moveJoint("RKneePitch", ___numberVar, 2)
    h.moveJoint("LAnklePitch", ___numberVar, 1)
    h.moveJoint("LAnklePitch", ___numberVar, 2)
    h.moveJoint("RAnklePitch", ___numberVar, 1)
    h.moveJoint("RAnklePitch", ___numberVar, 2)
    h.moveJoint("LAnkleRoll", ___numberVar, 1)
    h.moveJoint("LAnkleRoll", ___numberVar, 2)
    h.moveJoint("RAnkleRoll", ___numberVar, 1)
    h.moveJoint("RAnkleRoll", ___numberVar, 2)
    h.stiffness("Body", 1)
    h.stiffness("Body", 2)
    h.stiffness("Head", 1)
    h.stiffness("Head", 2)
    h.stiffness("Arms", 1)
    h.stiffness("Arms", 2)
    h.stiffness("LArm", 1)
    h.stiffness("LArm", 2)
    h.stiffness("RArm", 1)
    h.stiffness("RArm", 2)
    h.stiffness("Legs", 1)
    h.stiffness("Legs", 2)
    h.stiffness("LLeg", 1)
    h.stiffness("LLeg", 2)
    h.stiffness("RLeg", 1)
    h.stiffness("RLeg", 2)

def ____walk():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    h.walk(___numberVar, 0, 0)
    h.walk(-___numberVar, 0, 0)
    h.walk(0, 0,___numberVar)
    h.walk(0, 0,-___numberVar)
    h.walk(___numberVar,___numberVar,___numberVar)
    h.walkAsync(___numberVar, ___numberVar, ___numberVar)
    h.stop()

def ____animation():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    h.taiChi()
    h.wave()
    h.blink()
    h.wipeForehead()
    h.setAutonomousLife('ON')
    h.setAutonomousLife('OFF')
    h.pointLookAt('point', 0, ___numberVar, ___numberVar, ___numberVar, ___numberVar)
    h.pointLookAt('point', 1, ___numberVar, ___numberVar, ___numberVar, ___numberVar)
    h.pointLookAt('point', 2, ___numberVar, ___numberVar, ___numberVar, ___numberVar)
    h.pointLookAt('look', 0, ___numberVar, ___numberVar, ___numberVar, ___numberVar)
    h.pointLookAt('look', 1, ___numberVar, ___numberVar, ___numberVar, ___numberVar)
    h.pointLookAt('look', 2, ___numberVar, ___numberVar, ___numberVar, ___numberVar)

def ____sounds():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    h.say(str(___stringVar))
    h.say(str(___stringVar),___numberVar,___numberVar)
    h.say(str(h.getLanguage()))
    h.setLanguage("German")
    h.setLanguage("English")
    h.setLanguage("French")
    h.setLanguage("Japanese")
    h.setLanguage("Chinese")
    h.setLanguage("Spanish")
    h.setLanguage("Korean")
    h.setLanguage("Italian")
    h.setLanguage("Dutch")
    h.setLanguage("Finnish")
    h.setLanguage("Turkish")
    h.setLanguage("Arabic")
    h.setLanguage("Czech")
    h.setLanguage("Portuguese")
    h.setLanguage("Brazilian")
    h.setLanguage("Swedish")
    h.setLanguage("Danish")
    h.setLanguage("Norwegian")
    h.setLanguage("Greek")
    h.say(str(h.getVolume()))
    h.setVolume(___numberVar)

def ____vision():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    h.say(str(faceRecognitionModule.learnFace(___stringVar)))
    faceRecognitionModule.forgetFace(___stringVar)
    h.takePicture("Top", ___stringVar)
    h.takePicture("Bottom", ___stringVar)
    h.recordVideo(0, "Top", ___numberVar, ___stringVar)
    h.recordVideo(0, "Bottom", ___numberVar, ___stringVar)
    h.recordVideo(1, "Top", ___numberVar, ___stringVar)
    h.recordVideo(1, "Bottom", ___numberVar, ___stringVar)
    h.recordVideo(2, "Top", ___numberVar, ___stringVar)
    h.recordVideo(2, "Bottom", ___numberVar, ___stringVar)

def ____lights():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    h.setLeds("FaceLeds", 0xff0000, 0.1)
    h.setLeds("LeftFaceLeds", 0xffff00, 0.1)
    h.setLeds("RightFaceLeds", 0x33ff33, 0.1)
    h.setLeds("LeftFootLeds", 0x33ccff, 0.1)
    h.setLeds("RightFootLeds", 0xffffff, 0.1)
    h.setLeds("AllLeds", 0x000000, 0.1)
    h.setIntensity("BrainLeds", ___numberVar)
    h.setIntensity("EarLeds", ___numberVar)
    h.setIntensity("LeftEarLeds", ___numberVar)
    h.setIntensity("RightEarLeds", ___numberVar)
    h.setIntensity("ChestLeds", ___numberVar)
    h.ledOff("FaceLeds")
    h.ledOff("LeftFaceLeds")
    h.ledOff("RightFaceLeds")
    h.ledOff("LeftFootLeds")
    h.ledOff("RightFootLeds")
    h.ledOff("EarLeds")
    h.ledOff("LeftEarLeds")
    h.ledOff("RightEarLeds")
    h.ledOff("ChestLeds")
    h.ledOff("BrainLeds")
    h.ledOff("AllLeds")
    h.ledReset("FaceLeds")
    h.ledReset("LeftFaceLeds")
    h.ledReset("RightFaceLeds")
    h.ledReset("LeftFootLeds")
    h.ledReset("RightFootLeds")
    h.ledReset("EarLeds")
    h.ledReset("LeftEarLeds")
    h.ledReset("RightEarLeds")
    h.ledReset("ChestLeds")
    h.ledReset("BrainLeds")
    h.ledReset("AllLeds")
    h.randomEyes(___numberVar)
    h.rasta(___numberVar)


def run():
    h.setAutonomousLife('ON')
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    ____action()

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        faceRecognitionModule.unsubscribe()
        h.myBroker.shutdown()

if __name__ == "__main__":
    main()