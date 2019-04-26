'''
Takes 2 command line arguments:
* logging file type to analyse:
  * -apache2 access log create by apache2 (e.g. access-443.log)
  * -startInit -statLogin statistic logfile created by the openroberta server (e.g. ora-server.log)
* the path to the log file to analyse

The program reads the log file (assuming it is exactly for one day) and creates usage data for every second, minute and hour.
Then it prints a statistic and checks for gaps in the log file (to detect time interval, in which the server got no requests or was down)

@author: rbudde
'''

import sys
import re

LIMIT = 10

matchArg = None
actualMatcher = None
actualEncoding = None
actualLimit = None

# prototype of a apache log line:
# 123.4.567.89 - - [11/Apr/2019:07:10:02 +0200] "GET /js/main.js HTTP/1.1" 200 2819
def matchApacheLog(line):
    matcher = re.search('^[^:]*:(..):(..):(..)[^ \t]*[^"]*"(.*)$', line)
    if matcher is None:
        return None
    hh = matcher.group(1)
    mm = matcher.group(2)
    ss = matcher.group(3)
    rest = matcher.group(4)
    return (hh,mm,ss,rest,line)

# prototype of a openroberta STATISTICS log line:
# STATISTICS {"time":"2019-04-01 00:39:59,446", "sessionId":"1", "robotName":"r", "logLevel":"INFO", "message":{"args":[...],"action":"Initialization"}}

def matchStatInit(line):
    matcher = re.search('^[^:]*[^ ]* (..):(..):(..).*"action":"Initialization"', line)
    if matcher is None:
        return None
    hh = matcher.group(1)
    mm = matcher.group(2)
    ss = matcher.group(3)
    rest = line
    return (hh,mm,ss,rest,line)

def matchStatLogin(line):
    matcher = re.search('^[^:]*[^ ]* (..):(..):(..).*"action":"UserLogin"', line)
    if matcher is None:
        return None
    hh = matcher.group(1)
    mm = matcher.group(2)
    ss = matcher.group(3)
    rest = line
    return (hh,mm,ss,rest,line)

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
    if counter >= actualLimit:
        note = note + ' ***** this seems to be a severe gap >= ' + str(actualLimit) + ' *****'
    print('  gap at ' + ':'.join(startKey) + note)

def checkGap(dictC):
    global startKey, counter
    print('checking for gaps' + ' using scanner for ' + matchArg)
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
    print('log result for ' + name + ' using scanner for ' + matchArg)
    for k,v in dictC.items():
        print('  ' + ':'.join(k) + ' ' + format(v, "8d"))
    print('')

def run(fileName):
    ping_counter = 0
    hourC = {}
    hourCWithPing = {}
    minC = {}
    lineReader = open(fileName, "r", encoding=actualEncoding)
    for line in lineReader:
        result = actualMatcher(line)
        if result is None:
            continue
        (hh,mm,ss,rest,_) = result
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
    matchArg = sys.argv[1]
    if matchArg == '-apache2':
        actualMatcher = matchApacheLog
        actualEncoding = 'latin-1'
        actualLimit = 10
    elif matchArg == '-statInit':
        actualMatcher = matchStatInit
        actualEncoding = 'latin-1'
        actualLimit = 10
    elif matchArg == '-statLogin':
        actualMatcher = matchStatLogin
        actualEncoding = 'latin-1'
        actualLimit = 30
    else:
        print('no valid matcher found. -apache2 is the default')
        matchArg = '-apache2'
        actualMatcher = matchApacheLog
        actualEncoding = 'utf-8'
        actualLimit = 10
    fileName = sys.argv[2]
    print('start of scanner ' + matchArg + ' for file ' + fileName + ' with encoding ' + actualEncoding)
    run(fileName)