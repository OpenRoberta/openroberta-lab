import spike
import math
from spike.control import wait_for_seconds, wait_until

def get_sample_ultrasonic(sensor):
    sample = sensor.get_distance_cm(False)
    if (sample is None):
        return 999
    return sample


motorC = spike.Motor('C')
ultrasonic_sensor_U = spike.DistanceSensor('D')
ultrasonic_sensor_U2 = spike.DistanceSensor('E')
hub = spike.PrimeHub()

def run():
    motorC.run_for_degrees(180, int(50))
    print(get_sample_ultrasonic(ultrasonic_sensor_U))
    print(get_sample_ultrasonic(ultrasonic_sensor_U2))
    print(motorC.get_degrees_counted())

def main():
    try:
        run()
    except Exception as e:
        hub.light_matrix.show_image('SAD')

main()