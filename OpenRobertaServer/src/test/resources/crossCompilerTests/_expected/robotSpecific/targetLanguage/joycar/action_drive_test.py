import microbit
import random
import math

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

def run():
    global timer1
    drive(70, 70)
    microbit.sleep(1000)
    drive(0, 0)
    drive(100, 100)
    microbit.sleep(1000)
    drive(0, 0)
    microbit.sleep(500)
    drive(-70, -70)
    microbit.sleep(1000)
    drive(0, 0)
    drive(-100, -100)
    microbit.sleep(1000)
    drive(0, 0)
    microbit.sleep(500)
    drive(70, -70)
    microbit.sleep(1000)
    drive(0, 0)
    drive(-70, --70)
    microbit.sleep(1000)
    drive(0, 0)
    drive(-70, 70)
    microbit.sleep(1000)
    drive(0, 0)
    drive(--70, -70)
    microbit.sleep(1000)
    drive(0, 0)
    microbit.sleep(500)
    drive(100, 70)
    microbit.sleep(1000)
    drive(0, 0)
    drive(70, 100)
    microbit.sleep(1000)
    drive(0, 0)

def main():
    try:
        run()
    except Exception as e:
        raise
    finally:
        drive(0, 0)

if __name__ == "__main__":
    main()