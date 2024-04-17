from pybricks.hubs import PrimeHub
from pybricks.pupdevices import ColorSensor
from pybricks.parameters import Port, Color
from pybricks.tools import Matrix, wait
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


color_sensor_F = ColorSensor(Port.C)
color_sensor_F.detectable_colors([Color.MAGENTA, Color.BLUE, Color.AZURE, Color.CYAN, Color.YELLOW, Color.RED, Color.BLACK, Color.WHITE, Color.NONE])
hub = PrimeHub()


def get_color(color_sensor):
    hsv = color_sensor.hsv()
    color = color_sensor.color()
    if(color == Color.BLACK or color == Color.NONE):
        if hsv.v > 5 or hsv.s > 10:
            return Color.BLACK
    elif abs(abs(hsv.h) - abs(color.h))%330 < 30 or color == Color.WHITE:
        return color
    return Color.NONE

def hub_light_on(color):
    if color != Color.BLACK and color != Color.WHITE and color != Color.NONE :
        hub.light.on(Color(color.h,100,100))
    elif color == Color.BLACK or color == Color.NONE:
        hub.light.on(Color(0,0,0))
    elif color == Color.WHITE :
        hub.light.on(Color(0,0,100))

def run():
    hub_light_on(Color.MAGENTA)
    wait(500)
    hub.light.off()
    wait(500)
    while True:
        hub_light_on(get_color(color_sensor_F))

def main():
    try:
        run()
    except Exception as e:
        while True:
            hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))

main()