from pybricks.hubs import PrimeHub
from pybricks.parameters import Color
from pybricks.tools import Matrix, wait
import umath as math
import urandom as random

def hub_light_on(color):
    if color != Color.BLACK and color != Color.WHITE and color != Color.NONE :
        hub.light.on(Color(color.h,100,100))
    elif color == Color.BLACK or color == Color.NONE:
        hub.light.on(Color(0,0,0))
    elif color == Color.WHITE :
        hub.light.on(Color(0,0,100))


Color.MAGENTA = Color(315,50,15)
Color.BLUE = Color(225,20,20)
Color.AZURE = Color(200,20,20)
Color.CYAN = Color(150,20,20)
Color.YELLOW = Color(55,35,35)
Color.RED = Color(350,35,35)
Color.BLACK = Color(0,10,10)
Color.WHITE = Color(0,0,70)


hub = PrimeHub()


def run():
    hub_light_on(Color.MAGENTA)
    wait(500)
    hub_light_on(Color.VIOLET)
    wait(500)
    hub_light_on(Color.BLUE)
    wait(500)
    hub_light_on(Color.CYAN)
    wait(500)
    hub_light_on(Color.GREEN)
    wait(500)
    hub_light_on(Color.YELLOW)
    wait(500)
    hub_light_on(Color.ORANGE)
    wait(500)
    hub_light_on(Color.RED)
    wait(500)
    hub_light_on(Color.BLACK)
    wait(500)
    hub_light_on(Color.WHITE)
    wait(500)
    hub_light_on(Color.NONE)
    wait(500)

def main():
    try:
        run()
    except Exception as e:
        while True:
            hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))

main()