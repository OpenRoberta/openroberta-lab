from pybricks.hubs import PrimeHub
from pybricks.parameters import Side
from pybricks.tools import Matrix, vector
import umath as math
import urandom as random

def display_text(text):
    text_list = list(text)
    for idx,c in enumerate(text_list):
        if ord(c) < 33 or ord(c) > 125:
            text_list[idx] = '?'
    hub.display.text("".join(text_list))



hub = PrimeHub()
hub.imu.reset_heading(0)


def run():
    while True:
        display_text(str("".join(str(arg) for arg in ["x", hub.imu.tilt()[0], "y", hub.imu.tilt()[1], "z", int(hub.imu.heading()%180 if abs(hub.imu.heading()%360) < 180 else -abs(hub.imu.heading()%360 - 360))])))

def main():
    try:
        run()
    except Exception as e:
        while True:
            hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))

main()