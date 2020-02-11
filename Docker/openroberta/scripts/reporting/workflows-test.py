'''
test/debug the reporting program w.t.h. of this file :-) 

@author: rbudde
'''

from datetime import datetime
import time
from util import *
from store import *
from entry import *

if __name__ == "__main__":
    logDir = "D:/data/openroberta-lab/server/master/admin/logging/statistics-2020"
    logFile = "01.log.zip"
    outputFileHandle = open("D:/temp/testoutput.txt", 'w')
    printer = lambda text: outputFileHandle.write(text + '\n')
    start = time.time()
    Store.printer = printer
    fromTime = '0000'
    untilTime = '9999'
    printer('started at ' + time.strftime('%Y-%m-%d %H:%M:%S'))

    for line in getReader(logDir, logFile):
        fromStat(line).after(fromTime).before(untilTime)\
        .filterVal('action','ServerStart','Initialization','ChangeRobot','SessionDestroy',negate=True,substring=False)\
        .filterVal('robotName','',substring=False)\
        .showEntry(printer)
        
    end = time.time()
    print("run finished after {:.3f} sec".format(end - start)) 