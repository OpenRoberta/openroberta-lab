from pybricks.hubs import PrimeHub
from pybricks.pupdevices import Motor
from pybricks.parameters import Port, Direction
from pybricks.tools import Matrix, wait
import umath as math
import urandom as random
from pybricks.robotics import DriveBase

def circle_circumference(diameter):
    return math.pi * (diameter)

def get_deg_sec_from_percent(percent):
    if percent < -100 :
        percent = -100
    if percent > 100 :
        percent = 100
    return int(810.0 * (percent/100.0))

def deg_sec_to_mm_sec(deg_sec):
    return (deg_sec/360) * circle_circumference(WHEEL_DIAMETER)

def diff_drive(left_speed, right_speed, regulated):
    if(regulated):
        left_motor.run(get_deg_sec_from_percent(left_speed))
        right_motor.run(get_deg_sec_from_percent(right_speed))
    else:
        left_motor.dc(left_speed)
        right_motor.dc(right_speed)

def drive_straight(speed, dist):
    if abs(speed) < 1.5:
        speed = (abs(speed)/speed) * 1.5
    drive_base.settings(straight_speed = deg_sec_to_mm_sec(get_deg_sec_from_percent(speed)))
    drive_base.straight(abs(speed)/speed * dist * 10 )

def distance_to_angle(distance):
    return (distance/circle_circumference(WHEEL_DIAMETER)) * 360

def catch_edge_cases(dist, left_speed, right_speed):
    near_zero = 0.5
    #in case velocities cancel dont move, because no distance will be driven
    if(abs(dist) < near_zero or abs(left_speed+right_speed) < near_zero):
        return True
    if((abs(left_speed) < 1.5 and abs(right_speed) < 1.5)):
        left_speed = (abs(left_speed)/left_speed) * 1.5
        right_speed = (abs(right_speed)/right_speed) * 1.5
    #just go straight no need for calculations
    if abs(left_speed - right_speed) <  near_zero:
        drive_base.settings(straight_speed= deg_sec_to_mm_sec(get_deg_sec_from_percent(left_speed)))
        drive_base.straight(abs(left_speed)/left_speed * dist * 10)
        return True
    return False

def wait_for_completion():
    while not (left_motor.done() and right_motor.done()):
          pass

def tank_drive_dist(dist, left_speed, right_speed):
    if catch_edge_cases(dist, left_speed, right_speed):
        return
    sign = abs(dist)/dist
    #to mm always calc with positive dist
    dist = (abs(dist) * 10)
    #we have to keep deg_sec to set motor speed later
    deg_sec_left = get_deg_sec_from_percent(left_speed)
    deg_sec_right = get_deg_sec_from_percent(right_speed)
    #we need mm/sec to calculate driven radius
    speed_left_mm_sec =  deg_sec_to_mm_sec(deg_sec_left)
    speed_right_mm_sec = deg_sec_to_mm_sec(deg_sec_right)

    #we need this distinction because inner/outer circle change depending on which motor is faster
    if (abs(deg_sec_right) > abs(deg_sec_left)):
        drives_radius = (TRACKWIDTH/2.0) * ((speed_left_mm_sec+speed_right_mm_sec)/(speed_right_mm_sec-speed_left_mm_sec))
        left_circle = circle_circumference(drives_radius - (TRACKWIDTH/2))
        right_circle = circle_circumference(drives_radius + (TRACKWIDTH/2))
    else:
        drives_radius = (TRACKWIDTH/2.0) * ((speed_left_mm_sec+speed_right_mm_sec)/(speed_left_mm_sec-speed_right_mm_sec))
        left_circle = circle_circumference(drives_radius + (TRACKWIDTH/2))
        right_circle = circle_circumference(drives_radius - (TRACKWIDTH/2))

    percent_to_drive = (dist / circle_circumference(drives_radius))
    angle_left = distance_to_angle(percent_to_drive * left_circle)
    angle_right = distance_to_angle(percent_to_drive * right_circle)

    if not deg_sec_left :
        deg_sec_left = 1
    if not deg_sec_right :
        deg_sec_right = 1

    left_motor.run_angle(deg_sec_left, sign * abs(angle_left) , wait=False)
    right_motor.run_angle(deg_sec_right, sign * abs(angle_right) , wait=False)

    wait_for_completion()

def turn_for(power, degrees, direction):
    if power == 0 or degrees == 0:
        return
    sign = (abs(power)/power)
    tr = (get_deg_sec_from_percent( power )  * (WHEEL_DIAMETER * math.pi)) / (TRACKWIDTH * math.pi)
    if abs(tr) < 5 :
        tr = 5
    drive_base.settings(turn_rate = tr)
    drive_base.turn( sign * degrees * direction )

left_motor = Motor(Port.A, Direction.COUNTERCLOCKWISE)
right_motor = Motor(Port.B)
TRACKWIDTH = 11.5 * 10
WHEEL_DIAMETER = 56
drive_base = DriveBase(left_motor, right_motor, wheel_diameter=WHEEL_DIAMETER, axle_track=TRACKWIDTH)
drive_base.settings(straight_acceleration=810 * 10, turn_acceleration=810 * 10)
hub = PrimeHub()

def run():
    drive_straight(30,10)
    drive_straight(30,10* -1)
    drive_straight(-30,-10* -1)
    drive_base.drive(deg_sec_to_mm_sec(get_deg_sec_from_percent(30)) , 0)
    wait(500)
    drive_base.drive(deg_sec_to_mm_sec(get_deg_sec_from_percent(-30)) , 0)
    wait(500)
    drive_base.stop()
    wait(500)
    diff_drive(30, 30, False)
    wait(500)
    diff_drive(-30, -30, False)
    wait(500)
    drive_base.drive(0, 0)
    wait(500)
    turn_for(30,20,1)
    turn_for(30,20,-1)
    turn_for(-30,-20,-1)
    drive_base.drive(0, get_deg_sec_from_percent(30) * (WHEEL_DIAMETER * math.pi) / (TRACKWIDTH * math.pi))
    wait(500)
    drive_base.drive(0, get_deg_sec_from_percent(-30) * (WHEEL_DIAMETER * math.pi) / (TRACKWIDTH * math.pi))
    wait(500)
    diff_drive(-(30), 30, False)
    wait(500)
    tank_drive_dist(20, 10, 30)
    tank_drive_dist(-(20), 10, 30)
    tank_drive_dist(-1, -29, 30)
    diff_drive((10), (30), True)
    wait(500)
    diff_drive((-30), (-30), True)
    wait(500)
    diff_drive(-((10)), -((30)), False)

def main():
    try:
        run()
    except Exception as e:
        while True:
            hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))

main()