import calliopemini
import random
import math
import machine
import neopixel
from tcs3472 import tcs3472
from sht31 import SHT31
class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()
np = neopixel.NeoPixel(calliopemini.pin_RGB, 3)
color_sensor = tcs3472()
LIGHT_CONST = 40
sht31 = SHT31()


def ____buttons():
    global timer1
    print("Button A (B to Cancel)")
    calliopemini.sleep(500)
    while True:
        print(calliopemini.button_a.is_pressed())
        calliopemini.sleep(100)
        if calliopemini.button_b.is_pressed():
            break
    print("Button B")
    calliopemini.sleep(500)
    while True:
        print(calliopemini.button_b.is_pressed())
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("pin 1")
    calliopemini.sleep(500)
    while True:
        print(calliopemini.pin1.is_touched())
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("pin 3")
    calliopemini.sleep(500)
    while True:
        print(calliopemini.pin3.is_touched())
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break

def ____gesture():
    global timer1
    print("gesture upright?")
    calliopemini.sleep(500)
    while True:
        print(("up" == calliopemini.accelerometer.current_gesture()))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("gesture upside down?")
    calliopemini.sleep(500)
    while True:
        print(("down" == calliopemini.accelerometer.current_gesture()))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("gesture at the front side?")
    calliopemini.sleep(500)
    while True:
        print(("face down" == calliopemini.accelerometer.current_gesture()))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("gesture at the back?")
    calliopemini.sleep(500)
    while True:
        print(("face up" == calliopemini.accelerometer.current_gesture()))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("gesture shaking?")
    calliopemini.sleep(500)
    while True:
        print(("shake" == calliopemini.accelerometer.current_gesture()))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("gesture freely falling?")
    calliopemini.sleep(500)
    while True:
        print(("freefall" == calliopemini.accelerometer.current_gesture()))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break

def ____accelerometer():
    global timer1
    print("accelerometer x")
    calliopemini.sleep(500)
    while True:
        print(calliopemini.accelerometer.get_x())
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("accelerometer y")
    calliopemini.sleep(500)
    while True:
        print(calliopemini.accelerometer.get_y())
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("accelerometer z")
    calliopemini.sleep(500)
    while True:
        print(calliopemini.accelerometer.get_z())
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("accelerometer combined")
    calliopemini.sleep(500)
    while True:
        print(math.sqrt(calliopemini.accelerometer.get_x()**2 + calliopemini.accelerometer.get_y()**2 + calliopemini.accelerometer.get_z()**2))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break

def ____others():
    global timer1
    print("compass angle")
    calliopemini.sleep(500)
    while True:
        print(calliopemini.compass.heading())
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("mic sound")
    calliopemini.sleep(500)
    while True:
        print(int((calliopemini.microphone.sound_level() / 255) * 100))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("timer ")
    calliopemini.sleep(500)
    while True:
        print(( calliopemini.running_time() - timer1 ))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    timer1 = calliopemini.running_time()
    print("timer after reset")
    calliopemini.sleep(500)
    while True:
        print(( calliopemini.running_time() - timer1 ))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("temperature")
    calliopemini.sleep(500)
    while True:
        print(calliopemini.temperature())
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("light")
    calliopemini.sleep(500)
    while True:
        print(round(calliopemini.display.read_light_level() / 2.55))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break

def ____external():
    global timer1
    print("TCS3472 Color")
    calliopemini.sleep(500)
    while True:
        np[1] = (color_sensor.rgb())
        np.show()
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("TCS3472 light")
    calliopemini.sleep(500)
    while True:
        print(int( color_sensor.light() / LIGHT_CONST))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("TCS3472 rgb")
    calliopemini.sleep(500)
    while True:
        print(list(color_sensor.rgb())[0])
        print(list(color_sensor.rgb())[1])
        print(list(color_sensor.rgb())[2])
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("humidity")
    calliopemini.sleep(500)
    while True:
        print(sht31.get_temp_humi("h"))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("humidity temperature")
    calliopemini.sleep(500)
    while True:
        print(sht31.get_temp_humi("t"))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break

def run():
    global timer1
    ____buttons()
    ____gesture()
    ____accelerometer()
    ____others()
    ____external()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()