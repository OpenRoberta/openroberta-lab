#!/usr/bin/env python3
import math, random, time, requests, threading, sys, io
sys.stdout = io.StringIO()
sys.stderr = io.StringIO()
ROBOTINOIP = "127.0.0.1:80"
PARAMS = {'sid':'robertaProgram'}
MAXSPEED = 0.5
MAXROTATION = 0.57

_timer1 = None

___Element = None
___Element2 = None
___Element3 = None
___Element4 = None
___Element5 = None


def run(RV):
    global _timer1, ___Element, ___Element2, ___Element3, ___Element4, ___Element5
    time.sleep(1)
    _timer1 = time.time()
    ___Element = ((time.time() - _timer1)/1000)
    ___Element2 = ((time.time() - _timer1)/1000)
    ___Element3 = ((time.time() - _timer1)/1000)
    ___Element4 = ((time.time() - _timer1)/1000)
    ___Element5 = ((time.time() - _timer1)/1000)

    _timer1 = time.time()
    _timer2 = time.time()
    _timer3 = time.time()
    _timer4 = time.time()
    _timer5 = time.time()

def step(RV):
    pass

def main(RV):
    try:
        run(RV)
    except Exception as e:
        print(e)
        raise

def start(RV):
    motorDaemon2 = threading.Thread(target=main, daemon=True, args=(RV,), name='mainProgram')
    motorDaemon2.start()

def stop(RV):
    pass

def cleanup(RV):
    pass

