import cyberpi, mbot2, mbuild
import time
import math, random

_trackWidth = 11.5
_circumference = 20.420352248333657
_diffPortsSwapped = False

_colors = {
            "red": (204,0,0),
            "yellow": (255,255,0),
            "green": (51,204,0),
            "cyan": (51,255,255),
            "blue": (51,102,255),
            "purple": (204,51,204),
            "white": (255,255,255),
            "black": (0,0,0)
        }

def diffDriveFor(rpmL, rpmR, distance):
    timeToWait = getTimeToWait(rpmL, rpmR, distance)
    if distance < 0:
        rpmL = -rpmL
        rpmR = -rpmR
    if _diffPortsSwapped:
        mbot2.drive_speed(-rpmR, rpmL)
    else:
        mbot2.drive_speed(rpmL, -rpmR)
    time.sleep(timeToWait)
    mbot2.EM_stop()

def getTimeToWait(rpmL, rpmR, distance):
    absoluteDistance = abs(distance)
    speedL = rpmL * _circumference / 60
    speedR = rpmR * _circumference / 60

    resultingSpeed = (speedL + speedR) / 2
    angVel = (speedR - speedL) / (_trackWidth)
    if angVel != 0 and resultingSpeed != 0:
        radius = resultingSpeed / angVel
        absoluteDistance = radius * math.sin(absoluteDistance/radius)
    if resultingSpeed != 0:
        return abs(absoluteDistance / resultingSpeed)
    else:
        return 0

def RGBAsString(rgb):
    r, g, b = rgb
    color_diffs = []
    for color in _colors:
        cr, cg, cb = _colors[color]
        color_diff = math.sqrt(abs(r - cr)**2 + abs(g - cg)**2 + abs(b - cb)**2)
        color_diffs.append((color_diff, color))
    return min(color_diffs)[1]


def ____action():
    ____drive()
    ____move()
    ____display()
    ____lights()
    ____sounds()

def ____move():
    mbot2.EM_set_speed(30, "EM1")
    mbot2.EM_turn((5) * 360, 30, "EM1")
    mbot2.EM_stop("EM1")
    mbot2.EM_set_speed(30, "EM2")
    mbot2.EM_turn((5) * 360, 30, "EM2")
    mbot2.EM_stop("EM2")
    mbot2.EM_turn((5), 30, "EM2")
    mbot2.EM_turn((5), 30, "EM1")

def ____drive():
    diffDriveFor(30, 30, 10)
    diffDriveFor(-(30), -(30), 10)
    mbot2.drive_speed(30, -(30))
    mbot2.drive_speed(-(30),30)
    mbot2.EM_stop()
    mbot2.turn(20, 30)
    mbot2.turn(-(20), 30)
    mbot2.drive_speed(30, 30)
    mbot2.drive_speed(-(30), -(30))
    diffDriveFor(10, 30, 20)
    diffDriveFor(-(10), -(30), 20)
    mbot2.drive_speed(10, -(30))
    mbot2.drive_speed(-(10),30)

def ____display():
    cyberpi.display.show_label("Hallo", 16, int(8 * 0 + 5), int(17 * 0))
    cyberpi.console.println("Hallo")
    cyberpi.display.set_brush((204, 0, 0)[0], (204, 0, 0)[1], (204, 0, 0)[2])
    cyberpi.console.clear()
    print("Hallo")

def ____sounds():
    cyberpi.audio.play_tone(int(300), 100 * 0.001)
    cyberpi.audio.set_vol(cyberpi.audio.get_vol())
    cyberpi.audio.play_tone(int(138.591), 2000 * 0.001)
    cyberpi.audio.play_tone(int(130.813), 1000 * 0.001)
    cyberpi.audio.play_tone(int(146.832), 500 * 0.001)
    cyberpi.audio.play_tone(int(987.767), 250 * 0.001)
    cyberpi.audio.play_tone(int(554.365), 125 * 0.001)
    cyberpi.audio.play_record()

def ____lights():
    cyberpi.led.on((204, 0, 0)[0], (204, 0, 0)[1], (204, 0, 0)[2], 1)
    cyberpi.led.off(1)
    cyberpi.led.on((204, 0, 0)[0], (204, 0, 0)[1], (204, 0, 0)[2], 2)
    cyberpi.led.off(2)
    cyberpi.led.on((204, 0, 0)[0], (204, 0, 0)[1], (204, 0, 0)[2], 3)
    cyberpi.led.off(3)
    cyberpi.led.on((204, 0, 0)[0], (204, 0, 0)[1], (204, 0, 0)[2], 4)
    cyberpi.led.off(4)
    cyberpi.led.on((204, 0, 0)[0], (204, 0, 0)[1], (204, 0, 0)[2], 5)
    cyberpi.led.off(5)
    cyberpi.led.on((204, 0, 0)[0], (204, 0, 0)[1], (204, 0, 0)[2], "all")
    cyberpi.led.off("all")
    cyberpi.led.set_bri(50)
    mbuild.ultrasonic2.set_bri(50, 1, 1)
    mbuild.ultrasonic2.set_bri(50, 2, 1)
    mbuild.ultrasonic2.set_bri(50, 3, 1)
    mbuild.ultrasonic2.set_bri(50, 4, 1)
    mbuild.ultrasonic2.set_bri(50, 5, 1)
    mbuild.ultrasonic2.set_bri(50, 6, 1)
    mbuild.ultrasonic2.set_bri(50, 7, 1)
    mbuild.ultrasonic2.set_bri(50, 8, 1)
    mbuild.ultrasonic2.set_bri(50, "all", 1)
    mbuild.quad_rgb_sensor.set_led(RGBAsString((204, 0, 0)), 1)
    mbuild.quad_rgb_sensor.off_led(1)

def run():
    ____action()

def main():
    try:
        run()
    except Exception as e:
        cyberpi.display.show_label("Exeption on Mbot 2", 16, int(8 * 0 + 5), int(17 * 0))
        cyberpi.display.show_label(e, 16, int(8 * 0 + 5), int(17 * 1))
        raise
    finally:
        mbot2.motor_stop("all")
        mbot2.EM_stop("all")
main()