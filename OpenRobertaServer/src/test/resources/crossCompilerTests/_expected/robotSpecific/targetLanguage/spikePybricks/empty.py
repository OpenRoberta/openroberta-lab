from pybricks.hubs import PrimeHub
from pybricks.tools import Matrix
import umath as math
import urandom as random


hub = PrimeHub()


def run():
    pass

def main():
    try:
        run()
    except Exception as e:
        while True:
            hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))

main()