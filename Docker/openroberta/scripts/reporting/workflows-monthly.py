'''
analyse the openroberta statistics file of a whole month (usually done at the first day of the next month :-)

@author: rbudde
'''

from datetime import datetime
import time
from util import *
from store import *
from entry import *

def processInitData(logDir, logFileNameOptionallyWithZip, outputDir, fromTime='0000', untilTime='9999'):
    sessionIdStore = Store()
    groupInits = Store(groupBy='d')
    groupCountryCode = Store()
    groupBrowser = Store()
    groupOperatingSystem = Store()
    groupDeviceType = Store()
    for line in getReader(logDir, logFileNameOptionallyWithZip):
        fromStat(line).after(fromTime).before(untilTime).filterVal('action','Initialization').uniqueKey('sessionId', sessionIdStore)\
        .groupStore(groupInits).mapKey('args')\
        .keyStore('CountryCode', groupCountryCode).keyStore('Browser', groupBrowser).keyStore('OS', groupOperatingSystem).keyStore('DeviceType', groupDeviceType)
        
    groupInits.show(fmt='{:12s} {:5d}',title='initialization calls per day')
    groupCountryCode.show(fmt='{:12s} {:5d}',title='country codes')
    groupBrowser.show(fmt='{:40s} {:5d}',title='browser types')
    groupOperatingSystem.show(fmt='{:40s} {:5d}',title='operating systems')
    groupDeviceType.show(fmt='{:40s} {:5d}',title='device types')
    
    lastMonth = logFileNameOptionallyWithZip[:2]
    dirPrefix = outputDir + '/' + lastMonth + '_'
    initPlotFile = dirPrefix + 'uniqueSessions.png' 
    countrycodePlotFile = dirPrefix + 'countrycode.png' 
    browserTypesPlotFile = dirPrefix + 'browserTypes.png' 
    operatingSystemsPlotFile = dirPrefix + 'operatingSystems.png' 
    deviceTypesPiePlotFile = dirPrefix + 'deviceTypesPie.png' 
    deviceTypesBarPlotFile = dirPrefix + 'deviceTypesBar.png'
    
    groupInits.showBar(title='Unique Sessions',file=initPlotFile, legend=(1.04,0))
    groupCountryCode.showPie(title='country codes',file=countrycodePlotFile)
    groupBrowser.showPie(title='browser types',file=browserTypesPlotFile)
    groupOperatingSystem.showPie(title='operating systems',file=operatingSystemsPlotFile)
    groupDeviceType.showPie(title='device types',file=deviceTypesPiePlotFile)
    groupDeviceType.showBar(title='device types',file=deviceTypesBarPlotFile)
    
def processRobotUsage(fromTime, untilTime, baseDir, fileName):
    sessionIdRobotSet = Store(storeSet=True)
    for line in getReader(baseDir, fileName):
        fromStat(line).after(fromTime).before(untilTime).filterVal('action','ServerStart','Initialization','ChangeRobot','SessionDestroy',negate=True,substring=False)\
        .keyValStore('sessionId','robotName', sessionIdRobotSet)
    robotSessionIdSet = Store()
    invertStore(sessionIdRobotSet,robotSessionIdSet)
    robotSessionIdSet.show(fmt='{:40s} {:5d}',title='robotName used w.o. init+change')

def monthly(logDir, logFile, outputDir, printer=print):
    Store.printer = printer
    fromTime = '0000'
    untilTime = '9999'
    processInitData(logDir,logFile,outputDir)
    processRobotUsage(fromTime,untilTime,logDir,logFile)

if __name__ == "__main__":
    logDir = sys.argv[1]
    logFile = sys.argv[2]
    outputDir = sys.argv[3]
    outputFile = sys.argv[4] if len(sys.argv) >=5 else None
    if outputFile is None:
        printer = print
    else:
        outputFileHandle = open(outputDir + '/' + outputFile, 'w')
        printer = lambda text: outputFileHandle.write(text + '\n')
    start = time.time()
    monthly(logDir, logFile, outputDir, printer=printer)
    end = time.time()
    print("run finished after {:.3f} sec".format(end - start)) 