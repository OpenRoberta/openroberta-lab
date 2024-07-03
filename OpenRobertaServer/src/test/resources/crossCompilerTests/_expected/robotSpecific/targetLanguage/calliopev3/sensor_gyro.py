import calliopemini
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()


def run():
    global timer1
    print("Gyro test. Press A to step through")
    while True:
        print(get_rotation("pitch"))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    while True:
        print(get_rotation("roll"))
        calliopemini.sleep(100)
        if calliopemini.button_a.is_pressed():
            break
    print("DONE")

def main():
    try:
        run()
    except Exception as e:
        raise

def get_rotation(dim):
    x = -calliopemini.accelerometer.get_y()
    y = calliopemini.accelerometer.get_x()
    z = -calliopemini.accelerometer.get_z()
    roll = math.atan2(y, z)
    if (dim == "roll"):
        return math.floor((-360*roll)/(2*math.pi))
    elif (dim == "pitch"):
        factor = y * math.sin(roll) + z * math.cos(roll)
        if factor != 0:
            pitch = math.atan(-x / (factor))            
            return math.floor((360*pitch)/(2*math.pi))
if __name__ == "__main__":
    main()