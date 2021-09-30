import cyberpi, mbot2, mbuild
import time
import math, random
_timer1 = cyberpi.timer.get()
_timer2 = cyberpi.timer.get()
_timer3 = cyberpi.timer.get()
_timer4 = cyberpi.timer.get()
_timer5 = cyberpi.timer.get()

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


___numVar = 0
___colourVar = (204, 0, 0)
___numList = []
___booleanVar = True
def sensors():
    global _timer1, _timer2, _timer3, _timer4, _timer5, ___numVar, ___colourVar, ___numList, ___booleanVar
    ___numVar = mbuild.ultrasonic2.get(2)
    ___numVar = mbuild.ultrasonic2.get(1)
    ___colourVar = _colors[mbuild.quad_rgb_sensor.get_color_sta("L1", 1)]
    ___numVar = mbuild.quad_rgb_sensor.get_light("L1", 1)
    ___numList = [mbuild.quad_rgb_sensor.get_red("L1", 1), mbuild.quad_rgb_sensor.get_green("L1", 1), mbuild.quad_rgb_sensor.get_blue("L1", 1)]
    ___colourVar = _colors[mbuild.quad_rgb_sensor.get_color_sta("L1", 2)]
    ___colourVar = _colors[mbuild.quad_rgb_sensor.get_color_sta("L2", 2)]
    ___colourVar = _colors[mbuild.quad_rgb_sensor.get_color_sta("R1", 2)]
    ___colourVar = _colors[mbuild.quad_rgb_sensor.get_color_sta("R2", 2)]
    ___numVar = mbuild.quad_rgb_sensor.get_line_sta("all", 1)
    ___numVar = mbuild.quad_rgb_sensor.get_line_sta("all", 2)
    ___numVar = mbuild.ultrasonic2.get(1)
    ___numVar = mbuild.ultrasonic2.get(2)
    ___numVar = cyberpi.get_loudness()
    ___booleanVar = cyberpi.controller.is_press("up")
    ___booleanVar = cyberpi.controller.is_press("down")
    ___booleanVar = cyberpi.controller.is_press("left")
    ___booleanVar = cyberpi.controller.is_press("right")
    ___booleanVar = cyberpi.controller.is_press("middle")
    ___booleanVar = cyberpi.controller.is_press("any_direction")
    ___booleanVar = cyberpi.controller.is_press("A")
    ___booleanVar = cyberpi.controller.is_press("B")
    ___numVar = cyberpi.get_bri()
    ___numVar = cyberpi.get_rotation("x")
    ___numVar = cyberpi.get_acc("x")/ 9,80665
    ___numVar = cyberpi.get_acc("y")/ 9,80665
    ___numVar = cyberpi.get_acc("z")/ 9,80665
    ___numVar = ((cyberpi.timer.get() - _timer1)*1000)
    ___numVar = ((cyberpi.timer.get() - _timer2)*1000)
    ___numVar = ((cyberpi.timer.get() - _timer3)*1000)
    ___numVar = ((cyberpi.timer.get() - _timer4)*1000)
    ___numVar = ((cyberpi.timer.get() - _timer5)*1000)
    ___numVar = mbot2.EM_get_angle("EM1")
    ___numVar = (mbot2.EM_get_angle("EM1") / 360)
    ___numVar = mbot2.EM_get_angle("EM1")
    ___numVar = mbot2.EM_get_angle("EM2")
    ___numVar = (mbot2.EM_get_angle("EM2") / 360)
    ___numVar = mbot2.EM_get_angle("EM2")
    cyberpi.audio.record()
    cyberpi.audio.stop_record()
    cyberpi.reset_rotation("x")
    cyberpi.reset_rotation("y")
    cyberpi.reset_rotation("z")
    cyberpi.reset_rotation("all")
    _timer1 = cyberpi.timer.get()
    _timer2 = cyberpi.timer.get()
    _timer3 = cyberpi.timer.get()
    _timer4 = cyberpi.timer.get()
    _timer5 = cyberpi.timer.get()
    mbot2.EM_reset_angle("EM1") 
    mbot2.EM_reset_angle("EM2") 

def wait_until():
    global _timer1, _timer2, _timer3, _timer4, _timer5, ___numVar, ___colourVar, ___numList, ___booleanVar
    while True:
        if cyberpi.controller.is_press("A") == True:
            break
    while True:
        if mbuild.ultrasonic2.get(1) < 30:
            break
    while True:
        if cyberpi.get_loudness() > 50:
            break
    while True:
        if cyberpi.controller.is_press("up") == True:
            break
    while True:
        if cyberpi.get_bri() < 30:
            break
    while True:
        if cyberpi.get_rotation("x") > 90:
            break
    while True:
        if cyberpi.get_acc("x")/ 9,80665 > 30:
            break
    while True:
        if ((cyberpi.timer.get() - _timer1)*1000) > 500:
            break
    while True:
        if _colors[mbuild.quad_rgb_sensor.get_color_sta("L1", 1)] == (179, 0, 6):
            break
    while True:
        if mbuild.quad_rgb_sensor.get_light("L1", 1) < 50:
            break
    while True:
        if mbuild.quad_rgb_sensor.get_line_sta("all", 1) < 30:
            break
    while True:
        if mbot2.EM_get_angle("EM1") > 180:
            break
    while True:
        if (mbot2.EM_get_angle("EM1") / 360) > 2:
            break

def run():
    global _timer1, _timer2, _timer3, _timer4, _timer5, ___numVar, ___colourVar, ___numList, ___booleanVar
    sensors()
    wait_until()

def main():
    try:
        run()
    except Exception as e:
        raise
    finally:
        mbot2.motor_stop("all")
        mbot2.EM_stop("all")
main()