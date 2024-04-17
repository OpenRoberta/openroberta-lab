from pybricks.hubs import PrimeHub
from pybricks.pupdevices import ColorSensor, UltrasonicSensor, ForceSensor
from pybricks.parameters import Port, Color, Button
from pybricks.tools import Matrix, wait, StopWatch
import umath as math
import urandom as random

Color.MAGENTA = Color(315,50,15)
Color.BLUE = Color(225,20,20)
Color.AZURE = Color(200,20,20)
Color.CYAN = Color(150,20,20)
Color.YELLOW = Color(55,35,35)
Color.RED = Color(350,35,35)
Color.BLACK = Color(0,10,10)
Color.WHITE = Color(0,0,70)


touch_sensor_B = ForceSensor(Port.F)
touch_sensor_B = ForceSensor(Port.F)
ultrasonic_sensor_U = UltrasonicSensor(Port.D)
color_sensor_F = ColorSensor(Port.C)
color_sensor_F.detectable_colors([Color.MAGENTA, Color.BLUE, Color.AZURE, Color.CYAN, Color.YELLOW, Color.RED, Color.BLACK, Color.WHITE, Color.NONE])
stopWatch = StopWatch()
hub = PrimeHub()


def display_text(text):
    text_list = list(text)
    for idx,c in enumerate(text_list):
        if ord(c) < 33 or ord(c) > 125:
            text_list[idx] = '?'
    hub.display.text("".join(text_list))

def get_color(color_sensor):
    hsv = color_sensor.hsv()
    color = color_sensor.color()
    if(color == Color.BLACK or color == Color.NONE):
        if hsv.v > 5 or hsv.s > 10:
            return Color.BLACK
    elif abs(abs(hsv.h) - abs(color.h))%330 < 30 or color == Color.WHITE:
        return color
    return Color.NONE

def get_dist(distance_sensor):
    dist = distance_sensor.distance()/10
    if dist >= 200:
        return 999
    else:
        return int(dist)

def hsv2rgb(color):
    h = float(color.h)
    s = float(color.s / 100.0)
    v = float(color.v / 100.0)

    c = v * s
    x = c * (1 - abs(((h/60.0) % 2) - 1))
    m = v - c

    if 0.0 <= h < 60:
        rgb = (c, x, 0)
    elif 0.0 <= h < 120:
        rgb = (x, c, 0)
    elif 0.0 <= h < 180:
        rgb = (0, c, x)
    elif 0.0 <= h < 240:
        rgb = (0, x, c)
    elif 0.0 <= h < 300:
        rgb = (x, 0, c)
    elif 0.0 <= h < 360:
        rgb = (c, 0, x)

    return list(map(lambda n: (n + m) * 255, rgb))  

def run():
    display_text(str((Button.LEFT in hub.buttons.pressed())))
    display_text(str(touch_sensor_B.pressed()))
    display_text(str(get_dist(ultrasonic_sensor_U)))
    while True:
        if get_dist(ultrasonic_sensor_U) < 30:
            break
    display_text(str("Distance below 30cm"))
    while True:
        if touch_sensor_B.pressed() == True:
            break
    display_text(str("Touch Pressed"))
    while True:
        if int(min(touch_sensor_B.force() * 10.0 , 100.0)) > 30:
            break
    display_text(str("Force Above 30%"))
    while True:
        if get_color(color_sensor_F) == Color.WHITE:
            break
    display_text(str("White Detected"))
    while True:
        if color_sensor_F.reflection() < 50:
            break
    display_text(str("Light Above 30%"))
    while True:
        if int(color_sensor_F.ambient()) < 50:
            break
    display_text(str("Ambient Light Above 30%"))
    while True:
        if int(hsv2rgb(color_sensor_F.hsv())[0]/2.55) < 30:
            break
    display_text(str("Red Above 30%"))
    while True:
        if int(hsv2rgb(color_sensor_F.hsv())[1]/2.55) < 30:
            break
    display_text(str("Green Above 30%"))
    while True:
        if int(hsv2rgb(color_sensor_F.hsv())[2]/2.55) < 30:
            break
    display_text(str("Blue Above 30%"))
    while True:
        if (Button.LEFT in hub.buttons.pressed()) == True:
            break
    display_text(str("Button Left Pressed"))
    while True:
        if (Button.RIGHT in hub.buttons.pressed()) == True:
            break
    display_text(str("Button Right Pressed"))
    stopWatch.reset()
    wait(500)
    display_text(str("".join(str(arg) for arg in ["Timer:", stopWatch.time()])))

def main():
    try:
        run()
    except Exception as e:
        while True:
            hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))

main()