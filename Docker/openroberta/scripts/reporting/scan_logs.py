'''
analyse the openroberta log and stat file as written by logback:

@author: rbudde
'''

import time
from util import *
from entry import *
from workflows import *

baseDir = 'D:/data/openroberta-lab/server/master/admin/logging/statistics-2020'
lfile = '08.log'
sfile = 's12.log'
zfile = 's09.log.zip'
ofile = 's10.log'
fromTime = '0000' # e.g. '2019-09-03 12:05'
untilTime = '9999' # e.g. '2019-09-20'

if __name__ == "__main__":
    start = time.time()

    #openSessionsSinceLastRestart(baseDir,lfile)
    #sessionsActions(fromTime, untilTime,baseDir,sfile, '34-720', '34-721', '34-722')
    #oneSession(fromTime,untilTime,baseDir,sfile,'72')
    #oneSessionActions(fromTime,untilTime,baseDir,sfile,'72')
    #groupLogEntries(fromTime,untilTime,'h',lfile,baseDir,'event','code generation started')
    #groupStatActions(fromTime,untilTime,'h',sfile,baseDir,'Initialization') # 'Initialization' 'UserLogin' 'SimulationRun' 'ProgramRun' 'ProgramRunBack'
    #groupCountryCode(fromTime,untilTime,'h',sfile,baseDir)
    #processInitData(fromTime,untilTime,sfile,baseDir)
    #processRobotUsage(fromTime,untilTime,sfile,baseDir)
    #processSessions('2019-10-08 03:00:00','9999',ofile,baseDir)
    #sessionsAfterLastServerRestart(fromTime,untilTime,sfile)
    groupEntriesByTime(fromTime,untilTime,'h',baseDir,lfile,None)
    
    end = time.time()
    print("run for {:.3f} sec".format(end - start))