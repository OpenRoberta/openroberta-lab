import spike
import math
from spike.control import wait_for_seconds, wait_until

touch_sensor_B = spike.ForceSensor('F')
hub = spike.PrimeHub()

___Element2 = ['pink', 'azure', 'black']

def set_status_light(color):
    if (color is None):
        color = 'black'
    hub.status_light.on(color)

def run():
    global ___Element2
    while True:
        if touch_sensor_B.is_pressed():
            break
        if hub.right_button.is_pressed():
            break
    for ___Element in ___Element2:
        set_status_light(___Element);
        wait_for_seconds(500/1000)

def main():
    try:
        run()
    except Exception as e:
        hub.light_matrix.show_image('SAD')

main()