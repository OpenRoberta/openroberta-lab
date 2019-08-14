'''
analyse the openroberta statistics file:
  * -sessions-create-destroy-from-statistics-since-last-restart <path-to-log-file>
  * -sessions-load-from-statistics-since-last-restart <path-to-log-file>
  * -sessions-load-from-statistics-since-date <date> <path-to-log-file>  --- date as 2019-08-14 11:06:42 or 2019-08-14 or 2019-08 or 2019-08-14 11:06
  * -action-classification-from-statistics-since-last-restart <classGroup> <path-to-log-file>  --- see data.py for the classGroup's

@author: rbudde
'''

import sys
import re
import json

from util import *
from data import *

# prototype of a openroberta STATISTICS log line:
# STATISTICS {"time":"2019-04-01 00:39:59,446", "sessionId":"1", "robotName":"r", "logLevel":"INFO", "message":{"args":[...],"action":"Initialization"}}
# STATISTICS {"time":"2019-08-14 14:14:05,725", "sessionId":"2195", "robotName":"ev3lejosv1", "logLevel":"INFO", "message":{"action":"Initialization","args":[{"Browser":"FIREFOX\/60.0","OS":"Windows 10","CountryCode":"DE","DeviceType":"Computer","ScreenSize":"[1536,824]"}]}}

def getJson(line):
    matcher = re.search('STATISTICS (.*)', line)
    if matcher is None:
        return None
    jsonAsString = matcher.group(1)
    if jsonAsString:
        return json.loads(jsonAsString)

def runCreateDestroySessionsSinceLastRestart(fileName):
    store = SessionStore()
    for line in getReader(fileName):
        jsonStat = getJson(line)
        message = jsonStat["message"]
        action = message["action"]
        if action == "Initialization":
            store.add(jsonStat["sessionId"],jsonStat["time"])
            print("created: " + jsonStat["time"])
        elif action == "SessionDestroy":
            store.remove(message["args"]["sessionId"])
        elif action == "ServerStart":
            store = SessionStore()
    store.show("SINCE LAST RESTART")
    
def runSessionLoadSinceLastRestart(classGroup, fileName):
    classifyList = classGroups.get(classGroup)
    store = SessionStore()
    storeForClassGroups = SessionStore()
    for line in getReader(fileName):
        jsonStat = getJson(line)
        message = jsonStat["message"]
        sessionId = jsonStat["sessionId"]
        action = message["action"]
        if action == "ServerStart":
            store = SessionStore()
            storeForClassGroups = SessionStore()
            continue
        classifier = classifyAction[action]
        if classifier in classifyList:
            storeForClassGroups.increment(sessionId)
        store.increment(sessionId)
    print("SINCE LAST RESTART CLASSGROUP " + classGroup)
    for sessionId in storeForClassGroups.sessions.keys():
        val = store.sessions.get(sessionId)
        valForClassGroups = storeForClassGroups.sessions.get(sessionId, 0)
        print(sessionId +  " : " + str(valForClassGroups) + " (of " + str(val) + ")") 
    print("sessions processed: " + str(store.sessionTotalCounter))
    print("sessions filtered:  " + str(storeForClassGroups.sessionTotalCounter))
    print("SINCE LAST RESTART CLASSGROUP " + classGroup)

def showAllActions(fileName):
    actions = set()
    for line in getReader(fileName):
        jsonStat = getJson(line)
        message = jsonStat["message"]
        action = message["action"]
        actions.add(action)
    for action in actions:
        print(action)

def runSessionLoadSinceDate(startDate, fileName):
    store = SessionStore()
    for line in getReader(fileName):
        jsonStat = getJson(line)
        date = jsonStat["time"]
        if date < startDate:
            continue
        message = jsonStat["message"]
        sessionId = jsonStat["sessionId"]
        action = message["action"]
        store.increment(sessionId)
    store.show("SINCE DATE: " + startDate)

def showAllActionsOfASession(sessionIdToLookFor, fileName):
    actions = list()
    for line in getReader(fileName):
        jsonStat = getJson(line)
        message = jsonStat["message"]
        action = message["action"]
        sessionId = jsonStat["sessionId"]
        robotName = jsonStat["robotName"]
        if action == "ServerStart":
            actions = list()
            continue
        if sessionId == sessionIdToLookFor:
            actions.append(action + " (" + robotName + ")")
    print("ALL ACTIONS OF SESSION: " + sessionIdToLookFor)
    for action in actions:
        print(action)
    print("ALL ACTIONS OF SESSION: " + sessionIdToLookFor)

def showActionsOfAllSessions(fileName):
    sessions = {}
    for line in getReader(fileName):
        jsonStat = getJson(line)
        message = jsonStat["message"]
        action = message["action"]
        sessionId = jsonStat["sessionId"]
        robotName = jsonStat["robotName"]
        if action == "ServerStart":
            sessions = {}
            continue
        actionList = sessions.get(sessionId, None)
        if actionList is None:
            actionList = list()
        
         == sessionIdToLookFor:
            actions.append(action + " (" + robotName + ")")
    print("ALL ACTIONS OF SESSION: " + sessionIdToLookFor)
    for action in actions:
        print(action)
    print("ALL ACTIONS OF SESSION: " + sessionIdToLookFor)

if __name__ == "__main__":
    matchArg = sys.argv[1]
    if matchArg == '-sessions-create-destroy-from-statistics-since-last-restart':
        fileName = sys.argv[2]
        runCreateDestroySessionsSinceLastRestart(fileName)
    elif matchArg == '-sessions-load-from-statistics-since-last-restart':
        fileName = sys.argv[2]
        runSessionLoadSinceLastRestart("all", fileName)
    elif matchArg == '-action-classification-from-statistics-since-last-restart':
        classGroup = sys.argv[2]
        fileName = sys.argv[3]
        runSessionLoadSinceLastRestart(classGroup, fileName)
    elif matchArg == '-sessions-load-from-statistics-since-date':
        date = sys.argv[2]
        fileName = sys.argv[3]
        runSessionLoadSinceDate(date, fileName)
    elif matchArg == '-actions-of-a-session-since-last-restart':
        sessionId = sys.argv[2]
        fileName = sys.argv[3]
        showAllActionsOfASession(sessionId, fileName)
    elif matchArg == '-actions-of-all-sessions-since-last-restart':
        fileName = sys.argv[2]
        showActionsOfAllSessions(fileName)
    elif matchArg == '-actions-of-a-session-since-last-restart-interactive':
        fileName = sys.argv[2]
        while True:
            print("sessionId or q for quit")
            sessionId = input()
            if sessionId == "q":
                break
            showAllActionsOfASession(sessionId, fileName)
    elif matchArg == '-all-actions':
        showAllActions(fileName)
    else:
        print('no valid command')