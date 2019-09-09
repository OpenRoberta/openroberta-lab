'''
analyse the openroberta statistics file:
  * -sessions-create-destroy-from-statistics-since-last-restart <path-to-log-file>
  * -sessions-load-from-statistics-since-last-restart <path-to-log-file>
  * -sessions-load-from-statistics-since-date <date> <path-to-log-file>  --- date as 2019-08-14 11:06:42 or 2019-08-14 or 2019-08 or 2019-08-14 11:06
  * -action-classification-from-statistics-since-last-restart <classGroup> <path-to-log-file>  --- see data.py for the classGroup's

@author: rbudde
'''

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
