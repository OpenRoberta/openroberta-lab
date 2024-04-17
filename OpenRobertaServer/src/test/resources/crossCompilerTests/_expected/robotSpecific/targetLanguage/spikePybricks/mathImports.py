from pybricks.hubs import PrimeHub
from pybricks.tools import Matrix
import umath as math
import urandom as random


hub = PrimeHub()


___item = 0

def display_text(text):
    text_list = list(text)
    for idx,c in enumerate(text_list):
        if ord(c) < 33 or ord(c) > 125:
            text_list[idx] = '?'
    hub.display.text("".join(text_list))

def run():
    global ___item
    # String 42
    display_text(str(str(42)))
    # thjs is a J
    display_text(str(chr((int)(74))))
    ___item = math.sqrt(math.pi) + math.sin(math.e)
    ___item = - (10 **math.e**math.log(math.log(abs((1 + 5 ** 0.5) / 2 * (1 + 5 ** 0.5) / 2 + math.cos(math.sqrt(2)))))/math.log(10))
    ___item += 1
    display_text(str(min(max(( ( math.ceil(___item) ) % ( math.floor(___item) ) ), 1), 2)))
    display_text(str(random.randint(1, 100)))
    display_text(str(random.random()))
    display_text(str((random.random() % 2) == 0))
    display_text(str((random.random() % 2) == 1))
    display_text(str((random.random() % 1) == 0))
    display_text(str(random.random() > 0))
    display_text(str(random.random() < 0))
    display_text(str((random.random() % 1) == 0))

def main():
    try:
        run()
    except Exception as e:
        while True:
            hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))

main()