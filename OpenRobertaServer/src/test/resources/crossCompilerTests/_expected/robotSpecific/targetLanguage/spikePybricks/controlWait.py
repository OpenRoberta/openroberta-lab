from pybricks.hubs import PrimeHub
from pybricks.pupdevices import ForceSensor
from pybricks.parameters import Port
from pybricks.tools import Matrix, wait
import umath as math
import urandom as random


touch_sensor_B = ForceSensor(Port.F)
hub = PrimeHub()


def run():
    while True:
        if True:
            break
    wait(500)
    while True:
        if touch_sensor_B.pressed() == True:
            break

def main():
    try:
        run()
    except Exception as e:
        while True:
            hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))

main()