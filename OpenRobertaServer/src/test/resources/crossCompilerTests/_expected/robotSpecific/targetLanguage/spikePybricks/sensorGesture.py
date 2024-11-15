from pybricks.hubs import PrimeHub
from pybricks.parameters import Side
from pybricks.tools import Matrix, wait, StopWatch, vector
import umath as math
import urandom as random

def display_text(text):
    text_list = list(text)
    for idx,c in enumerate(text_list):
        if ord(c) < 33 or ord(c) > 125:
            text_list[idx] = '?'
    hub.display.text("".join(text_list))

def get_acceleration():
    return math.sqrt((abs(hub.imu.acceleration()[0])**2) + (abs(hub.imu.acceleration()[1])**2) + (abs(hub.imu.acceleration()[2])**2))

def is_free_fall():
    threshold = 350
    stopWatch.time()
    start = stopWatch.time()
    while stopWatch.time() - start < 333:
        if(get_acceleration() < threshold): 
            return True
    return False

def is_shaken():
    count_tapped = 0
    tapped = True
    while tapped := is_tapped(1000):
        if tapped:
            count_tapped += 1
            wait(75)
        if count_tapped >= 10:
            return True
    return False

def lerp(a, b, t):
    x = a[0] * (1-t) + t * b[0]
    y = a[1] * (1-t) + t * b[1]
    z = a[2] * (1-t) + t * b[2]
    return vector(x,y,z)

def diff(a,b):
    return vector(a[0]-b[0],a[1]-b[1],a[2]-b[2])

def is_tapped(threshold = 5000):
    low_pass_value = hub.imu.acceleration()
    start = stopWatch.time()
    while stopWatch.time() - start < 333:
        acceleration = hub.imu.acceleration()
        low_pass_value = lerp(low_pass_value, acceleration, 1/2)
        delta_acceleration = diff(acceleration, low_pass_value)
        prod =  math.sqrt((abs(delta_acceleration[0])**2) + (abs(delta_acceleration[1])**2) + (abs(delta_acceleration[2])**2))
        if prod > threshold:
          return True
    wait(20)
    return False



stopWatch = StopWatch()
hub = PrimeHub()
hub.imu.reset_heading(0)


def run():
    display_text(str((hub.imu.up() == Side.TOP)))
    while True:
        if (hub.imu.up() == Side.TOP) == True:
            break
    display_text(str("front"))
    while True:
        if (hub.imu.up() == Side.BOTTOM) == True:
            break
    display_text(str("back"))
    while True:
        if (hub.imu.up() == Side.FRONT) == True:
            break
    display_text(str("upright"))
    while True:
        if (hub.imu.up() == Side.BACK) == True:
            break
    display_text(str("upside down"))
    while True:
        if (hub.imu.up() == Side.RIGHT) == True:
            break
    display_text(str("left"))
    while True:
        if (hub.imu.up() == Side.LEFT) == True:
            break
    display_text(str("right"))
    while True:
        if is_tapped() == True:
            break
    display_text(str("tapped"))
    while True:
        if is_shaken() == True:
            break
    display_text(str("shaking"))
    while True:
        if is_free_fall() == True:
            break
    display_text(str("free fall"))

def main():
    try:
        run()
    except Exception as e:
        while True:
            hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))

main()