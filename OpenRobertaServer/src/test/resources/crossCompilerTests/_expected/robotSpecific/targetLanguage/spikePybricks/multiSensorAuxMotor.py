from pybricks.hubs import PrimeHub
from pybricks.pupdevices import Motor, UltrasonicSensor
from pybricks.parameters import Port, Direction
from pybricks.tools import Matrix
import umath as math
import urandom as random

def get_deg_sec_from_percent(percent):
    if percent < -100 :
        percent = -100
    if percent > 100 :
        percent = 100
    return int(810.0 * (percent/100.0))

def get_dist(distance_sensor):
    dist = distance_sensor.distance()/10
    if dist >= 200:
        return 999
    else:
        return int(dist)


motorC = Motor(Port.C)

ultrasonic_sensor_U = UltrasonicSensor(Port.D)
ultrasonic_sensor_U2 = UltrasonicSensor(Port.E)
hub = PrimeHub()


def run():
    motorC.run_angle(rotation_angle = 180, speed = get_deg_sec_from_percent(50))
    print(get_dist(ultrasonic_sensor_U))
    print(get_dist(ultrasonic_sensor_U2))
    print(motorC.angle())

def main():
    try:
        run()
    except Exception as e:
        while True:
            hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))

main()