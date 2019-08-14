'''
analyse the openroberta log file as written by logback:
  * -sessions-from-log-since-last-restart <path-to-log-file>
  * -sessions-from-log-since-date <date> <path-to-log-file>  --- date as 2019-08-14 11:06:42 or 2019-08-14 or 2019-08 or 2019-08-14 11:06

@author: rbudde
'''

import sys
import re
import json
from util import *

store = SessionStore()

# prototype of a openroberta STATISTICS log line:
# 2019-08-14 11:06:42,022 [qtp1123629720-1168] session-id=[] user-id=[] robot-name=[] INFO d.f.i.r.j.provider.OraDataProvider - session #1737 created
# 2019-08-14 11:07:35,613 [qtp1123629720-1141] session-id=[] user-id=[] robot-name=[] INFO d.f.iais.roberta.main.ServerStarter - jetty session destroyed  for /rest REST endpoint. Session number 1736

def matchLogLineDateText(line):
    matcher = re.search('^([^ ]*) ([^ ]*).* - (.*)$', line)
    if matcher is None:
        return None
    date = matcher.group(1)
    time = matcher.group(2)
    text = matcher.group(3)
    return (date + ' ' + time, text)

def processCreateDestroy(text, date):
    global store
    matcher = re.search('^session #(.*) created$', text)
    if matcher is not None:
        store.add(matcher.group(1),date)
        return
    matcher = re.search('^.*destroyed  for /rest REST endpoint. Session number (.*)$', text)
    if matcher is not None:
        store.remove(matcher.group(1))
        return

def runSessionsFromLastRestart(fileName):
    global store
    store = SessionStore()
    for line in getReader(fileName):
        cd = matchLogLineDateText(line)
        if cd is None:
            continue
        date = cd[0]
        text = cd[1]
        if "server started at " in text:
            store = SessionStore()
        processCreateDestroy(text, date)
    store.show("SINCE LAST RESTART")

def runSessionsSinceDate(startDate, fileName):
    global store
    store = SessionStore()
    for line in getReader(fileName):
        cd = matchLogLineDateText(line)
        if cd is None:
            continue
        date = cd[0]
        text = cd[1]
        if date < startDate:
            continue
        if "server started at " in text:
            store = SessionStore()
        processCreateDestroy(text, date)
    store.show("SINCE " + startDate)

if __name__ == "__main__":
    matchArg = sys.argv[1]
    if matchArg == '-sessions-from-log-since-last-restart':
        fileName = sys.argv[2]
        runSessionsFromLastRestart(fileName)
    elif matchArg == '-sessions-from-log-since-date':
        date = sys.argv[2]
        fileName = sys.argv[3]
        runSessionsSinceDate(date, fileName)
    else:
        print('no valid command')