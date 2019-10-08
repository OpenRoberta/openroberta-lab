'''
analyse the openroberta log and stat file as written by logback:

@author: rbudde
'''

import time
from util import *
from store import *
from entry import *

def groupLogEntries(fromTime, untilTime, grouper, fileName, matchKey, *matchStrings):
    groupH = Store()
    for line in getReader(fileName):
        fromLog(line).after(fromTime).before(untilTime).filterVal(matchKey, *matchStrings).groupCount(grouper, groupH)
    showGroup(groupH,fmt='{},{}')
    
def groupStatActions(fromTime, untilTime, grouper, fileName, *actions):
    groupH = Store()
    for line in getReader(fileName):
        fromStat(line).after(fromTime).before(untilTime).mapKey('message').filterVal('action',*actions).groupCount(grouper, groupH)
    showGroup(groupH,fmt='{},{}')
    showBar(groupH,title=str(actions),file='D:/downloads/init.png')

def processInitData(fromTime, untilTime, fileName):
    sessionIdStore = Store()
    groupInits = Store(groupBy='d')
    groupCountryCode = Store()
    groupBrowser = Store()
    groupOperatingSystem = Store()
    groupDeviceType = Store()
    for line in getReader(fileName):
        fromStat(line).after(fromTime).before(untilTime).filterVal('action','Initialization').uniqueKey('sessionId', sessionIdStore)\
        .groupStore(groupInits).mapKey('args')\
        .keyStore('CountryCode', groupCountryCode).keyStore('Browser', groupBrowser).keyStore('OS', groupOperatingSystem).keyStore('DeviceType', groupDeviceType)
    showStore(groupInits,fmt='{:12s} {:5d}',title='initialization calls per day')
    showStore(groupCountryCode,fmt='{:12s} {:5d}',title='country codes')
    showStore(groupBrowser,fmt='{:40s} {:5d}',title='browser types')
    showStore(groupOperatingSystem,fmt='{:40s} {:5d}',title='operating systems')
    showStore(groupDeviceType,fmt='{:40s} {:5d}',title='device types')
    showBar(groupInits,title='Unique Sessions',file='D:/downloads/uniqueSessions.png', legend=(1.04,0))
    showPie(groupCountryCode,title='country codes',file='D:/downloads/countrycode.png')
    showPie(groupBrowser,title='browser types',file='D:/downloads/browserTypes.png')
    showPie(groupOperatingSystem,title='operating systems',file='D:/downloads/operatingSystems.png')
    showPie(groupDeviceType,title='device types',file='D:/downloads/deviceTypesPie.png')
    showBar(groupDeviceType,title='device types',file='D:/downloads/deviceTypesBar.png')
    
def processSessions(fromTime, untilTime, fileName):
    grouper='m'
    sessionIdStore = Store()
    groupInits = Store(groupBy=grouper)
    for line in getReader(fileName):
        fromStat(line).after(fromTime).before(untilTime).filterVal('action','Initialization').uniqueKey('sessionId', sessionIdStore)\
        .groupStore(groupInits)
    showStore(groupInits,fmt='{:12s} {:5d}',title='session inits grouped by ' + grouper)

def processRobotUsage(fromTime, untilTime, fileName):
    sessionIdRobotSet = Store(storeSet=True)
    for line in getReader(fileName):
        fromStat(line).after(fromTime).before(untilTime).filterVal('action','ServerStart','Initialization','ChangeRobot',negate=True,substring=False)\
        .keyValStore('sessionId','robotName', sessionIdRobotSet)
    robotSessionIdSet = Store()
    invertStore(sessionIdRobotSet,robotSessionIdSet)
    showStore(robotSessionIdSet,fmt='{:40s} {:5d}',title='robotName used w.o. init+change')
    
def sessionsActions(fromTime, untilTime, fileName, *sessionNumbers):
    groupActions = Store(storeList=True)
    for line in getReader(fileName):
        fromStat(line).after(fromTime).before(untilTime).filterVal('sessionId', *sessionNumbers)\
        .keyValStore('sessionId', 'action', groupActions)
    showStore(groupActions, fmt='{:15} {:6d} {}')
        
def oneSessionActions(fromTime, untilTime, fileName, *sessionNumbers):
    for line in getReader(fileName):
        fromStat(line).after(fromTime).before(untilTime).filterVal('sessionId', *sessionNumbers, substring=False)\
        .reset().assemble('robotName').mapKey('message').assemble('action').showAssembled()

def openSessionsSinceLastRestart(fileName):
    store = Store()
    for line in getReader(fileName):
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
    