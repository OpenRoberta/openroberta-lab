from pybricks.hubs import PrimeHub
from pybricks.tools import Matrix, wait
import umath as math
import urandom as random


hub = PrimeHub()


___item2 = [0, 0, 0]

def display_text(text):
    text_list = list(text)
    for idx,c in enumerate(text_list):
        if ord(c) < 33 or ord(c) > 125:
            text_list[idx] = '?'
    hub.display.text("".join(text_list))

def run():
    global ___item2
    while True:
        for ___k0 in range(int(0), int(10), int(1)):
            if ( True or True ) != False:
                if ( True and True ) == False:
                    display_text(str("B"))
                else:
                    display_text(str("A"))
        wait(500)
    for ___i in range(int(1), int(10), int(1)):
        while not True:
            wait(500)
        break
    for ___item in ___item2:
        wait(500)

def main():
    try:
        run()
    except Exception as e:
        while True:
            hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))

main()