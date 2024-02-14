import spike
import math
from spike.control import wait_for_seconds, wait_until
TRACKWIDTH = 11.5
diff_drive = spike.MotorPair('A', 'B')
diff_drive.set_motor_rotation(5.6 * math.pi, 'cm')
hub = spike.PrimeHub()

def run():
    # drive
    diff_drive.move(10, 'cm', 0, int(30))
    diff_drive.move(10, 'cm', 0, int(-(30)))
    diff_drive.start(0, int(30))
    wait_for_seconds(500/1000)
    diff_drive.start_at_power(int(-(30)), 0)
    wait_for_seconds(500/1000)
    diff_drive.set_stop_action('brake')
    diff_drive.stop()
    # turn
    diff_drive.move(20 * math.pi / 360 * TRACKWIDTH, 'cm', 100, int(30))
    diff_drive.move(20 * math.pi / 360 * TRACKWIDTH, 'cm', -100, int(30))
    diff_drive.start(100, int(30))
    wait_for_seconds(500/1000)
    diff_drive.start_at_power(int(30), -100)
    wait_for_seconds(500/1000)
    diff_drive.set_stop_action('coast')
    diff_drive.stop()
    # steer
    diff_drive.move_tank(20, 'cm', int(10), int(30))
    diff_drive.move_tank(-(20), 'cm', int(10), int(30))
    diff_drive.start_tank(int(10), int(30))
    wait_for_seconds(500/1000)
    diff_drive.start_tank_at_power(int(-(10)), int(-(30)))
    wait_for_seconds(500/1000)
    diff_drive.set_stop_action('coast')
    diff_drive.stop()

def main():
    try:
        run()
    except Exception as e:
        hub.light_matrix.show_image('SAD')

main()