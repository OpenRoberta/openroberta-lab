import microbit
import random
import math
import music


def drive(speedLeft, speedRight):
    sR = scale(speedRight)
    sL = scale(speedLeft)
    b2 = b3 = b4 = b5 = 0
    if speedRight < 0:
        b2 = b'\x02' + bytes([sR])
        b3 = b'\x03' + bytes([0])
    else:
        b2 = b'\x02' + bytes([0])
        b3 = b'\x03' + bytes([sR])
    if speedLeft < 0:
        b4 = b'\x04' + bytes([sL])
        b5 = b'\x05' + bytes([0])
    else:
        b4 = b'\x04' + bytes([0])
        b5 = b'\x05' + bytes([sL])
    microbit.i2c.write(0x70, b2)
    microbit.i2c.write(0x70, b3)
    microbit.i2c.write(0x70, b4)
    microbit.i2c.write(0x70, b5)

def scale(speed):
    if speed == 0:
        return 0
    else:
        return round(abs(speed) * 446.25 / 255 + 80)

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

microbit.i2c.init(freq=400000, sda=microbit.pin20, scl=microbit.pin19)
microbit.i2c.write(0x70, b'\x00\x01')
microbit.i2c.write(0x70, b'\xE8\xAA')

timer1 = microbit.running_time()

___speed = 0

def ____driveSpeedTest():
    global timer1, ___speed
    # Robot should increase speed from 0 to 100% and decrease the speed again until it starts driving backwards with up                                 too 100% of its speed
    while not (___speed > 90):
        microbit.display.scroll(str(___speed))
        drive(___speed, ___speed)
        microbit.sleep(1000)
        drive(0, 0)
        ___speed = ___speed + 10
    while not (___speed < -100):
        microbit.display.scroll(str(___speed))
        drive(___speed, ___speed)
        microbit.sleep(1000)
        drive(0, 0)
        ___speed = ___speed - 10
    music.pitch(261, 250, microbit.pin16)
    #
    #
    #
    # drive forward with speed 50%
    drive(50, 50)
    microbit.sleep(1000)
    # drive backwards with speed 50%
    drive(-50, -50)
    microbit.sleep(1000)
    # drive forward with speed 50%
    drive(--50, --50)
    microbit.sleep(1000)
    # drive backwards with speed 50%
    drive(-50, -50)
    microbit.sleep(1000)
    drive(0, 0)
    music.pitch(261, 250, microbit.pin16)

def ____turnLeftRightTest():
    global timer1, ___speed
    # Turn left with speed -100% to +100%, so it should first spin right and then left when the speed value is                                 positiv
    ___speed = -100
    while not (___speed > 100):
        microbit.display.scroll(str(___speed))
        drive(-___speed, ___speed)
        microbit.sleep(1000)
        drive(0, 0)
        ___speed = ___speed + 10
    # Turn right with speed -100% to +100%, so it should first spin left and then right when the speed value is                                 positiv
    ___speed = -100
    while not (___speed > 100):
        microbit.display.scroll(str(___speed))
        drive(___speed, -___speed)
        microbit.sleep(1000)
        drive(0, 0)
        ___speed = ___speed + 10
    music.pitch(261, 250, microbit.pin16)
    #
    #
    # changing speed from +50 %to -50%, first right then left wheel
    drive(50, -50)
    microbit.sleep(1000)
    drive(-50, --50)
    microbit.sleep(1000)
    drive(--50, -50)
    microbit.sleep(1000)
    drive(-50, 50)
    microbit.sleep(1000)
    drive(0, 0)
    music.pitch(261, 250, microbit.pin16)

def ____steerDriveTest():
    global timer1, ___speed
    # Only the speed of one wheel changes, first the left wheel from -100% to +100%, after that the right wheel from                                 -100% to +100%.
    ___speed = -100
    while not (___speed > 100):
        microbit.display.scroll(str(___speed))
        drive(___speed, - (___speed))
        microbit.sleep(1000)
        drive(0, 0)
        ___speed = ___speed + 10
    ___speed = -100
    while not (___speed > 100):
        microbit.display.scroll(str(___speed))
        drive(- (___speed), ___speed)
        microbit.sleep(1000)
        drive(0, 0)
        ___speed = ___speed + 10
    # Same thing, just that the mode is set to backwards so the wheels should start turning into the other direction
    ___speed = -100
    while not (___speed > 100):
        microbit.display.scroll(str(___speed))
        drive(-___speed, -- (___speed))
        microbit.sleep(1000)
        drive(0, 0)
        ___speed = ___speed + 10
    ___speed = -100
    while not (___speed > 100):
        microbit.display.scroll(str(___speed))
        drive(-- (___speed), -___speed)
        microbit.sleep(1000)
        drive(0, 0)
        ___speed = ___speed + 10
    music.pitch(261, 250, microbit.pin16)
    #
    #
    # left wheel starts with +50% speed, forward mode, then -50%
    drive(50, 0)
    microbit.sleep(1000)
    drive(-50, 0)
    microbit.sleep(1000)
    # change to backwards mode
    drive(--50, -0)
    microbit.sleep(1000)
    drive(-50, -0)
    microbit.sleep(1000)
    # right wheel starts with +50% speed, forward mode, then -50%
    drive(0, 50)
    microbit.sleep(1000)
    drive(0, -50)
    microbit.sleep(1000)
    # change to backwards mode
    drive(-0, --50)
    microbit.sleep(1000)
    drive(-0, -50)
    drive(0, 0)
    music.pitch(261, 250, microbit.pin16)


def run():
    global timer1, ___speed
    ____steerDriveTest()
    ____driveSpeedTest()
    ____turnLeftRightTest()

def main():
    try:
        run()
    except Exception as e:
        raise
    finally:
        drive(0, 0)

if __name__ == "__main__":
    main()