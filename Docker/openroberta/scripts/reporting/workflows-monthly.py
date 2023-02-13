'''
analyse the openroberta statistics file of a whole month (usually done at the first day of the next month :-)

@author: rbudde
'''

from datetime import datetime
import time
from util import *
from store import *
from entry import *
from pathlib import Path, WindowsPath


def processInitData(logDir, outputDir, logFileNameOptionallyWithZip, fromTime='0000', untilTime='9999'):
    sessionIdStore = Store()
    groupInits = Store(groupBy='d')
    groupCountryCode = Store()
    groupBrowser = Store()
    groupOperatingSystem = Store()
    groupDeviceType = Store()
    for line in getReader(logDir, logFileNameOptionallyWithZip):
        fromStat(line).after(fromTime).before(untilTime).filterVal('action', 'Initialization').uniqueKey('sessionId', sessionIdStore)\
            .groupStore(groupInits).mapKey('args')\
            .keyStore('CountryCode', groupCountryCode).keyStore('Browser', groupBrowser).keyStore('OS', groupOperatingSystem).keyStore('DeviceType', groupDeviceType)

    condenseOperatingSystem = Store()
    condenseStore(groupOperatingSystem, condenseOperatingSystem, condenseOS)
    cutCountryCode = Store()
    cutStore(groupCountryCode, cutCountryCode,
             nameForOther="-other-", lowerLimitForOther=1000)
    groupInits.show(fmt='{:12s} {:5d}', title='initialization calls per day')
    cutCountryCode.show(fmt='{:12s} {:5d}', title='country codes')
    groupBrowser.show(fmt='{:40s} {:5d}', title='browser types')
    groupOperatingSystem.show(fmt='{:40s} {:5d}', title='operating systems')
    condenseOperatingSystem.show(
        fmt='{:40s} {:5d}', title='operating systems (grouped)')
    groupDeviceType.show(fmt='{:40s} {:5d}', title='device types')

    lastMonth = logFileNameOptionallyWithZip[:2]
    filePrefix = lastMonth + '_'
    initPlotFile = outputDir / (filePrefix + 'uniqueSessions.png')
    countrycodePlotFile = outputDir / (filePrefix + 'countrycode.png')
    browserTypesPlotFile = outputDir / (filePrefix + 'browserTypes.png')
    operatingSystemsPlotFile = outputDir / \
        (filePrefix + 'operatingSystems.png')
    deviceTypesPiePlotFile = outputDir / (filePrefix + 'deviceTypesPie.png')
    deviceTypesBarPlotFile = outputDir / (filePrefix + 'deviceTypesBar.png')

    groupInits.showBar(title='Unique Sessions',
                       file=initPlotFile, legend=(1.04, 0))
    cutCountryCode.showPie(title='country codes', file=countrycodePlotFile)
    groupBrowser.showPie(title='browser types', file=browserTypesPlotFile)
    condenseOperatingSystem.showPie(
        title='operating systems (grouped)', file=operatingSystemsPlotFile)
    groupDeviceType.showPie(title='device types', file=deviceTypesPiePlotFile)
    groupDeviceType.showBar(title='device types', file=deviceTypesBarPlotFile)


def processRobotUsage(logDir, outputDir, logFileNameOptionallyWithZip, fromTime='0000', untilTime='9999'):
    sessionIdRobotSet = Store(storeSet=True)
    for line in getReader(logDir, logFileNameOptionallyWithZip):
        fromStat(line).after(fromTime).before(untilTime).filterVal('action', 'ServerStart', 'Initialization', 'ChangeRobot', 'SessionDestroy', negate=True, substring=False)\
            .keyValStore('sessionId', 'robotName', sessionIdRobotSet)
    robotSessionIdSet = Store()
    invertStore(sessionIdRobotSet, robotSessionIdSet)
    robotSessionIdSet.show(
        fmt='{:40s} {:5d}', title='robotName used w.o. init+change')
    lastMonth = logFileNameOptionallyWithZip[:2]
    robotUsagePiePlotFile = outputDir / (lastMonth + '_' + 'robotUsagePie.png')
    robotSessionIdSet.showPie(title='robot usage', file=robotUsagePiePlotFile)


def monthly(logDir, outputDir, logFile, printer=print):
    Store.printer = printer
    processInitData(logDir, outputDir, logFile)
    processRobotUsage(logDir, outputDir, logFile)


if __name__ == "__main__":
    logDir = Path(sys.argv[1])
    logFile = sys.argv[2]
    outputDir = Path(sys.argv[3])
    outputFile = sys.argv[4] if len(sys.argv) >= 5 else None
    if outputFile is None:
        printer = print
    else:
        outputFileHandle = open(outputDir / outputFile, 'w')
        def printer(text): return outputFileHandle.write(text + '\n')
    start = time.time()
    monthly(logDir, outputDir, logFile, printer=printer)
    end = time.time()
    print("run finished after {:.3f} sec".format(end - start))
