'''
analyse the openroberta log and stat file as written by logback:

@author: rbudde
'''

import time
from util import *
from store import *
from entry import *

def groupEntriesByTime(fromTime, untilTime, grouper, baseDir, fileName, matchKey, *matchStrings, type='stat'):
    timeGroup = Store(groupBy=grouper)
    if type == 'stat':
        fromFct = fromStat
    else:
        fromFct = fromLog
    for line in getReader(baseDir, fileName):
        fromFct(line).after(fromTime).before(untilTime).filterVal(matchKey, *matchStrings).groupStore(timeGroup)
    timeGroup.show(fmt='{:26s} {:8.0f}')
    timeGroup.showBar(title='entries per ' + grouper,file='D:/tmp/entriesByTime.png',type='bar',legend=None,xAxisNbins=20)
    
def groupStatActionsByTime(fromTime, untilTime, grouper, baseDir, fileName, *actions):
    timeGroup = Store(groupBy=grouper)
    for line in getReader(baseDir, fileName):
        fromStat(line).after(fromTime).before(untilTime).mapKey('message').filterVal('action',*actions).groupStore(timeGroup)
    timeGroup.show(fmt='{:26s} {:8.0f}')
    timeGroup.showBar(title=str(actions),file='D:/tmp/actions.png',type='bar')
    
def groupStatSessionInitsByTime(fromTime, untilTime, baseDir, fileName):
    grouper='m'
    sessionIdStore = Store()
    groupInits = Store(groupBy=grouper)
    for line in getReader(baseDir, fileName):
        fromStat(line).after(fromTime).before(untilTime).filterVal('action','Initialization').uniqueKey('sessionId', sessionIdStore)\
        .groupStore(groupInits)
    groupInits.show(fmt='{:12s} {:5d}',title='session inits grouped by ' + grouper)

def groupStatSessionsAfterLastServerRestartByTime(fromTime, untilTime, baseDir, fileName):
    grouper='m'
    sessionIdStore = None
    groupInits = None
    def resetStores():
        nonlocal sessionIdStore, groupInits
        sessionIdStore = Store()
        groupInits = Store(groupBy=grouper)
    resetStores()
    for line in getReader(baseDir, fileName):
        fromStat(line).after(fromTime).before(untilTime).filterVal('action','ServerStart',substring=False).exec(resetStores).reset()\
        .filterVal('action','Initialization').uniqueKey('sessionId', sessionIdStore).groupStore(groupInits)
    print('number of sessions found: ' + str(groupInits.totalKeyCounter))
    groupInits.show(fmt='{:12s} {:5d}',title='session inits after the last server restart grouped by ' + grouper)

def computeOpenStatSessionsAfterLastServerRestart(fromTime, untilTime, baseDir, fileName):
    actionStore = None
    def resetStore():
        nonlocal actionStore
        actionStore = Store(storeList=True)
    resetStore()
    for line in getReader(baseDir, fileName):
        fromStat(line).after(fromTime).before(untilTime).filterVal('action','ServerStart',substring=False).exec(resetStore).reset()\
        .after(fromTime).before(untilTime).keyValStore('sessionId','action',actionStore)\
        .filterVal('action','SessionDestroy').closeKey('sessionId', actionStore)
    print('number of sessions: ' + str(actionStore.totalKeyCounter))
    print('number of open sessions: ' + str(actionStore.openKeyCounter))
    for key, item in actionStore.data.items():
        if item.state == 'open':
            print('{:12s} {}'.format(key,item.storeList))

def computeStatRobotUsage(fromTime, untilTime, baseDir, fileName):
    sessionIdRobotSet = Store(storeSet=True)
    for line in getReader(baseDir, fileName):
        fromStat(line).after(fromTime).before(untilTime).filterVal('action','ServerStart','Initialization','ChangeRobot',negate=True,substring=False)\
        .keyValStore('sessionId','robotName', sessionIdRobotSet)
    robotSessionIdSet = Store()
    invertStore(sessionIdRobotSet,robotSessionIdSet)
    robotSessionIdSet.show(fmt='{:40s} {:5d}',title='robotName used w.o. init+change')
    
def computeStatSessionsActionsForSomeSessionIds(fromTime, untilTime, baseDir, fileName, *sessionNumbers):
    groupActions = Store(storeList=True)
    for line in getReader(baseDir, fileName):
        fromStat(line).after(fromTime).before(untilTime).filterVal('sessionId', *sessionNumbers)\
        .keyValStore('sessionId', 'action', groupActions)
    showStore(groupActions, fmt='{:15} {:6d} {}')
        
def computeOpenLogSessionsSinceLastRestart(baseDir, fileName):
    store = Store()
    for line in getReader(baseDir, fileName):
        entry = fromLog(line).entry
        if entry is not None:
            event = entry['event']
            matcher = re.search('server started at ', event)
            if matcher is not None:
                store = Store()
                continue
            matcher = re.search('session #(.*) created', event)
            if matcher is not None:
                store.put(matcher.group(1), entry['time'])
                continue
            matcher = re.search('destroyed  for /rest REST endpoint. Session number (.*)', event)
            if matcher is not None:
                store.close(matcher.group(1))
                continue
    store.show("SINCE LAST RESTART")
    