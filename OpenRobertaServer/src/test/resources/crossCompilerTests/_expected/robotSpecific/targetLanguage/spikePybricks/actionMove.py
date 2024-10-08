from pybricks.hubs import PrimeHub
from pybricks.pupdevices import Motor
from pybricks.parameters import Port, Direction
from pybricks.tools import Matrix, wait
import umath as math
import urandom as random

def get_deg_sec_from_percent(percent):
    if percent < -100 :
        percent = -100
    if percent > 100 :
        percent = 100
    return int(810.0 * (percent/100.0))


motorA = Motor(Port.A)
motorB = Motor(Port.B)

hub = PrimeHub()


def run():
    motorA.run_angle(rotation_angle = 360 * 1, speed = get_deg_sec_from_percent(30))
    motorA.run_angle(rotation_angle = 360 * -30, speed = get_deg_sec_from_percent(-30))
    motorA.run_angle(rotation_angle = 360, speed = get_deg_sec_from_percent(30))
    motorA.run_angle(rotation_angle = -360, speed = get_deg_sec_from_percent(-30))
    motorA.run(get_deg_sec_from_percent(30))
    wait(500)
    motorA.run(get_deg_sec_from_percent(-30))
    wait(500)
    motorA.dc(-30)
    wait(500)
    motorA.dc(30)
    wait(500)
    motorA.dc(30)
    motorB.dc(30)
    wait(500)
    motorA.stop()
    wait(500)
    motorA.brake()
    wait(500)

def main():
    try:
        run()
    except Exception as e:
        while True:
            hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))

main()