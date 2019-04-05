'''
Created on 03.04.2019

@author: rbudde
'''

import sys
import re

LIMIT = 10

def initSecC():
    secC = {}
    ta = []
    for i1 in range(24):
        ta.append(format(i1, "02d"))
        for i2 in range(60):
            ta.append(format(i2, "02d"))
            for i3 in range(60):
                ta.append(format(i3, "02d"))
                secC[tuple(ta)] = 0
                ta.pop(-1)
            ta.pop(-1)
        ta.pop(-1)
    return secC
secC = initSecC()
secCWithPing = initSecC()

def upd_dict(dictC, key):
    old = dictC.get(key)
    if old is None:
        dictC[key] = 1
    else:
        dictC[key] = 1 + old

startKey = None
counter = 0

def printGap(lastKey):
    if counter == 1:
        note = ''
    else:
        note = ' of ' + str(counter) + ' sec until ' + ':'.join(lastKey)
    if counter >= LIMIT:
        note = note + ' ***** this seems to be a severe gap >= ' + str(LIMIT) + ' *****'
    print('  gap at ' + ':'.join(startKey) + note)

def checkGap(dictC):
    global startKey, counter
    print('checking for gaps')
    for k in sorted(dictC.keys()):
        v = dictC[k]
        if startKey is None:
            startKey = k
            counter = 0
            continue
        elif v == 0:
            counter += 1
        elif counter == 0:
            startKey = k
            continue
        else:
            printGap(k)
            startKey = k
            counter = 0
    if counter > 0:
        printGap(('24','00','00'))
    print('')

def printC(name, dictC):
    print('log result for ' + name)
    for k,v in dictC.items():
        print('  ' + ':'.join(k) + ' ' + format(v, "8d"))
    print('')

def main(fileName):
    ping_counter = 0
    hourC = {}
    hourCWithPing = {}
    minC = {}
    lineReader = open(fileName, "r")
    for line in lineReader:
        matcher = re.search('^[^:]*:(..):(..):(..)[^ \t]*[^"]*"(.*)$', line)
        hh = matcher.group(1)
        mm = matcher.group(2)
        ss = matcher.group(3)
        rest = matcher.group(4)
        upd_dict(secCWithPing, (hh,mm,ss))
        upd_dict(hourCWithPing, (hh,))
        if (rest.find('ping') >= 0):
            ping_counter += 1
        else:
            upd_dict(hourC, (hh,))
            upd_dict(minC, (hh,mm))
            upd_dict(secC, (hh,mm,ss))
    print('pings: ' + str(ping_counter) + '\n')
    printC('hours', hourC)
    # printC('minutes', minC)
    checkGap(secCWithPing)

if __name__ == "__main__":
    fileName = sys.argv[1]
    print('start of apache log checker for file ' + fileName)
    main(fileName)