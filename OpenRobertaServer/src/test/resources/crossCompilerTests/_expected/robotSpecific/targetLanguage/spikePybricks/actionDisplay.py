from pybricks.hubs import PrimeHub
from pybricks.tools import Matrix, wait
import umath as math
import urandom as random

def display_text(text):
    text_list = list(text)
    for idx,c in enumerate(text_list):
        if ord(c) < 33 or ord(c) > 125:
            text_list[idx] = '?'
    hub.display.text("".join(text_list))

def show_animation(image_list):
    hub.display.animate(image_list, 500)
    wait(500 * len(image_list) - 10)
    hub.display.icon(image_list[len(image_list)-1])



hub = PrimeHub()


___item = [Matrix([[0, 100, 0, 100, 0], [100, 100, 100, 100, 100], [100, 100, 100, 100, 100], [0, 100, 100, 100, 0], [0, 0, 100, 0, 0]]), Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 100, 100, 100, 0], [0, 0, 100, 0, 0], [0, 0, 0, 0, 0]]), Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [100, 0, 0, 0, 100], [0, 100, 100, 100, 0]]), Matrix([[0, 0, 0, 0, 0], [0, 0, 0, 0, 0], [0, 0, 0, 0, 0], [100, 0, 0, 0, 100], [0, 100, 100, 100, 0]]), Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [100, 0, 100, 0, 100]]), Matrix([[100, 0, 0, 0, 100], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [100, 100, 100, 100, 100], [100, 0, 100, 0, 100]]), Matrix([[0, 0, 0, 0, 0], [100, 100, 0, 100, 100], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [0, 0, 0, 0, 0]]), Matrix([[0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 0, 100, 0, 0], [0, 100, 0, 100, 0], [0, 0, 100, 0, 0]]), Matrix([[100, 0, 0, 0, 100], [0, 0, 0, 0, 0], [100, 100, 100, 100, 100], [0, 0, 100, 0, 100], [0, 0, 100, 100, 100]]), Matrix([[100, 100, 100, 100, 100], [100, 100, 0, 100, 100], [0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 100, 100, 100, 0]]), Matrix([[0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 0, 0, 100, 0], [0, 0, 100, 0, 0], [0, 100, 0, 0, 0]])]

def run():
    global ___item
    display_text(str("ABC"))
    display_text(str("DEF"))
    hub.display.icon(Matrix([[100, 0, 0, 0, 100], [0, 0, 0, 0, 0], [0, 0, 100, 0, 0], [0, 0, 0, 0, 0], [100, 0, 0, 0, 100]]))
    show_animation(list(___item))
    hub.display.off()
    hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 0, 0, 0, 100], [0, 0, 0, 100, 0], [100, 0, 100, 0, 0], [0, 100, 0, 0, 0]]))
    wait(2000)

def main():
    try:
        run()
    except Exception as e:
        while True:
            hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))

main()