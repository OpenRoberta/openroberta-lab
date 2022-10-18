import spike
import math
from spike.control import wait_for_seconds, wait_until
motorB = spike.Motor('B')
motorA = spike.Motor('A')

def run():
    # motor
    motorB.run_for_rotations(1, 30)
    motorA.run_for_degrees(360, 30)
    motorB.start(30)
    wait_for_seconds(500/1000)
    motorB.set_stop_action('coast')
    motorB.stop()
    motorA.start_at_power(-30)
    wait_for_seconds(500/1000)
    motorA.set_stop_action('brake')
    motorA.stop()

def main():
    try:
        run()
    except Exception as e:
        hub.light_matrix.show_image('SAD')

main()