'''
analyse the openroberta log and stat file as written by logback:

@author: rbudde
'''

import time
from util import *
from entry import *
from workflows import *

lfile = 'D:/ProjekteArbeit/OPEN-ROBERTA/log/l09-3.log'
sfile = 'D:/ProjekteArbeit/OPEN-ROBERTA/log/s09.log'
ofile = 'D:/ProjekteArbeit/OPEN-ROBERTA/log/s10.log'
fromTime = '0000' # e.g. '2019-09-03 12:05'
untilTime = '9999' # e.g. '2019-09-20'

if __name__ == "__main__":
    start = time.time()

    #openSessionsSinceLastRestart(lfile)
    #sessionsActions(fromTime, untilTime, sfile, '34-720', '34-721', '34-722')
    #oneSession(fromTime,untilTime,sfile,'72')
    #oneSessionActions(fromTime,untilTime,sfile,'72')
    #groupLogEntries(fromTime,untilTime,'h',lfile,'event','code generation started')
    #groupStatActions(fromTime,untilTime,'h',sfile,'Initialization') # 'Initialization' 'UserLogin' 'SimulationRun' 'ProgramRun' 'ProgramRunBack'
    #groupCountryCode(fromTime,untilTime,'h',sfile)
    processInitData(fromTime,untilTime,sfile)
    processRobotUsage(fromTime,untilTime,sfile)
    #processSessions('2019-10-08 03:00:00','9999',ofile)
    
    end = time.time()
    print("run for {:.3f} sec".format(end - start))