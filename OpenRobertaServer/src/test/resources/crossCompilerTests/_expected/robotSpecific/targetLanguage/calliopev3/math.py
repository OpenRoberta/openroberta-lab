import calliopemini
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()


___numberVar = 0
___numberList = [0, 0]
def ____math():
    global timer1, ___numberVar, ___numberList
    calliopemini.display.scroll(str(0))
    calliopemini.display.scroll(str(___numberVar + ___numberVar))
    calliopemini.display.scroll(str(___numberVar - ___numberVar))
    calliopemini.display.scroll(str(___numberVar * ___numberVar))
    calliopemini.display.scroll(str(___numberVar / float(___numberVar)))
    calliopemini.display.scroll(str(math.pow(___numberVar, ___numberVar)))
    calliopemini.display.scroll(str(math.sqrt(___numberVar)))
    calliopemini.display.scroll(str(math.fabs(___numberVar)))
    calliopemini.display.scroll(str(- (___numberVar)))
    calliopemini.display.scroll(str(math.log(___numberVar)))
    calliopemini.display.scroll(str(math.log10(___numberVar)))
    calliopemini.display.scroll(str(math.exp(___numberVar)))
    calliopemini.display.scroll(str(math.pow(10, ___numberVar)))
    calliopemini.display.scroll(str(math.sin(___numberVar)))
    calliopemini.display.scroll(str(math.cos(___numberVar)))
    calliopemini.display.scroll(str(math.tan(___numberVar)))
    calliopemini.display.scroll(str(math.asin(___numberVar)))
    calliopemini.display.scroll(str(math.acos(___numberVar)))
    calliopemini.display.scroll(str(math.atan(___numberVar)))
    calliopemini.display.scroll(str(math.pi))
    calliopemini.display.scroll(str(math.e))
    calliopemini.display.scroll(str((1 + 5 ** 0.5) / 2))
    calliopemini.display.scroll(str(math.sqrt(2)))
    calliopemini.display.scroll(str(math.sqrt(0.5)))
    calliopemini.display.scroll(str((___numberVar % 2) == 0))
    calliopemini.display.scroll(str((___numberVar % 2) == 1))
    calliopemini.display.scroll(str(___numberVar > 0))
    calliopemini.display.scroll(str(___numberVar < 0))
    calliopemini.display.scroll(str((___numberVar % ___numberVar) == 0))
    calliopemini.display.scroll(str(round(___numberVar)))
    calliopemini.display.scroll(str(math.ceil(___numberVar)))
    calliopemini.display.scroll(str(math.floor(___numberVar)))
    calliopemini.display.scroll(str(( ( ___numberVar ) % ( ___numberVar ) )))
    calliopemini.display.scroll(str(min(max(___numberVar, ___numberVar), ___numberVar)))
    calliopemini.display.scroll(str(random.randint(___numberVar, ___numberVar)))
    calliopemini.display.scroll(str(random.random()))
    calliopemini.display.scroll(str(_isPrime(___numberVar)))
    calliopemini.display.scroll(str((___numberVar % 1) == 0))
    ___numberVar += ___numberVar
    calliopemini.display.scroll(str(sum(___numberList)))
    calliopemini.display.scroll(str(min(___numberList)))
    calliopemini.display.scroll(str(max(___numberList)))
    calliopemini.display.scroll(str(float(sum(___numberList))/len(___numberList)))
    calliopemini.display.scroll(str(_median(___numberList)))
    calliopemini.display.scroll(str(_standard_deviation(___numberList)))


def run():
    global timer1, ___numberVar, ___numberList
    ____math()

def main():
    try:
        run()
    except Exception as e:
        raise

def _median(l):
    l = sorted(l)
    l_len = len(l)
    if l_len < 1:
        return None
    if l_len % 2 == 0:
        return (l[int((l_len - 1) / 2)] + l[int((l_len + 1) / 2)] ) / 2.0
    else:
        return l[int((l_len - 1) / 2)]

def _isPrime(number):
    if(number == 0 or number == 1):
        return False
    for i in range(2, int(math.floor(math.sqrt(number))) + 1):
        remainder = number % i
        if remainder == 0:
            return False
    return True

def _standard_deviation(l):
    mean = float(sum(l)) / len(l)
    sd = 0
    for i in l:
        sd += (i - mean)*(i - mean)
    return math.sqrt(sd / len(l))
if __name__ == "__main__":
    main()