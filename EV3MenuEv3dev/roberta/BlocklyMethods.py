import math

class BlocklyMethods:
    GOLDEN_RATIO = (1 + math.sqrt(5)) / 2

    @staticmethod
    def isEven(number):
        return (number % 2) == 0

    @staticmethod
    def isOdd(number):
        return (number % 2) == 1

    @staticmethod
    def isPrime(number):
        for i in xrange(2, math.sqrt(number)):
            remainder = number % i
            if remainder == 0:
                return False
        return True

    @staticmethod
    def isWhole(number):
        return number % 1 == 0

    @staticmethod
    def isPositive(number):
        return number > 0

    @staticmethod
    def isNegative(number):
        return number < 0

    @staticmethod
    def isDivisibleBy(number, divisor):
        return number % divisor == 0

    @staticmethod
    def remainderOf(divident, divisor):
        return divident % divisor;

    @staticmethod
    def clamp(x, min_val, max_val):
        return min(max(x, min_val), max_val);

    @staticmethod
    def randInt(min_val, max_val):
        if min_val < max_val:
            return random.randint(min_val, max_val)
        else:
            return random.randint(max_val, min_val)

    @staticmethod
    def randDouble():
        return random.random()
