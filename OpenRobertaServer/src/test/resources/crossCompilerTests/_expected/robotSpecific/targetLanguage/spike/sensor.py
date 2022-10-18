import spike
import math
from spike.control import wait_for_seconds, wait_until, Timer

touch_sensor_B = spike.ForceSensor('F')
touch_sensor_B = spike.ForceSensor('F')
ultrasonic_sensor_U = spike.DistanceSensor('D')
color_sensor_F = spike.ColorSensor('C')
timer = Timer()
hub = spike.PrimeHub()

def get_sample_ultrasonic(sensor):
    sample = sensor.get_distance_cm(False)
    if (sample is None):
        return 999
    return sample

def set_status_light(color):
    if (color is None):
        color = 'black'
    hub.status_light.on(color)

def run():
    # sensors
    timer.reset()
    print(color_sensor_F.get_color())
    print(color_sensor_F.get_reflected_light())
    print(color_sensor_F.get_red() / 10.24)
    print(color_sensor_F.get_green() / 10.24)
    print(color_sensor_F.get_blue() / 10.24)
    print(get_sample_ultrasonic(ultrasonic_sensor_U))
    print(touch_sensor_B.is_pressed())
    print(touch_sensor_B.get_force_percentage())
    print(hub.left_button.is_pressed())
    print(hub.left_button.is_pressed())
    print((hub.motion_sensor.get_orientation() == 'front'))
    print((hub.motion_sensor.get_orientation() == 'back'))
    print((hub.motion_sensor.get_orientation() == 'up'))
    print((hub.motion_sensor.get_orientation() == 'down'))
    print((hub.motion_sensor.get_orientation() == 'rightside'))
    print((hub.motion_sensor.get_orientation() == 'leftside'))
    print((hub.motion_sensor.get_gesture() == 'tapped'))
    print((hub.motion_sensor.get_gesture() == 'shaken'))
    print((hub.motion_sensor.get_gesture() == 'falling'))
    print(hub.motion_sensor.get_pitch_angle())
    print(hub.motion_sensor.get_roll_angle())
    print(hub.motion_sensor.get_yaw_angle())
    print(color_sensor_F.get_ambient_light())
    print(timer.now() * 1000)
    while True:
        if touch_sensor_B.is_pressed() == True:
            break
        if touch_sensor_B.get_force_percentage() > 30:
            break
        if color_sensor_F.get_color() == 'pink':
            break
        if color_sensor_F.get_reflected_light() < 50:
            break
        if color_sensor_F.get_red() / 10.24 < 30:
            break
        if color_sensor_F.get_green() / 10.24 < 30:
            break
        if color_sensor_F.get_blue() / 10.24 < 30:
            break
        if get_sample_ultrasonic(ultrasonic_sensor_U) < 30:
            break
        if hub.left_button.is_pressed() == True:
            break
        if (hub.motion_sensor.get_orientation() == 'front') == True:
            break
        if (hub.motion_sensor.get_orientation() == 'back') == True:
            break
        if (hub.motion_sensor.get_orientation() == 'up') == True:
            break
        if (hub.motion_sensor.get_orientation() == 'down') == True:
            break
        if (hub.motion_sensor.get_orientation() == 'rightside') == True:
            break
        if (hub.motion_sensor.get_orientation() == 'leftside') == True:
            break
        if (hub.motion_sensor.get_gesture() == 'tapped') == True:
            break
        if (hub.motion_sensor.get_gesture() == 'shaken') == True:
            break
        if (hub.motion_sensor.get_gesture() == 'falling') == True:
            break
        if hub.motion_sensor.get_pitch_angle() > 90:
            break
        if hub.motion_sensor.get_roll_angle() > 90:
            break
        if hub.motion_sensor.get_yaw_angle() > 90:
            break
        if color_sensor_F.get_ambient_light() < 50:
            break
        if timer.now() * 1000 > 500:
            break
    set_status_light('pink');

def main():
    try:
        run()
    except Exception as e:
        hub.light_matrix.show_image('SAD')

main()