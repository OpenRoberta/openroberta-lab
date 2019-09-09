'''
analyse the openroberta log and stat file as written by logback:

@author: rbudde
'''

import time
from util import *
from intervalstore import *
from entry import *

def groupLogEntries(fromTime, untilTime, grouper, fileName, matchKey, *matchStrings):
    groupH = {}
    for line in getReader(fileName):
        fromLog(line).after(fromTime).before(untilTime).filterVal(matchKey, *matchStrings).groupCount(grouper, groupH)
    showGroup(groupH,fmt='{},{}')
    
def groupStatActions(fromTime, untilTime, grouper, fileName, *actions):
    groupH = {}
    for line in getReader(fileName):
        fromStat(line).after(fromTime).before(untilTime).mapKey('message').filterVal('action',*actions).groupCount(grouper, groupH)
    showGroup(groupH,fmt='{},{}')
    showBar(groupH,title=str(actions),file='D:/downloads/init.png')

def groupInitData(fromTime, untilTime, fileName):
    groupInits = {}
    groupCountryCode = {}
    groupBrowser = {}
    groupOperatingSystem = {}
    groupDeviceType = {}
    for line in getReader(fileName):
        fromStat(line).after(fromTime).before(untilTime).mapKey('message').filterVal('action','Initialization').reset()\
        .uniqueKey('sessionId').mapKey('message').groupCount('d',groupInits)\
        .mapKey('args').mapList(0,lazy=True)\
        .keyCount('CountryCode', groupCountryCode).keyCount('Browser', groupBrowser, pattern='(.*)\/').keyCount('OS', groupOperatingSystem).keyCount('DeviceType', groupDeviceType)
    showGroup(groupInits,fmt='{:12s} {:5d}',title='initialization calls per day')
    showGroup(groupCountryCode,fmt='{:12s} {:5d}',title='country codes')
    showGroup(groupBrowser,fmt='{:40s} {:5d}',title='browser types')
    showGroup(groupOperatingSystem,fmt='{:40s} {:5d}',title='operating systems')
    showGroup(groupDeviceType,fmt='{:40s} {:5d}',title='device types')
    showBar(groupInits,title='Unique Sessions',file='D:/downloads/uniqueSessions.png')
    showPie(groupCountryCode,title='country codes',file='D:/downloads/countrycode.png')
    showPie(groupBrowser,title='browser types',file='D:/downloads/browserTypes.png')
    showPie(groupOperatingSystem,title='operating systems',file='D:/downloads/operatingSystems.png')
    showPie(groupDeviceType,title='device types',file='D:/downloads/deviceTypes.png')
    
def oneSession(fromTime, untilTime, fileName, *sessionNumbers):
    for line in getReader(fileName):
        fromStat(line).after(fromTime).before(untilTime).filterVal('sessionId', *sessionNumbers, substring=False)\
        .reset().mapKey('message').showEntry()
        
def oneSessionActions(fromTime, untilTime, fileName, *sessionNumbers):
    for line in getReader(fileName):
        fromStat(line).after(fromTime).before(untilTime).filterVal('sessionId', *sessionNumbers, substring=False)\
        .reset().assemble('robotName').mapKey('message').assemble('action').showAssembled()

def openSessionsSinceLastRestart(fileName):
    store = IntervalStore()
    for line in getReader(fileName):
        entry = fromLog(line).entry
        if entry is not None:
            event = entry['event']
            matcher = re.search('server started at ', event)
            if matcher is not None:
                store = IntervalStore()
                continue
            matcher = re.search('session #(.*) created', event)
            if matcher is not None:
                store.activate(matcher.group(1), entry['time'])
                continue
            matcher = re.search('destroyed  for /rest REST endpoint. Session number (.*)', event)
            if matcher is not None:
                store.deactivate(matcher.group(1))
                continue
    store.show("SINCE LAST RESTART")
    